package djs.com.happysharebuy.entity;

/**
 * Created by bin on 2017/7/4.
 * 县市的Bean
 */

public class CountyBean {

    private int DistrictID;
    private String DistrictName;
    private int CityID;

    public int getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(int districtID) {
        DistrictID = districtID;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }
}
