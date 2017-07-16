package airsky46.com.phoneproxy.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import airsky46.com.phoneproxy.MainActivity;
import airsky46.com.phoneproxy.biz.PhoneManager;
import airsky46.com.phoneproxy.biz.constanse.UIMessage;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by airsky46 on 2017/7/16.
 */

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果是接收到短信
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVER")) {
            StringBuilder sb = new StringBuilder();
            //接收由SMS传过来的数据
            Bundle bundle = intent.getExtras();
            //判断是否有数据
            if (bundle != null) {
                Log.d(this.getClass().getName(), "get new sms message");
                if (MainActivity.instance != null && MainActivity.instance.mHandler != null) {
                    MainActivity.instance.mHandler.obtainMessage(UIMessage.MSG_INBOX.getMessageType(), "SMS Received").sendToTarget();
                }
            }
        }
    }
}