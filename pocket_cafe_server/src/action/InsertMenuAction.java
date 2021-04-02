package action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MenuDAO;
import vo.MenuVO;

@WebServlet("/insert_menu.do")
public class InsertMenuAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		String store_idx_str = request.getParameter("store_idx");
		String name = request.getParameter("name");
		String price_str = request.getParameter("price");
		String photo = request.getParameter("photo");
		String hot_str = request.getParameter("hot");
		String ice_str = request.getParameter("ice");
		String regular_str = request.getParameter("regular");
		String large_str = request.getParameter("large");
		String xlarge_str = request.getParameter("xlarge");
		String takeout_str = request.getParameter("takeout");
		
		if( store_idx_str != null && name != null && price_str != null && photo != null && hot_str != null && ice_str != null && regular_str != null && large_str != null && xlarge_str != null && takeout_str != null && takeout_str != null ) {
			int store_idx = Integer.parseInt( store_idx_str );
			int price = Integer.parseInt( price_str );
			int hot = Integer.parseInt( hot_str );
			int ice = Integer.parseInt( ice_str );
			int regular = Integer.parseInt( regular_str );
			int large = Integer.parseInt( large_str );
			int xlarge = Integer.parseInt( xlarge_str );
			int takeout = Integer.parseInt( takeout_str );
			
			MenuDAO dao = MenuDAO.getInstance().getInstance();
			MenuVO vo = new MenuVO();
			vo.setStore_idx(store_idx);
			vo.setName(name);
			vo.setPrice(price);
			vo.setPhoto(photo);
			vo.setHot(hot);
			vo.setIce(ice);
			vo.setRegular(regular);
			vo.setLarge(large);
			vo.setXlarge(xlarge);
			vo.setTakeout(takeout);
			
			int result = dao.insert_menu(vo);
			String resultStr = "fail";
			
			if( result >= 1 ) {
				resultStr = "success";
			}
			
			response.setCharacterEncoding("UTF-8"); 
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().println(String.format("[{'result':'%s'}]", resultStr));
			
		}
		
	}

}
