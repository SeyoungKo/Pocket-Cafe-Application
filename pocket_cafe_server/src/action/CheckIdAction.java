package action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MemberDAO;
import vo.MemberVO;

@WebServlet("/check_id.do")
public class CheckIdAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String result = "";
		String id = request.getParameter("id");
		
		if( id != null ) {
			
			MemberDAO dao = MemberDAO.getInstance();
			MemberVO vo = dao.selectId( id );
	
			if( vo == null ) {
				result = "success";
			}else {
				result = "fail";
			}
		
			response.getWriter().println(String.format("[{'result':'%s'}]", result));
		
		}
		//결과가 success: 사용가능한 아이디, fail: 이미 존재하는 아이디
	}

}
