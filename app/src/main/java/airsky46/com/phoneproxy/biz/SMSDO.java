package airsky46.com.phoneproxy.biz;

/**
 * Created by airsky46 on 2017/7/16.
 */

public class SMSDO {
    private String adress;
    private String body;

    public SMSDO(String adress, String body) {
        this.adress = adress;
        this.body = body;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAdress() {
        return this.adress;
    }

    public String getBody() {
        return this.body;
    }
}
