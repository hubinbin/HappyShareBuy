package djs.com.happysharebuy.entity;

/**
 * Created by bin on 2017/7/3.
 * 用户信息的Bean
 */

public class UserInfoBean {

    private int status;
    private String msg;
    private String tximg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTximg() {
        return tximg;
    }

    public void setTximg(String tximg) {
        this.tximg = tximg;
    }
}
