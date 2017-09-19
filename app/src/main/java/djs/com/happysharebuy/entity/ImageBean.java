package djs.com.happysharebuy.entity;

import java.io.Serializable;

/**
 * Created by bin on 2017/9/15.
 * 图片的Bean
 */

public class ImageBean implements Serializable {

    private String imgurl;
    private int Id;
    private int compid;
    private boolean isdel;
    private String addtime;
    private boolean isDelect;

    public boolean isDelect() {
        return isDelect;
    }

    public void setDelect(boolean delect) {
        isDelect = delect;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCompid() {
        return compid;
    }

    public void setCompid(int compid) {
        this.compid = compid;
    }

    public boolean isdel() {
        return isdel;
    }

    public void setIsdel(boolean isdel) {
        this.isdel = isdel;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
