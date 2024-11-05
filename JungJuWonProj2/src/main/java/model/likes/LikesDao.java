package model.likes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import jakarta.servlet.ServletContext;
import service.DaoService;

public class LikesDao implements DaoService<LikesDto>{

	private Connection conn;
	private PreparedStatement psmt;
	private ResultSet rs;
	
	public LikesDao(ServletContext context) {
		try {
			conn = ((DataSource)context.getAttribute("DATA-SOURCE")).getConnection();
		}
		catch (SQLException e) {e.printStackTrace();}
	}//////////

	@Override
	public void close() {
		try {
			if(conn!= null) conn.close();
			if(psmt!= null) psmt.close();
			if(rs!= null) rs.close();
		}
		catch (SQLException e) {e.printStackTrace();}
	}//////////

	@Override
	public List<LikesDto> findAll(Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LikesDto findById(String... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTotalRecordCount(Map<String, String> map) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(LikesDto dto) {
		int num=0;
		try {
			psmt = conn.prepareStatement("insert into likes values(seq_likes.nextval, ?, ?)");
			psmt.setString(1, dto.getBbsid());
			psmt.setString(2, dto.getUsername());
			num = psmt.executeUpdate();
		}
		catch (SQLException e) {e.printStackTrace();}
		return num;
	}

	@Override
	public int update(LikesDto dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(LikesDto dto) {
		int num=0;
		try {
			psmt = conn.prepareStatement("delete from likes where bbsid = ? and username = ?");
			psmt.setString(1, dto.getBbsid());
			psmt.setString(2, dto.getUsername());
			num = psmt.executeUpdate();
		}
		catch (SQLException e) {e.printStackTrace();}
		return num;
	}/////

	public int getCountByBbsid(String bbsid) {
		int num=-1;
		try {
			psmt = conn.prepareStatement("select count(*) from likes where bbsid=?");
			psmt.setString(1, bbsid);
			rs = psmt.executeQuery();
			rs.next();
			num = rs.getInt(1);
		}
		catch (SQLException e) {e.printStackTrace();}
		return num;
	}/////

	public boolean findByBbsidAndUsername(String bbsid, String username) {
		boolean isLike = false;
		try {
			psmt = conn.prepareStatement("select count(*) from likes where bbsid=? and username=?");
			psmt.setString(1, bbsid);
			psmt.setString(2, username);
			rs = psmt.executeQuery();
			rs.next();
			if(rs.getInt(1)==1) isLike=true;
		}
		catch (SQLException e) {e.printStackTrace();}
		return isLike;
	}/////

	public void deleteAllByBbsid(String bbsid) {
		try {
			psmt = conn.prepareStatement("delete from likes where bbsid=?");
			psmt.setString(1, bbsid);
			psmt.executeUpdate();
		}
		catch (SQLException e) {e.printStackTrace();}
	}/////

}
