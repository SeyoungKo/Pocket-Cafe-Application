package action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import dao.SearchDAO;
import vo.SearchVO;

/**
 * Servlet implementation class SearchGPSAction
 */
@WebServlet("/SearchGPSAction.do")
public class SearchGPSAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		
		String result = "";
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lng = Double.parseDouble(request.getParameter("lng"));
//		
	/*	double lat =  37.479775;
		double lng = 126.847180;
		*/
		SearchVO vo = new SearchVO();
		vo.setLat(lat);
		vo.setLng(lng);
		
		SearchDAO dao = SearchDAO.getInstance();
		List<SearchVO> list = dao.gps_selectList(vo);
		
		JSONArray jsonArray = new JSONArray();
		
		for(int i=0; i<list.size(); i++) {
			JSONObject gpsSearch = new JSONObject();
			response.setCharacterEncoding("UTF-8");
			
			gpsSearch.put("idx", String.format("%d", list.get(i).getIdx()));
			gpsSearch.put("manager_idx", String.format("%d", list.get(i).getManager_idx()));
			gpsSearch.put("name", String.format("%s", list.get(i).getName()));
			gpsSearch.put("location", String.format("%s", list.get(i).getLocation()));
			gpsSearch.put("openclose", String.format("%s", list.get(i).getOpenclose()));
			gpsSearch.put("tel", String.format("%s", list.get(i).getTel()));
			gpsSearch.put("notice", String.format("%s", list.get(i).getNotice()));
			gpsSearch.put("distance", String.format("%s", list.get(i).getDistance()));
			
			jsonArray.add(gpsSearch);
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("gpsSearch", jsonArray);
		
		System.out.println(jsonObject.toString());
			
		response.getWriter().println(jsonObject.toString());
	}

}
