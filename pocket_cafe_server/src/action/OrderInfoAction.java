package action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import dao.OrdersDAO;
import vo.OrdersVO;

/**
 * Servlet implementation class MemberInfoAction
 */
@WebServlet("/order_info.do")
public class OrderInfoAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");

		String idxStr = request.getParameter("store_idx");
		int idx = Integer.parseInt(idxStr);
		
		//System.out.println(idx);

		OrdersDAO dao = OrdersDAO.getInstance();
		OrdersVO vo = new OrdersVO();
		vo.setStore_idx(idx);
		
		List<OrdersVO> list = dao.selectOrder(vo);

		String jsonArray = "[";
		
		for(int i=0; i<list.size(); i++) {
			
			OrdersVO select_vo = list.get(i);
			String ordersInfo = "{";
			response.setCharacterEncoding("UTF-8");
			
			ordersInfo += String.format("'orders_idx':'%d', ", select_vo.getOrders_idx());
			ordersInfo += String.format("'guest_idx':'%d', ", select_vo.getGeust_idx());
			ordersInfo += String.format("'order_list':'%s', ", select_vo.getOrder_list());
			ordersInfo += String.format("'price':'%d', ", select_vo.getPrice());
			ordersInfo += String.format("'ordertime':'%s', ", select_vo.getOrdertime());
			ordersInfo += String.format("'done':'%d', ", select_vo.getDone());
			ordersInfo += String.format("'donetime':'%s'", select_vo.getDonetime());
			
		
			/*ordersInfo.put("orders_idx", String.format("%d", list.get(i).getOrders_idx()));
			ordersInfo.put("order_time", String.format("%s", list.get(i).getOrdertime()));
			ordersInfo.put("finish_time", String.format("%s", list.get(i).getArrivingtime()));
			ordersInfo.put("order_list", String.format("%d", list.get(i).getOrders_idx()));
			*/
			ordersInfo += "}";
			jsonArray += ordersInfo;
			
			if( i < list.size() - 1) {
				jsonArray += ", ";
			}
			
		}
		
		jsonArray += "]";
		//System.out.println(jsonArray);

		System.out.println(jsonArray);
		response.getWriter().println(jsonArray.toString());

	}

}
