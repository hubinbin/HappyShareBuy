package djs.com.happysharebuy.entity;

/**
 * Created by bin on 2017/7/4.
 * 行业的Bean
 */

public class IndustryBean {

    private int Id;
    private String itemName;
    private int fid;
    private int sortn;
    private int num;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getSortn() {
        return sortn;
    }

    public void setSortn(int sortn) {
        this.sortn = sortn;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
