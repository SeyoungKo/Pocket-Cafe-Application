package action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MemberDAO;
import vo.MemberVO;

@WebServlet("/change_memberinfo.do")
public class ChangeMenberinfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String id = request.getParameter("id");
		String nickname = request.getParameter("nickname");
		String tel = request.getParameter("tel");
		
		if( id != null && nickname != null && tel != null ) {
			
			MemberDAO dao = MemberDAO.getInstance();
			
			MemberVO vo = new MemberVO();
			vo.setId(id);
			vo.setNickname(nickname);
			vo.setTel(tel);
			
			String resultStr;
			int result = dao.changeInfo(vo);
			
			if( result >= 1 ) {
				resultStr = "success";
			}else {
				resultStr = "fail";
			}
			
			response.getWriter().println(String.format("[{'result':'%s'}]", resultStr));
		
			
		}
		
	}

}
