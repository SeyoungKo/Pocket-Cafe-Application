package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import service.MyBatisConnector;
import vo.SearchVO;

public class SearchDAO {
	
	static SearchDAO single = null;
	SqlSessionFactory factory = null;
	
	public static SearchDAO getInstance() {
		
		if( single == null ) {
			single = new SearchDAO();
		}
		
		return single;
	
	}
	public SearchDAO() {
		factory = MyBatisConnector.getInstance().getSqlSessionFactory();
	
	}
	
	
	public List<SearchVO> selectList(SearchVO vo) {
		
		SqlSession sqlSession = factory.openSession();
		List<SearchVO> list = null;
		list = sqlSession.selectList("search.select_list",vo);
		if (list ==null) {
			System.out.println("오류");
		}
		sqlSession.close();
		return list;
	}
	
	public List<SearchVO> gps_selectList(SearchVO vo){
		
		SqlSession sqlSession = factory.openSession();
		List<SearchVO> list = null;
		list = sqlSession.selectList("search.gps_selectList",vo);
		
		sqlSession.close();
		return list;
	}
}
