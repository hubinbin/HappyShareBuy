package djs.com.happysharebuy.entity;

import java.io.Serializable;

/**
 * Created by bin on 2017/7/5.
 * 业务记录的Bean
 */

public class BusinessBean implements Serializable {

    private int Id;
    private int compId;
    private int perId;
    private String txt1;
    private String addtime;
    private String tximg;
    private String realName;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCompId() {
        return compId;
    }

    public void setCompId(int compId) {
        this.compId = compId;
    }

    public int getPerId() {
        return perId;
    }

    public void setPerId(int perId) {
        this.perId = perId;
    }

    public String getTxt1() {
        return txt1;
    }

    public void setTxt1(String txt1) {
        this.txt1 = txt1;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getTximg() {
        return tximg;
    }

    public void setTximg(String tximg) {
        this.tximg = tximg;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
