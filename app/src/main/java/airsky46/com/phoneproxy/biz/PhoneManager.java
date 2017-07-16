package airsky46.com.phoneproxy.biz;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by airsky46 on 2017/7/16.
 */

public class PhoneManager {
    private Uri smsUri = Uri.parse("content://sms/inbox");
    private Context context
    private final String phoneProxy = "phoneProxyXML";
    private SharedPreferences sharedPreferences;

    private final String lastSmsDateKey = "lastSmsDate";
    private final String smsProxyNum = "smsProxyNum";
    private final String phoneProxyNum = "phoneProxyNum";


    public PhoneManager(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(phoneProxy, Context.MODE_PRIVATE);
    }

    private long getLastSmsDate() {
        return sharedPreferences.getLong(lastSmsDateKey, 0);
    }

    public long getPhoneProxyNum() {
        return sharedPreferences.getLong(phoneProxyNum, 0);
    }

    public void addPhoneProxyNum(long num2add) {
        sharedPreferences.edit().putLong(phoneProxy, getPhoneProxyNum() + num2add).commit();
    }


    public void setCurrentSmsDate(Long currentSmsDate) {
        sharedPreferences.edit().putLong(lastSmsDateKey, currentSmsDate).commit();
    }

    public long getSmsProxyNum() {
        return sharedPreferences.getLong(smsProxyNum, 0);
    }

    private void addSmsProxyNum(long num2add) {
        sharedPreferences.edit().putLong(smsProxyNum, getSmsProxyNum() + num2add).commit();
    }

    public List<SMSDO> getSMSFromPhone() {
        List<SMSDO> smsdoList = new ArrayList<SMSDO>();
        Cursor cursor = null;
        // 添加异常捕捉
        try {
            cursor = context.getContentResolver().query(smsUri,
                    new String[]{"_id", "address", "read", "body", "date"},
                    null, null, "date desc"); // datephone想要的短信号码
            if (cursor != null) { // 当接受到的新短信与想要的短信做相应判断
                long lastSmsTime = getLastSmsDate();
                long newLastSmsTime = 0;
                while (cursor.moveToNext()) {
                    String body = cursor.getString(cursor.getColumnIndex("body"));// 短信内容
                    String adress = cursor.getString(cursor.getColumnIndex("address"));// 发送短信的好吗或地址
                    long smsdate = Long.parseLong(cursor.getString(cursor
                            .getColumnIndex("date")));
                    if (newLastSmsTime == 0) {
                        newLastSmsTime = smsdate;
                    }
                    // 如果当前时间和短信时间间隔超过60秒,或者短信创建时间在上一条短信时间之前，认为这条短信无效
                    if (smsdate <= lastSmsTime) {
                        break;
                    }
                    smsdoList.add(new SMSDO(adress, body));
                }
                setCurrentSmsDate(newLastSmsTime);
            }
        } catch (Exception e) {
            Log.e("smsManager", e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        addSmsProxyNum(smsdoList.size());
        return smsdoList;
    }
}
