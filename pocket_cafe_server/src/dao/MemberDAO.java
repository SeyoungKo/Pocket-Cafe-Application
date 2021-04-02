package dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import service.MyBatisConnector;
import vo.MemberVO;

public class MemberDAO {
	
	static MemberDAO single = null;
	SqlSessionFactory factory = null;
	
	public static MemberDAO getInstance() {
		
		if( single == null ) {
			single = new MemberDAO();
		}
		
		return single;
		
	}
	
	public MemberDAO() {
		
		factory = MyBatisConnector.getInstance().getSqlSessionFactory();
		
	}
	
	public MemberVO selectId( String id ) {
		
		SqlSession sqlSession = factory.openSession();
		MemberVO vo = sqlSession.selectOne("member.select_id", id);
		
		sqlSession.close();
		
		return vo;
		
	}
	
	public MemberVO selectIdx( int idx ) {
		
		SqlSession sqlSession = factory.openSession();
		MemberVO vo = sqlSession.selectOne("member.select_idx", idx);
		
		sqlSession.close();
		
		return vo;
		
	}
	
	public int insertGuest( MemberVO vo ) {
		
		SqlSession sqlSession = factory.openSession( true );
		int result = sqlSession.insert("member.insert_guest", vo);
		
		sqlSession.close();
		
		return result;
		
	}
	
	public int insertManager( MemberVO vo ) {
		
		SqlSession sqlSession = factory.openSession( true );
		int result = sqlSession.insert("member.insert_manager", vo);
		
		sqlSession.close();
		
		return result;
		
	}
	
	public MemberVO login( MemberVO vo ) {
		
		SqlSession sqlSession = factory.openSession();
		MemberVO login_vo = sqlSession.selectOne("member.login", vo);
		
		sqlSession.close();
		
		return login_vo;
		
	}
	
	public int changeInfo( MemberVO vo ) {
		
		SqlSession sqlSession = factory.openSession( true );
		int result = sqlSession.update("member.update_info", vo);
		
		sqlSession.close();
		
		return result;
		
	}
	
	public int changePwd( MemberVO vo ) {
		
		SqlSession sqlSession = factory.openSession( true );
		int result = sqlSession.update("member.update_pwd", vo);
		
		sqlSession.close();
		
		return result;
		
	}
	
}
