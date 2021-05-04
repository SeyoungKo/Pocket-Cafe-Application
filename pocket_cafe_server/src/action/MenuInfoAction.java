package action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MenuDAO;
import vo.MenuVO;

/**
 * Servlet implementation class MenuInfoAction
 */
@WebServlet("/menu_info.do")
public class MenuInfoAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String store_idx = request.getParameter("idx");
		
		if( store_idx != null ) {
			
			int idx = Integer.parseInt( store_idx );
			List<MenuVO> list = MenuDAO.getInstance().select_list_storeidx(idx);
			String result = "[";
			
			for( int i = 0; i < list.size(); i++ ) {
				MenuVO vo = list.get(i);
				String json = String.format("{'idx':'%d', 'store_idx':'%d', 'name':'%s', 'price':'%d', 'photo':'%s', 'ice':'%d', 'hot':'%d', 'regular':'%d', 'large':'%d', 'xlarge':'%d', 'takeout':'%d'}", 
											   vo.getIdx(), vo.getStore_idx(), vo.getName(), vo.getPrice(), vo.getPhoto(), vo.getIce(), vo.getHot(), vo.getRegular(), vo.getLarge(), vo.getXlarge(), vo.getTakeout() );
				result += json;
				
				if( i != list.size() - 1 ) {
					result += ", ";
				}
			}
			
			result += "]";
			
			response.setCharacterEncoding("UTF-8"); 
			response.setContentType("text/html; charset=UTF-8");
			System.out.println(result);
			response.getWriter().println(result);
			
		}
		
	}

}
