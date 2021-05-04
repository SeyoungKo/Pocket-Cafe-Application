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
 * Servlet implementation class SearchKeywordAction
 */
@WebServlet("/SearchKeyword.do")
public class SearchKeywordAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		
		String result = "";
	
		String name = request.getParameter("name");
		System.out.println(name);
			
		
		SearchDAO dao = SearchDAO.getInstance();
		
		SearchVO vo = new SearchVO();
		vo.setName(name);
		List<SearchVO> list = dao.selectList(vo);
		
		if(list ==null) {
			System.out.println("null");
			return ;
		}
		
		JSONArray jsonArray = new JSONArray();
	
		for(int i=0; i<list.size(); i++) {
			
			JSONObject search = new JSONObject();
			response.setCharacterEncoding("UTF-8");
			
			search.put("idx", String.format("%d", list.get(i).getIdx()));
			search.put("manager_idx", String.format("%d",list.get(i).getManager_idx()));
			search.put("name", String.format("%s",list.get(i).getName()));
			search.put("openclose", String.format("%s",list.get(i).getOpenclose()));
			search.put("tel", String.format("%s",list.get(i).getTel()));
			search.put("location", String.format("%s", list.get(i).getLocation()));
			search.put("notice", String.format("%s", list.get(i).getNotice()));
			search.put("photo1", String.format("%s", list.get(i).getPhoto1()));
			search.put("photo2", String.format("%s", list.get(i).getPhoto2()));
			
			jsonArray.add(search);
		}
		JSONObject jsonObject = new JSONObject();
		
		jsonObject.put("search", jsonArray);
		
		System.out.println(jsonObject.toString()); //�����
		
	
		response.getWriter().println(jsonObject.toString());
	}

}