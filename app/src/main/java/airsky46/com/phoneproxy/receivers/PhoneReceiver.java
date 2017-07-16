package airsky46.com.phoneproxy.receivers;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import airsky46.com.phoneproxy.MainActivity;
import airsky46.com.phoneproxy.biz.PhoneManager;
import airsky46.com.phoneproxy.biz.constanse.UIMessage;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by airsky46 on 2017/7/16.
 */

public class PhoneReceiver extends BroadcastReceiver {
    private static long initcount;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String phoneNumber = intent
                    .getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d("", "call OUT:" + phoneNumber);
        } else {
            if(PhoneManager.getInstance() == null){
                new PhoneManager(context);
            }
            //查了下android文档，貌似没有专门用于接收来电的action,所以，非去电即来电.
            //如果我们想要监听电话的拨打状况，需要这么几步 :
            if (initcount == 0) {
                TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
                manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
                initcount++;
            }
            //设置一个监听器

            Bundle bundle = intent.getExtras();
            String phoneNr = bundle.getString("incoming_number");
            Log.d(intent.getAction(), "phoneNr: " + phoneNr);
        }
    }

    public static PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d("call statue " + TelephonyManager.CALL_STATE_RINGING, "phoneNr: " + incomingNumber);
                    //输出来电号码
                    Log.d(this.getClass().getName(), "get new phone call");
                    PhoneManager.getInstance().addPhoneProxyNum(1);
                    if (MainActivity.instance != null && MainActivity.instance.mHandler != null) {
                        MainActivity.instance.mHandler.obtainMessage(UIMessage.PHONE_IN.getMessageType(), "Phone call Received").sendToTarget();
                    }
                    break;
            }
        }
    };
}