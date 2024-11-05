package model.comments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sql.DataSource;

import jakarta.servlet.ServletContext;
import service.DaoService;

public class CommentsDao implements DaoService<CommentsDto>{

	private Connection conn;
	private PreparedStatement psmt;
	private ResultSet rs;
	
	public CommentsDao(ServletContext context) {
		try {
			conn = ((DataSource)context.getAttribute("DATA-SOURCE")).getConnection();
		}
		catch (SQLException e) {e.printStackTrace();}
	}////////////
	
	@Override
	public void close() {
		try {
			if(conn!= null) conn.close();
			if(psmt!= null) psmt.close();
			if(rs!= null) rs.close();
		}
		catch (SQLException e) {e.printStackTrace();}
	}

	@Override
	public List<CommentsDto> findAll(Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommentsDto findById(String... params) {
		CommentsDto dto = null;
		try {
			psmt = conn.prepareStatement("select * from comments where username = ?");
			psmt.setString(1, params[0]);
			rs = psmt.executeQuery();
			rs.next();
			
		}
		catch (SQLException e) {e.printStackTrace();}
		return dto;
	}

	@Override
	public int getTotalRecordCount(Map<String, String> map) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(CommentsDto dto) {
		int commentid=0;
		try {
			psmt = conn.prepareStatement("insert into comments values(seq_comments.nextval,?,?,?,default)");
			psmt.setLong(1, dto.getBbsid());
			psmt.setString(2, dto.getUsername());
			psmt.setString(3, dto.getContent());
			psmt.executeUpdate();
			
			psmt = conn.prepareStatement("select commentid from comments where commentid = (select max(commentid) from comments)");
			rs = psmt.executeQuery();
			rs.next();
			commentid = rs.getInt(1);
		}
		catch (SQLException e) {e.printStackTrace();}
		return commentid;
	}

	@Override
	public int update(CommentsDto dto) {
		int num=0;
		try {
			psmt = conn.prepareStatement("update comments set content = ? where commentid = ?");
			psmt.setString(1, dto.getContent());
			psmt.setLong(2, dto.getCommentid());
			num = psmt.executeUpdate();
		}
		catch (SQLException e) {e.printStackTrace();}
		return num;
	}

	@Override
	public int delete(CommentsDto dto) {
		int num=0;
		try {
			psmt = conn.prepareStatement("delete from comments where commentid = ?");
			psmt.setLong(1, dto.getCommentid());
			num = psmt.executeUpdate();
		}
		catch (SQLException e) {e.printStackTrace();}
		return num;
	}
	
	public List<CommentsDto> findAllByUsername(String username) {
		List<CommentsDto> commentsByUsername = new Vector<>();
		try {
			String sql = "select c.*, b.title, b.boardname, b.boardid "
					+ "from comments c join (select bbs.*, boards.boardname from bbs join boards on bbs.boardid=boards.boardid) b on c.bbsid=b.bbsid "
					+ "where c.username=? order by commentid DESC";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, username);
			rs = psmt.executeQuery();
			while(rs.next()) {
				CommentsDto dto = CommentsDto.builder().commentid(rs.getLong(1))
													.bbsid(rs.getLong(2))
													.content(rs.getString(4))
													.postdate(rs.getTimestamp(5))
													.bbstitle(rs.getString(6))
													.boardname(rs.getString(7))
													.boardid(rs.getString(8))
													.build();
				commentsByUsername.add(dto);
			}
		}
		catch (SQLException e) {e.printStackTrace();}
		return commentsByUsername;
	}///////////

	public List<CommentsDto> findByBbsid(long bbsid) {
		List<CommentsDto> commentsByBbsid= new Vector<>();
		try {
			psmt = conn.prepareStatement("select comments.*, members.nickname from comments join members on comments.username = members.username where bbsid = ? order by commentid DESC");
			psmt.setLong(1, bbsid);
			rs = psmt.executeQuery();
			while(rs.next()) {
				CommentsDto dto = CommentsDto.builder().commentid(rs.getLong(1))
													.bbsid(rs.getLong(2))
													.username(rs.getString(3))
													.content(rs.getString(4))
													.postdate(rs.getTimestamp(5))
													.nickname(rs.getString(6))
													.build();
				commentsByBbsid.add(dto);
			}
		}
		catch (SQLException e) {e.printStackTrace();}
		return commentsByBbsid;
	}

	public void deleteAllByBbsid(String bbsid) {
		try {
			psmt = conn.prepareStatement("delete from comments where bbsid=?");
			psmt.setString(1, bbsid);
			psmt.executeUpdate();
		}
		catch (SQLException e) {e.printStackTrace();}
	}///////

	

	
}	
