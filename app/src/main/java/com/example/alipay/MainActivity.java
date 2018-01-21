package com.example.alipay;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.example.alipay.utils.OrderInfoUtil2_0;
import com.example.alipay.utils.PayResult;

import java.util.Map;

/**
 *  重要说明:
 *
 *  这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 *  真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 *  防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int SDK_PAY_FLAG = 1001;
    //private String RSA_PRIVATES = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCNYm+oveZOECAjwrH1E+RHznGxVqdAKI/teijarKYIV7RjpNyfMaEaI0ms8vd9aXtN6gEeSPvBQmWVunY1FWfLpAOkSYGJLJ8GJEgiNTAstCgkHw21DaojrD9LxoUZbvfBwWXiDLDAPUGiU6pnG7AkClJuzSETMCTWsrcB35Y9MMprnPaXgNG8+MJ6P2Z1xmN51uNQw4Z99iDrR27lrQH/OXNzLnRDzlj0rwoYFHDSPds58qmjVRTcBXCVpZoLmuf4OfSc8gplNGz/qs/rjOfKEOrcZQeKw1SCkG5U4ZHsMM5XmwbCGg20G9+BokYdHJNKFKu/+kwu69No1Mcy8RTfAgMBAAECggEAIXBCkFo5egT+VPbbN+d4ejMtWI/yBo6RW80klHN44Ug89cQsGcqXG6N07V6ZgiPMceUCVrNUN6UIeZ0cD/n8DoHACr8Hz/Wptr4mAVErD6ecRs7BYyzULJO0dKuDFzzThBPFkO0HcLAMMeQvzSsTQbLfRC1nwS4FyHGELwE+e0IQy3wug7jAid/X2crGC438pwxS7iCjZxsO44WCteCLTjIG/y2AR42wJXSRlPpsGQP6CVgUKa1ATEsoGBDoImDAitnPAyADyOvRMf3jqOcadWq8MtXKPM1KyfM1Sq+NgPawwXxdBHPXB4aDPHmoZm3qb8Nat1VkbTfnmnFNVNiGAQKBgQDGcR0xEI/oP/HRdhKQJCNguUN2dcXIfbfLj4ff9yMtQ+086W3BpJYO5rq6B8mXU66wg3crKJHwpaQ5a6CXb1U757y2J2qPccKdy3ZXed7z0bEkGxwPzwkAiNXM30KvHO9QxVFX3oILDca2qOk7h5vRrRCH9GHdZkYgf7F0WRFwnwKBgQC2ZKYOVPE881ek0SFHURuTN99M+MsciyLzJNeRpopXCBvViRV3rMvyzCRsciJEqQmZnQM7VDkqh3MtutEDnPv2Qux3Qlhk756Q8PdmS9hPl9WK8NGSSA6AQFGqrV16ngjYRm1h+fm6c6K9YFaoJXw/5qYF48X0hXRE39++TXSzwQKBgBnji/Fovb2JCh1PkCBp9ouZ3+lGeCUt8ZqHAS0A6v/uyraVpZILzN/ozheTCIPLkRDKNfPVeSSyF3i+R9c52R7VntMM1WQdbUx0zN2gsquQgdG6D7EoS35cW7g8sFB0L+yTsYcLKmASzgfqhXMUwAlc0LlL8rCVtTRsNFR/gjz1AoGAUiANmSRsHvqe+wpjRp5hoS8mL51Srz6C9SIgomdvoPJ4vfRkoyc+Ccwblmzpuyq1tOI640rwFpM4rF2S4WKdHOxTVvubm489QZwOeZQrCOOf9liqtIgXZ24Ol6BKF/zylJdZhyUsaeTJYSXwvvNp98fd94bwykIQ8TYwo5pyssECgYAZC+l1Ok0VJyisBLgOHoAuwYmWbFRC0RJAwQQoTs4/ozHiR+kFOgiHY6W7sjfgdMej+0U0gNifm2nn0lj1KRuOXiAzkzRBTkiwDChP0PAa2ns9GSbxApRVPJJzeM2NlRX4ptscjKUqWB3tgqPNWDTjW0d7iCYeFWkx0GfRgSwHaQ==";
    // 私钥
    private String RSA_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCWRHKIXqhqpkD65ICzqnUvwQs980oJcNwiUg/6jCDNoOG5cv/a0xjksbo/QHrEiCuzko6CGGC4B4uiRM9bdOLPOE4EHe0cF0TbpFbT3pGCEMvKnFityBLdET0rHHKgR25Cwy88mtSEz8E/jyKotKMy1ApJ1c25JzVPmbgnnh+rVjtoh5DIvS3v7Oz4RM5BVn1RbI4oCGM3vPv6zaoU8/evA7dWcAHg4Ys42M616H/AAplxDA+pBH4O1ZhgWcnEEwZoFtCtpNQQjvlcZoZpM+jsY7Yo7rcS/dv8Wh3qtGelhhuoipMu7jGfvFl/jNn0NhgYH1mCbGiXugrtvGFSGYu5AgMBAAECggEAKWSffCs/D2nOuo4ZWeSZtVbjBuSuv10EdrvORQE2xA4OP+yKyDegQtcma+gSKXMtPqEfenzGn8sMTt3PlOzk0Zo7+2xW9Yb/Y9WoSWKlCIQh7yUUVIKpl5X5GJgUh0xF9kQYDgTEh/VG0YBXVsuzrPu1/dLUga1oRQ4eKWOoa6S/M3W0YzhNg5FGuTk9VKRs7VBnsRCcWIYNAWSz9B5MI53KG9BxtXGUd6c5QCkF8Lak51i289dRoxEQLVjZA+t1m+UTk3FLAvUHX52CX+CXHQ7hD2guwhkmxHbfPGY+FuAiQRZyHcuGIKEibGvoLtR1c9UNTZbJ7nVgDWxRhmCNkQKBgQDi0U53yYUKQ2aCNTIN71yxnLC13WpIRMXgY2EcePR7lQQRlJgiV23FIIHRIQDo71BJ+SYqgaqDRjesdKD7ogIZYt2EmiakGyXH3BSidnLorYseEatby8ImGKSxbzhf5DXJCnPGlWCrbiNSk7JozDXFCnz7JALOmaYZ0vjtrzmPnQKBgQCpmc4i8+S7zcf/YWJwCDZhe9noS2hMesQX13BVnPywBHlDWbGtgoOO1JqlnWF0Y4bo0OoLt3pYy3Frd+GciHiBtjb3A6tcIJNgJVbz93n8PMackV4gfwjgHDVzBAV9I641s06q2M2FkU/tDFmpHbm65Ig3yfETCJEtLQXNh9FHzQKBgFjCK4C4vXUstvqRMos9dfqTJJumj6bEMuMCQMhRlOqff74ZdgdKS0xu0h4yWhbNP5ATDZpor387hSHZaf3Ogh/JDoKLiD+D2B8Dwpdo3sKq1tvncjUz4T/jON+udFMiDOIlUvUNE41CRlRUM+bKsufumMX2a5rJibRlWMA6Z74JAoGAJm1b4F2EaOzyMLHe/OVPdPz73X8gbZuvTHuazzsXocYRt6nd8SNOLZGMTJQBHb7jNWMIdIko5m+KDkCGVNIzA6jGgWdKb0BORA2Ryws6j2Ux1w201wYRe73Ienvhxn3MHgy3eUxQHMBU7K/MrN24VFktZ/6QabCaPDMkSnJIf80CgYEA3HskZb/9hjTEdga1o4lamwfsJzFyqisxyTDvsvpNYTKM3rMVpSd+ao9c7NmuA+3CrUd3/tmJ1TmcGKOSD0Gg7mo/4FrCN0pZsWBlWbGOBDqE3TcyYDhp+/pfsdicPTD4wX57vwa4TXOUz/h0kHf6R44Ik8rSmxGlWe+MzfLnxk8=";
    // appId
    public static final String APPID = "2018012102008076";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_to_alipay_pay).setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    // 同步获取结果
                    String resultInfo = payResult.getResult();
                    Log.e(TAG, "Pay:" + resultInfo);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        /**
         * 特别注意：
         构造交易数据并签名必须在商户服务端完成，商户的应用私钥绝对不能保存在商户APP客户端中，也不能从服务端下发。
         同步返回的数据，只是一个简单的结果通知，商户确定该笔交易付款是否成功需要依赖服务端收到支付宝异步通知的结果进行判断。
         商户系统接收到通知以后，必须通过验签（验证通知中的sign参数）来确保支付通知是由支付宝发送的。建议使用支付宝提供的SDK来完成，详细验签规则参考异步通知验签。
         */
        if (v.getId() == R.id.btn_to_alipay_pay){
            // 秘钥验证的类型 true:RSA2 false:RSA
            boolean rsa = false;
            // 构造支付订单参数列表
            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa);
            // 构造支付订单参数信息
            String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
            // 对支付参数信息进行签名
            String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE, rsa);
            // 订单信息 通过 订单信息 & 加密订单这种公式就拼接出商品信息
            final String orderInfo = orderParam + "&" + sign;
            //异步处理
            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    // 新建任务
                    PayTask alipay = new PayTask(MainActivity.this);
                    // 获取支付结果
                    Map<String, String> result = alipay.payV2(orderInfo, true); // 用户在商户app内部点击付款，是否需要一个loading做为在钱包唤起之前的过渡
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }
    }

}
