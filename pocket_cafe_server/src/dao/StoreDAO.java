package dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import service.MyBatisConnector;
import vo.StoreVO;

public class StoreDAO {
	
	static StoreDAO single = null;
	SqlSessionFactory factory = null;
	
	public static StoreDAO getInstance() {
		
		if( single == null ) {
			single = new StoreDAO();
		}
		
		return single;
		
	}
	
	public StoreDAO() {
		factory = MyBatisConnector.getInstance().getSqlSessionFactory();
	}
	
	public int insertStore( StoreVO vo ) {
		
		SqlSession sqlSession = factory.openSession( true );
		int result = sqlSession.insert("store.insert", vo);
		
		sqlSession.close();
		
		return result;
		
	}
	
	public StoreVO selectOneStore( int manager_idx ) {
		
		SqlSession sqlSession = factory.openSession();
		StoreVO vo = sqlSession.selectOne("store.select_one", manager_idx);
		
		sqlSession.close();
		
		return vo;
		
	}
	
	public StoreVO selectOneStore_idx( int idx ) {
		
		SqlSession sqlSession = factory.openSession();
		StoreVO vo = sqlSession.selectOne("store.select_one_idx", idx);
		
		sqlSession.close();
		
		return vo;
		
	}
	
	public int updateStore( StoreVO vo ) {
		
		SqlSession sqlSession = factory.openSession( true );
		int result = sqlSession.update("store.update", vo);
		
		sqlSession.close();
		
		return result;
		
	}
	
	public int updatePhoto1( StoreVO vo ) {
		
		SqlSession sqlSession = factory.openSession( true );
		int result = sqlSession.update("store.update_photo1", vo);
		
		sqlSession.close();
		
		return result;
		
	}
	
	public int updatePhoto2( StoreVO vo ) {
		
		SqlSession sqlSession = factory.openSession( true );
		int result = sqlSession.update("store.update_photo2", vo);
		
		sqlSession.close();
		
		return result;
		
	}
	
	
}
