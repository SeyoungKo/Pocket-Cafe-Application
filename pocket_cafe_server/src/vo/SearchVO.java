package vo;

public class SearchVO {

	int idx, manager_idx;
	String name, location ,notice,photo1, photo2 ,openclose ,tel , distance;
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	double lat, lng;
	
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public double getLat() {
		return lat;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public String getOpenclose() {
		return openclose;
	}
	public void setOpenclose(String openclose) {
		this.openclose = openclose;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public int getManager_idx() {
		return manager_idx;
	}
	public void setManager_idx(int manager_idx) {
		this.manager_idx = manager_idx;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPhoto1() {
		return photo1;
	}
	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}
	
	public String getPhoto2() {
		return photo2;
	}
	
	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}
	
}
