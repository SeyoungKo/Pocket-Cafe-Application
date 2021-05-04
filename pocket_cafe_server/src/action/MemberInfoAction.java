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
 * Servlet implementation class MemberInfoAction
 */
@WebServlet("/member_info.do")
public class MemberInfoAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String id = request.getParameter("id");
		String idxStr = request.getParameter("idx");
		String result = "";
		
		if( id != null ) {
			
			MemberDAO dao = MemberDAO.getInstance();
			MemberVO vo = dao.selectId(id);
			
			int idx = vo.getIdx();
			String pwd = vo.getPwd();
			String nickname = vo.getNickname();
			String tel = vo.getTel();
			int division = vo.getDivision();
			
			result = String.format("[{'idx':'%d', 'id':'%s', 'pwd':'%s', 'nickname':'%s', 'tel':'%s', 'division':'%d'}]", idx, id, pwd, nickname, tel, division);
			response.getWriter().println(result);
			
		}else if( idxStr != null ) {
			
			int idx = Integer.parseInt(idxStr);
			MemberDAO dao = MemberDAO.getInstance();
			MemberVO vo = dao.selectIdx(idx);
			
			String member_id = vo.getId();
			String pwd = vo.getPwd();
			String nickname = vo.getNickname();
			String tel = vo.getTel();
			int division = vo.getDivision();
			
			result = String.format("[{'idx':'%d', 'id':'%s', 'pwd':'%s', 'nickname':'%s', 'tel':'%s', 'division':'%d'}]", idx, member_id, pwd, nickname, tel, division);
			response.getWriter().println(result);
			
		}
		
	}

}
