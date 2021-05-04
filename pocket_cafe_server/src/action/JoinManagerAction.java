package action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MemberDAO;
import dao.StoreDAO;
import vo.MemberVO;
import vo.StoreVO;

@WebServlet("/join_manager.do")
public class JoinManagerAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		String nickname = request.getParameter("nickname");
		String tel = request.getParameter("tel");
		String store_name = request.getParameter("store_name");
		String store_loc = request.getParameter("store_loc");
		String store_lat = request.getParameter("store_lat");
		String store_lng = request.getParameter("store_lng");
		
		String resultStr = "";
		
		if( id != null && pwd != null && nickname != null && tel != null && store_name != null && store_loc != null && store_lat != null && store_lng != null ) {
			
			MemberVO member_vo = new MemberVO();
			member_vo.setId(id);
			member_vo.setPwd(pwd);
			member_vo.setNickname(nickname);
			member_vo.setTel(tel);
			
			int result_join = MemberDAO.getInstance().insertManager( member_vo );
			
			if( result_join >= 1 ) {
				
				member_vo = MemberDAO.getInstance().selectId(id);
				int manager_idx = member_vo.getIdx();
				
				StoreVO store_vo = new StoreVO();
				store_vo.setManager_idx(manager_idx);
				store_vo.setName(store_name);
				store_vo.setLocation(store_loc);
				store_vo.setLat(store_lat);
				store_vo.setLng(store_lng);
				
				int result_store = StoreDAO.getInstance().insertStore(store_vo);
				
				if( result_store >= 1 ) {
					resultStr = "success";
				}else {
					resultStr = "fail_store";
				}
				
			}else {
				resultStr = "fail_join";
			}
			
			response.getWriter().println(String.format("[{'result':'%s'}]", resultStr));
			
		}
		
	}

}
