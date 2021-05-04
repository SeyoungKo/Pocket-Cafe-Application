package vo;

public class OrdersVO {
	
	private int orders_idx, geust_idx, store_idx, price, done;
	private String order_list, ordertime, arrivingtime, donetime;
	
	public int getOrders_idx() {
		return orders_idx;
	}
	
	public void setOrders_idx(int orders_idx) {
		this.orders_idx = orders_idx;
	}
	
	public int getGeust_idx() {
		return geust_idx;
	}

	public void setGeust_idx(int geust_idx) {
		this.geust_idx = geust_idx;
	}

	public int getStore_idx() {
		return store_idx;
	}
	
	public void setStore_idx(int store_idx) {
		this.store_idx = store_idx;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getDone() {
		return done;
	}
	
	public void setDone(int done) {
		this.done = done;
	}
	
	public String getOrder_list() {
		return order_list;
	}
	
	public void setOrder_list(String order_list) {
		this.order_list = order_list;
	}
	
	public String getOrdertime() {
		return ordertime;
	}
	
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	
	public String getArrivingtime() {
		return arrivingtime;
	}
	
	public void setArrivingtime(String arrivingtime) {
		this.arrivingtime = arrivingtime;
	}
	
	public String getDonetime() {
		return donetime;
	}
	
	public void setDonetime(String donetime) {
		this.donetime = donetime;
	}
	
}
