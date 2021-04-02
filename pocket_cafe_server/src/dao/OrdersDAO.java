package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import service.MyBatisConnector;
import vo.OrdersVO;

public class OrdersDAO {
	
	static OrdersDAO single = null;
	SqlSessionFactory factory = null;
	
	public static OrdersDAO getInstance() {
		
		if( single == null ) {
			single = new OrdersDAO();
		}
		
		return single;
	}
	
	public OrdersDAO() {
		factory = MyBatisConnector.getInstance().getSqlSessionFactory();
	}
	
	public int insert_order( OrdersVO vo ) {
		
		SqlSession sqlSession = factory.openSession(true);
		int result = sqlSession.insert("orders.insert", vo);
		
		sqlSession.close();
		
		return result;
		
	}
	
	public int OrderFinish(int idx) {
		
		SqlSession sqlSession = factory.openSession( true );
		int result = sqlSession.update("orders.orders_finish", idx);
		
		sqlSession.close();

		return result;
		
	}
	
	public List<OrdersVO> selectOrder(OrdersVO vo) {

		SqlSession sqlSession = factory.openSession();
		List<OrdersVO> list = sqlSession.selectList("orders.select_orders", vo);

		sqlSession.close();

		return list;

	}
	
	public List<OrdersVO> selectOrder_guest( int guest_idx ){
		
		SqlSession sqlSession = factory.openSession();
		List<OrdersVO> list = sqlSession.selectList("orders.select_orders_guest", guest_idx);

		sqlSession.close();

		return list;
		
	}
	
}
