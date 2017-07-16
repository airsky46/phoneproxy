package airsky46.com.phoneproxy;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.EditText;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import airsky46.com.phoneproxy.biz.SMSDO;
import airsky46.com.phoneproxy.biz.PhoneManager;
import airsky46.com.phoneproxy.biz.constanse.UIMessage;
import airsky46.com.phoneproxy.receivers.SMSReceiver;

public class MainActivity extends AppCompatActivity {
    public static MainActivity instance;
    private SMSReceiver smsReceiver;
    private PhoneManager phoneManager;
    public  Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (UIMessage.valueOf(msg.what)) {
                case MSG_INBOX:
                    updateSMSInfo();
                    break;
                case PHONE_IN:
                    updatePhoneInfo();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        smsReceiver = new SMSReceiver(MainActivity.this, mHandler);
        phoneManager = new PhoneManager(MainActivity.this);
        getContentResolver().registerContentObserver(
                Uri.parse("content://sms/"), true, smsReceiver);// 注册监听短信数据库的变化
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE}, 1);

        instance = MainActivity.this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private void updateSMSInfo() {
        EditText smsnumtext = (EditText) findViewById(R.id.smsproxyshow);
        List<SMSDO> smsdoList = phoneManager.getSMSFromPhone();
        for (SMSDO smsdo : smsdoList) {
            smsnumtext.setText(String.valueOf(phoneManager.getSmsProxyNum()));
            Log.d(this.getClass().getName(), smsdo.getAdress() + ":" + smsdo.getBody());
        }
    }

    private void updatePhoneInfo() {
        EditText phonenumtext = (EditText) findViewById(R.id.phoneproxyshow);
        phonenumtext.setText(String.valueOf(phoneManager.getPhoneProxyNum()));
    }

}
