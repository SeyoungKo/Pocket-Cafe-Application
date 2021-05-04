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
 * Servlet implementation class OrderFinish
 */
@WebServlet("/orderFinish.do")
public class OrderFinishAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String idxStr = request.getParameter("idx");
		
		if( idxStr != null ) {
			int idx = Integer.parseInt(idxStr);
		
			OrdersDAO dao = OrdersDAO.getInstance();
			int result = dao.OrderFinish(idx);
			String resultStr = "fail";
			
			if( result >= 1 ) {
				resultStr = "success";
			}
			
			response.getWriter().println(String.format("[{'result':'%s'}]", resultStr));
		}
	}

}
