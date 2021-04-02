package action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.OrdersDAO;
import vo.OrdersVO;

/**
 * Servlet implementation class OrdersInfoGuestAction
 */
@WebServlet("/orders_info_guest.do")
public class OrdersInfoGuestAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String guest_idx_str = request.getParameter("guest_idx");
		
		if( guest_idx_str != null ) {
			
			int guest_idx = Integer.parseInt( guest_idx_str );
			
			OrdersDAO dao = OrdersDAO.getInstance();
			List<OrdersVO> list = dao.selectOrder_guest(guest_idx);
			
			String jsonArray = "[";
			
			for( int i = 0; i < list.size(); i++ ) {
				
				OrdersVO vo = list.get(i);
				String jsonObject = String.format("{'store_idx':'%d', 'ordertime':'%s', 'order_list':'%s', 'price':'%d', 'done':'%d', 'donetime':'%s'}", vo.getStore_idx(), vo.getOrdertime(), vo.getOrder_list(), vo.getPrice(), vo.getDone(), vo.getDonetime());
				
				jsonArray += jsonObject;
				
				if( i < list.size() - 1 ) {
					jsonArray += ", ";
				}
				
			}
			
			jsonArray += "]";
			
			System.out.println(jsonArray);
			response.setCharacterEncoding("UTF-8"); 
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().println(jsonArray);
			
		}
		
	}

}
