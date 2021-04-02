package action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MemberDAO;
import vo.MemberVO;

/**
 * Servlet implementation class ChangeMemberpwd
 */
@WebServlet("/change_memberpwd.do")
public class ChangeMemberpwd extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		
		if( id != null && pwd != null ) {
			
			MemberDAO dao = MemberDAO.getInstance();
			
			MemberVO vo = new MemberVO();
			vo.setId(id);
			vo.setPwd(pwd);
			
			int result = dao.changePwd(vo);
			String resultStr;
			
			if( result >= 1 ) {
				resultStr = "success";
			}else {
				resultStr = "fail";
			}
			
			response.getWriter().println(String.format("[{'result':'%s'}]", resultStr));
			
		}
		
	}

}
