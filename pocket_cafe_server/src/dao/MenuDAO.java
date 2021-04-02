package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import service.MyBatisConnector;
import vo.MenuVO;

public class MenuDAO {
	
	static MenuDAO single = null;
	SqlSessionFactory factory = null;
	
	public static MenuDAO getInstance() {
		
		if( single == null ) {
			single = new MenuDAO();
		}
		
		return single;
	}
	
	public MenuDAO() {
		factory = MyBatisConnector.getInstance().getSqlSessionFactory();
	}
	
	public List<MenuVO> select_list_storeidx( int store_idx ){
		
		SqlSession sqlSession = factory.openSession();
		List<MenuVO> list = sqlSession.selectList("menu.menu_list", store_idx);
		
		sqlSession.close();
		
		return list;
		
	}
	
	public MenuVO select_one_idx( int idx ) {
		
		SqlSession sqlSession = factory.openSession();
		MenuVO vo = sqlSession.selectOne("menu.menu_one", idx);
		
		sqlSession.close();
		
		return vo;
		
	}
	
	public int insert_menu( MenuVO vo ) {
		
		SqlSession sqlSession = factory.openSession(true);
		int result = sqlSession.insert("menu.insert", vo);
		
		sqlSession.close();
		
		return result;
		
	}
	
	public int delete_menu( int idx ) {
		
		SqlSession sqlSession = factory.openSession( true );
		int result = sqlSession.delete("menu.delete", idx);
		
		sqlSession.close();
		
		return result;
		
	}
	
}
