package airsky46.com.phoneproxy.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import airsky46.com.phoneproxy.biz.constanse.UIMessage;

/**
 * Created by airsky46 on 2017/7/16.
 */

public class SMSReceiver extends ContentObserver {
    private Context mContext;
    private Handler mHandler; // 更新UI线程

    public SMSReceiver(Context mContext,
                       Handler mHandler) {
        super(mHandler); // 所有ContentObserver的派生类都需要调用该构造方法
        this.mContext = mContext;
        this.mHandler = mHandler;
    }

    /**
     * 当观察到的Uri发生变化时，回调该方法去处理。所有ContentObserver的派生类都需要重载该方法去处理逻辑
     * selfChange:回调后，其值一般为false，该参数意义不大
     */
    @Override
    public void onChange(boolean selfChange) {
        // TODO Auto-generated method stub
        super.onChange(selfChange);
        System.out.println();
        mHandler.obtainMessage(UIMessage.MSG_INBOX.getMessageType(), "SMS Received").sendToTarget();
    }

}