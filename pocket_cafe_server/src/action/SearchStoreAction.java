package action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.StoreDAO;
import vo.StoreVO;

/**
 * Servlet implementation class SearchStoreAction
 */
@WebServlet("/search_store.do")
public class SearchStoreAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String result = "";
		
		int idx = Integer.parseInt(request.getParameter("login_idx"));
		
		StoreDAO dao = StoreDAO.getInstance();
		StoreVO vo = dao.selectOneStore(idx);
		
		int store_idx = vo.getIdx();
		
		result = String.format("[{'store_idx':'%d'}]", store_idx);
		response.getWriter().println(result);
		
		System.out.println(result);
	}
	

}
