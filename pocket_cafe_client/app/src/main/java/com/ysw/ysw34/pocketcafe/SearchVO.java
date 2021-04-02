package com.ysw.ysw34.pocketcafe;

public class SearchVO {
    int idx,manager_idx;
    String name, location,photo1, photo2, openclose, tel,lat,lng, notice, km;

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getKm() {
        return km;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getOpenclose() {

        return openclose;

    }

    public String getTel() {
        return tel;
    }

    public void setOpenclose(String openclose) {
        this.openclose = openclose;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;

    }

    public String getLocation() {
        return location;
    }

    public String getPhoto1() {
        return photo1;
    }

    public int getIdx() {
        return idx;
    }

    public int getManager_idx() {
        return manager_idx;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public void setManager_idx(int manager_idx) {
        this.manager_idx = manager_idx;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto2() {
        return photo2;
    }
}
