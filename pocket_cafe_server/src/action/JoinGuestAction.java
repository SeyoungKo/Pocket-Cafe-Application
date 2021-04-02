package action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MemberDAO;
import vo.MemberVO;

@WebServlet("/join_guest.do")
public class JoinGuestAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		String nickname = request.getParameter("nickname");
		String tel = request.getParameter("tel");
		
		if( id != null && pwd != null && nickname != null && tel != null ) {
			
			MemberVO vo = new MemberVO();
			
			vo.setId( id );
			vo.setPwd( pwd );
			vo.setNickname( nickname );
			vo.setTel( tel );
			
			MemberDAO dao = MemberDAO.getInstance();
			int result = dao.insertGuest(vo);
			
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
