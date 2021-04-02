package action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.OrdersDAO;
import vo.OrdersVO;

/**
 * Servlet implementation class InsertOrderAction
 */
@WebServlet("/insert_order.do")
public class InsertOrderAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		String guest_idx_str = request.getParameter("guest_idx");
		String store_idx_str = request.getParameter("store_idx");
		String order_list = request.getParameter("order_list");
		String price_str = request.getParameter("price");
		
		if( guest_idx_str != null && store_idx_str != null && order_list != null && price_str != null ) {
			
			int guest_idx = Integer.parseInt(guest_idx_str);
			int store_idx = Integer.parseInt(store_idx_str);
			int price = Integer.parseInt(price_str);
			
			OrdersDAO dao = OrdersDAO.getInstance();
			OrdersVO vo = new OrdersVO();
			vo.setGeust_idx(guest_idx);
			vo.setStore_idx(store_idx);
			vo.setOrder_list(order_list);
			vo.setPrice(price);
			
			int result = dao.insert_order(vo);
			String resultStr = "fail";
			
			if( result >= 1 ) {
				resultStr = "success";
			}
			
			response.getWriter().println(String.format("[{'result':'%s'}]", resultStr));
			
		}
		
	}

}
