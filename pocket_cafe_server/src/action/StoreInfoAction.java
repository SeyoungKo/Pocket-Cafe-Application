package action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.StoreDAO;
import vo.StoreVO;

@WebServlet("/store_info.do")
public class StoreInfoAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		int store_idx = -1;
		String manager_idx = request.getParameter("manager_idx");
		String store_idx_str = request.getParameter("store_idx");
		String store_name = "";
		String store_loc = "";
		String store_tel = "";
		String store_openclose = "";
		String store_photo1 = "";
		String store_photo2 = "";
		String store_notice = "";
		String store_lat = "";
		String store_lng = "";
		String result = "";
		
		if( manager_idx != null ) {
			
			int idx = Integer.parseInt( manager_idx );
			StoreDAO dao = StoreDAO.getInstance();
			
			StoreVO vo = dao.selectOneStore(idx);
			store_idx = vo.getIdx();
			store_name = vo.getName();
			store_loc = vo.getLocation();
			store_tel = vo.getTel();
			store_openclose = vo.getOpenclose();
			store_photo1 = vo.getPhoto1();
			store_photo2 = vo.getPhoto2();
			store_notice = vo.getNotice();
			store_lat = vo.getLat();
			store_lng = vo.getLng();
			
			result = String.format("[{'idx':'%d', 'name':'%s', 'location':'%s', 'tel':'%s', 'openclose':'%s', 'photo1':'%s', 'photo2':'%s', 'notice':'%s', 'lat':'%s', 'lng':'%s'}]", store_idx, store_name, store_loc, store_tel, store_openclose, store_photo1, store_photo2, store_notice, store_lat, store_lng);
			
			response.setCharacterEncoding("UTF-8"); 
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().println(result);
		}else if( store_idx_str != null ) {
			
			int store_idx_param = Integer.parseInt(store_idx_str);
			StoreDAO dao = StoreDAO.getInstance();
			
			StoreVO vo = dao.selectOneStore_idx(store_idx_param);
			store_idx = vo.getIdx();
			store_name = vo.getName();
			store_loc = vo.getLocation();
			store_tel = vo.getTel();
			store_openclose = vo.getOpenclose();
			store_photo1 = vo.getPhoto1();
			store_photo2 = vo.getPhoto2();
			store_notice = vo.getNotice();
			store_lat = vo.getLat();
			store_lng = vo.getLng();
			
			result = String.format("[{'idx':'%d', 'name':'%s', 'location':'%s', 'tel':'%s', 'openclose':'%s', 'photo1':'%s', 'photo2':'%s', 'notice':'%s', 'lat':'%s', 'lng':'%s'}]", store_idx, store_name, store_loc, store_tel, store_openclose, store_photo1, store_photo2, store_notice, store_lat, store_lng);
			
			response.setCharacterEncoding("UTF-8"); 
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().println(result);
			
		}
		
	}

}
