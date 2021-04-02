package action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.StoreDAO;
import vo.StoreVO;

@WebServlet("/change_storeinfo.do")
public class ChangeStoreInfoAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String idx = request.getParameter("manager_idx");
		String name = request.getParameter("name");
		String location = request.getParameter("location");
		String tel = request.getParameter("tel");
		String openclose = request.getParameter("openclose");
		String notice = request.getParameter("notice");
		String lat = request.getParameter("lat");
		String lng = request.getParameter("lng");
		
		if( idx != null && name != null && location != null && tel != null && openclose != null && notice != null & lat != null && lng != null ) {
			
			int manager_idx = Integer.parseInt( idx );
			
			StoreDAO dao = StoreDAO.getInstance();
			
			StoreVO vo = new StoreVO();
			vo.setManager_idx( manager_idx );
			vo.setName( name );
			vo.setLocation( location );
			vo.setTel( tel );
			vo.setOpenclose( openclose );
			vo.setNotice( notice );
			vo.setLat( lat );
			vo.setLng( lng );
			
			int result = dao.updateStore( vo );
			String resultStr = "fail";
			
			if( result >= 1 ) {
				resultStr = "success";
			}
			
			response.getWriter().println(String.format("[{'result':'%s'}]", resultStr));
			
		}
		
	}

}
