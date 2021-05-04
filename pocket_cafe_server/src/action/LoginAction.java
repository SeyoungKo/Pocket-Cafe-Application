package action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MemberDAO;
import vo.MemberVO;

@WebServlet("/login.do")
public class LoginAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		int idx = 0;
		int division = 0;
		
		if( id != null && pwd != null ) {
			
			MemberDAO dao = MemberDAO.getInstance();
			MemberVO vo = dao.selectId(id);
			
			String result = "";
			
			if( vo == null ) {
				
				result = "fail_id";
				
			}else {
				
				MemberVO login_vo = new MemberVO();
				login_vo.setId(id);
				login_vo.setPwd(pwd);
				
				vo = dao.login( login_vo );
				
				if( vo == null ) {
					result = "fail_pwd";
				}else {
					result = "success";
					division = vo.getDivision();
					idx = vo.getIdx();
				}
				
			}
			
			response.getWriter().println(String.format("[{'result':'%s', 'idx':'%d', 'division':'%d'}]", result, idx, division));
			
		}
		
	}

}
