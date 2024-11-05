package model.members;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sql.DataSource;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;
import service.DaoService;

public class MembersDao implements DaoService<MembersDto>{

	private Connection conn;
	private PreparedStatement psmt;
	private ResultSet rs;
	
	public MembersDao(ServletContext context) {
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
	}////////////

	public boolean isMember(MembersDto dto) {
		try {
			psmt = conn.prepareStatement("select count(*) from members where username = ? and password = ?");
			psmt.setString(1, dto.getUsername());
			psmt.setString(2, dto.getPassword());
			rs = psmt.executeQuery();
			rs.next();
			if(rs.getInt(1)==1) return true;
		}
		catch (SQLException e) {e.printStackTrace();}
		return false;
	}
	
	
	@Override
	public List<MembersDto> findAll(Map<String, String> map) {
		List<MembersDto> list = new Vector<>();
		try {
			psmt = conn.prepareStatement("select * from members where username not in ('admin')");
			rs = psmt.executeQuery();
			while(rs.next()) {
				MembersDto dto = MembersDto.builder().username(rs.getString(1))
													.name(rs.getString(3))
													.nickname(rs.getString(4))
													.phone(rs.getString(5))
													.email(rs.getString(6))
													.regidate(rs.getTimestamp(7)).build();
				list.add(dto);
			}
			
		} catch (SQLException e) {e.printStackTrace();}
		return list;
	}

	@Override
	public MembersDto findById(String... params) {
		MembersDto dto = null;
		try {
			psmt = conn.prepareStatement("select * from members where username = ?");
			psmt.setString(1, params[0]);
			rs= psmt.executeQuery();
			while(rs.next()) {
				dto = MembersDto.builder().username(rs.getString(1))
										.name(rs.getString(3))
										.nickname(rs.getString(4))
										.phone(rs.getString(5))
										.email(rs.getString(6))
										.regidate(rs.getTimestamp(7)).build();
			}
		}
		catch (SQLException e) {e.printStackTrace();}
		return dto;
	}

	@Override
	public int getTotalRecordCount(Map<String, String> map) {

		return 0;
	}

	@Override
	public int insert(MembersDto dto) {
		int num = 0;
		try {
			psmt = conn.prepareStatement("insert into members values(?,?,?,?,?,?,default)");
			psmt.setString(1, dto.getUsername());
			psmt.setString(2, dto.getPassword());
			psmt.setString(3, dto.getName());
			psmt.setString(4, dto.getNickname());
			psmt.setString(5, dto.getPhone());
			psmt.setString(6, dto.getEmail());
			num = psmt.executeUpdate();
		}
		catch (SQLException e) {e.printStackTrace();}
		return num;
	}
	
	

	@Override
	public int update(MembersDto dto) {
		int num = 0;
		try {
			psmt = conn.prepareStatement("update members set password = ?, nickname = ?, phone = ?, email = ? where username = ?");
			psmt.setString(1, dto.getPassword());
			psmt.setString(2, dto.getNickname());
			psmt.setString(3, dto.getPhone());
			psmt.setString(4, dto.getEmail());
			psmt.setString(5, dto.getUsername());
			num=psmt.executeUpdate();
		}
		catch (SQLException e) {e.printStackTrace();}
		return num;
	}

	@Override
	public int delete(MembersDto dto) {
		int num=0;
		try {
			psmt = conn.prepareStatement("delete from members where username = ?");
			psmt.setString(1, dto.getUsername());
			num=psmt.executeUpdate();
		} catch (SQLException e) {e.printStackTrace();}
		return num;
	}

	public boolean existByUsername(String username) {
		boolean exits;
		try {
			psmt = conn.prepareStatement("select count(*) from members where username = ?");
			psmt.setString(1, username);
			rs = psmt.executeQuery();
			rs.next();
			if(rs.getInt(1)==1) return true; 
		}
		catch (SQLException e) {e.printStackTrace();}
		return false;
	}

	public boolean existByNickname(String nickname) {
		boolean exits;
		try {
			psmt = conn.prepareStatement("select count(*) from members where nickname = ?");
			psmt.setString(1, nickname);
			rs = psmt.executeQuery();
			rs.next();
			if(rs.getInt(1)==1) return true; 
		}
		catch (SQLException e) {e.printStackTrace();}
		return false;
	}

	
	
}
