package action;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.StoreDAO;
import vo.StoreVO;

@WebServlet("/update_storephoto2.do")
public class UpdateStorePhoto2Action extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String file_name = request.getParameter("file_name");
		String store_idx = request.getParameter("idx");
		
		String web_path = "/store_photo/";
		
		//현재 WebApplication을 관리하는 객체 ServletContext
		ServletContext application = request.getServletContext();
		String real_path = application.getRealPath( web_path );
		
		
		if( file_name != null && store_idx != null ) {
			
			int idx = Integer.parseInt( store_idx );
			
			StoreDAO dao = StoreDAO.getInstance();
			
			StoreVO original_vo = dao.selectOneStore_idx(idx);
			String original_photo2 = original_vo.getPhoto2();
			
			File photo2 = new File(real_path + "/" + original_photo2);
			
			if( photo2.exists() ) {
				photo2.delete();
			}
			
			StoreVO vo = new StoreVO();
			vo.setIdx(idx);
			vo.setPhoto2(file_name);
			
			int result = dao.updatePhoto2( vo );
			String resultStr = "fail";
			
			if( result >= 1 ) {
				resultStr = "success";
			}
			
			response.getWriter().println(String.format("[{'result':'%s'}]", resultStr));
			
		}
		
	}

}
