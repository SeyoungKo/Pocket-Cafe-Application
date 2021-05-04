package action;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MenuDAO;
import vo.MenuVO;

@WebServlet("/delete_menu.do")
public class DeleteMenuAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String idx_str = request.getParameter("idx");
		
		String web_path = "/menu_photo/";
		
		//현재 WebApplication을 관리하는 객체 ServletContext
		ServletContext application = request.getServletContext();
		String real_path = application.getRealPath( web_path );
		
		if( idx_str != null ) {
			
			int idx = Integer.parseInt( idx_str );
			
			MenuDAO dao = MenuDAO.getInstance();
			
			MenuVO vo = dao.select_one_idx(idx);
			String photo = vo.getPhoto();
			
			if( photo != null ) {
				
				if( !photo.equals("") ) {
					File file_photo = new File( real_path + "/" + photo );
					
					if( file_photo.exists() ) {
						file_photo.delete();
					}
				}
			}
			
			int result = dao.delete_menu(idx);
			String resultStr = "fail";
			
			if( result >= 1 ) {
				resultStr = "success";
			}
			
			response.getWriter().println(String.format("[{'result':'%s'}]", resultStr));
		
		}
		
	}

}
