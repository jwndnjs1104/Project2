package model.bbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sql.DataSource;

import jakarta.servlet.ServletContext;
import model.PagingUtil;
import service.DaoService;

public class BbsDao implements DaoService<BbsDto>{
	
	private Connection conn;
	private PreparedStatement psmt;
	private ResultSet rs;
	
	public BbsDao(ServletContext context) {
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
	public List<BbsDto> findAll(Map<String, String> map) {
		List<BbsDto> listAll = new Vector<>();
		try {
			//psmt = conn.prepareStatement("select * from (select d.*,rank() over (order by bbsid DESC) as rank from bbs b) where rank between ? and ?");
			//psmt.setString(1, map.get(PagingUtil.START));
			//psmt.setString(2, map.get(PagingUtil.END));
			
			//String sql = "select * from (select d.*, rank() over (order by id DESC) as idRank from dataroom d";
			//sql+=" ) where idRank between ? and ? ";
			String sql = "select b.*, c.commentcount, bs.boardname, m.nickname, likecount "
					+ "from bbs b left outer join (select bbsid,count(*) as commentcount from comments group by bbsid) c on b.bbsid = c.bbsid "
					+ "join boards bs on b.boardid=bs.boardid "
					+ "join members m on b.username=m.username "
					+ "left outer join (select bbsid, count(*) as likecount from likes group by bbsid) l on b.bbsid=l.bbsid "
					+ "where b.boardid not in (1,2) "
					+ "order by b.bbsid DESC";
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			while(rs.next()) {
				BbsDto bbsDto = BbsDto.builder().bbsid(rs.getLong(1))
												.boardid(rs.getLong(2))
												.username(rs.getString(3))
												.title(rs.getString(4))
												.content(rs.getString(5))
												.postdate(rs.getTimestamp(6))
												.hitcount(rs.getInt(7))
												.files(rs.getString(8))
												.commentcount(rs.getInt(9))
												.boardname(rs.getString(10))
												.nickname(rs.getString(11))
												.likecount(rs.getString(12))
												.build();
				listAll.add(bbsDto);
			}
		}
		catch (SQLException e) {e.printStackTrace();}
		return listAll;
	}////////////

	@Override
	public BbsDto findById(String... params) {
		BbsDto bbsDto = null;
		try {
			if(params.length > 1 && params[1].contains("hit")) {
				psmt=conn.prepareStatement("update bbs set hitcount=hitcount+1 where bbsid = ?");
				psmt.setString(1, params[0]);
				psmt.executeUpdate();
			}
			
			psmt=conn.prepareStatement("select bbs.*, boards.boardname, members.nickname, commentcount "
					+ "from bbs join boards on bbs.boardid=boards.boardid "
					+ "join members on bbs.username=members.username "
					+ "left outer join (select bbsid, count(*) as commentcount from comments group by bbsid) c on bbs.bbsid=c.bbsid "
					+ "where bbs.bbsid = ?");
			psmt.setString(1, params[0]);
			rs=psmt.executeQuery();
			rs.next();
			bbsDto = BbsDto.builder().bbsid(rs.getLong(1))
									.boardid(rs.getLong(2))
									.username(rs.getString(3))
									.title(rs.getString(4))
									.content(rs.getString(5))
									.postdate(rs.getTimestamp(6))
									.hitcount(rs.getInt(7))
									.files(rs.getString(8))
									.boardname(rs.getString(9))
									.nickname(rs.getString(10))
									.commentcount(rs.getInt(11))
									.build();
		}
		catch (SQLException e) {e.printStackTrace();}
		return bbsDto;
	}

	@Override
	public int getTotalRecordCount(Map<String, String> map) {
		int totalRecordCount = 0;
		String sql = "select count(*) from bbs join members on bbs.username=members.username";
		
		if(map!=null && map.containsKey("boardid") && map.get("boardid").equals("0")) {
			sql+= " where boardid not in (1, 2) ";
		}
		else if(map!=null && map.containsKey("boardid")){
			sql+= " where boardid = "+map.get("boardid");
		}
		
		if(map!=null && map.containsKey("searchColumn")) {
			sql+=" and "+map.get("searchColumn")+ " like '%"+map.get("searchWord")+"%'";
		}
		
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			rs.next();
			totalRecordCount = rs.getInt(1);
		}
		catch (SQLException e) {e.printStackTrace();}
		return totalRecordCount;
	}

	@Override
	public int insert(BbsDto dto) {
		int num=0;
		try {
			psmt = conn.prepareStatement("insert into bbs values(seq_bbs.nextval,?,?,?,?,default,default,?)");
			psmt.setLong(1, dto.getBoardid());
			psmt.setString(2, dto.getUsername());
			psmt.setString(3, dto.getTitle());
			psmt.setString(4, dto.getContent());
			psmt.setString(5, dto.getFiles());
			num = psmt.executeUpdate();
		}
		catch (SQLException e) {e.printStackTrace();}
		return num;
	}/////////////

	@Override
	public int update(BbsDto dto) {
		int num=0;
		try {
			psmt = conn.prepareStatement("update bbs set title=?, content=?, files=? where bbsid=?");
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContent());
			psmt.setString(3, dto.getFiles());
			psmt.setLong(4, dto.getBbsid());
			num = psmt.executeUpdate();
		}
		catch (SQLException e) {e.printStackTrace();}
		return num;
	}////////////

	@Override
	public int delete(BbsDto dto) {
		int num=0;
		try {
			psmt = conn.prepareStatement("delete from bbs where bbsid=?");
			psmt.setLong(1, dto.getBbsid());
			num = psmt.executeUpdate();
		}
		catch (SQLException e) {e.printStackTrace();}
		return num;
	}

	public List<BbsDto> findBoardsAll() {
		List<BbsDto> boards = new Vector<>();
		try {
			psmt = conn.prepareStatement("select * from boards order by boardid");
			rs = psmt.executeQuery();
			while(rs.next()) {
				BbsDto dto = BbsDto.builder().boardid(rs.getLong(1)).boardname(rs.getString(2)).build();
				boards.add(dto);
			}
			return boards;
		}
		catch (SQLException e) {e.printStackTrace();}
		return boards;
	}//////////
	
	//키는 board, value는 bbs list
	public Map<String, List<BbsDto>> findAllByBoards(Map<Long, String> boards) {
		
		Map<String, List<BbsDto>> bbsAll = new HashMap<>();
		
		try {
			for(long boardid:boards.keySet()){ //게시판 여러개 
				List<BbsDto> listAll = new Vector<>();
				String board = boards.get(boardid);
				psmt = conn.prepareStatement("select * from bbs where boardid = ? order by bbsid DESC");
				psmt.setLong(1, boardid);
				rs = psmt.executeQuery();
				while(rs.next()) { //게시판 1개에 대한 모든 게시물
					BbsDto bbsDto = BbsDto.builder().bbsid(rs.getLong(1))
													.boardid(rs.getLong(2))
													.username(rs.getString(3))
													.title(rs.getString(4))
													.content(rs.getString(5))
													.postdate(rs.getTimestamp(6))
													.hitcount(rs.getInt(7))
													.files(rs.getString(8))
													.build();
					listAll.add(bbsDto);
				}/////while
				bbsAll.put(board, listAll);
			}/////for
		}////try
		catch (SQLException e) {e.printStackTrace();}
		return bbsAll;
	}/////////
	
	//게시판 하나에 대해 전체글 가져오기
	public List<BbsDto> findAllByBoard(Map<String, String> map) {
		List<BbsDto> listAll = new Vector<>();
		try {
			String sql = "select * from "
						+ "(select b.*, c.commentcount, bs.boardname, m.nickname, likecount, rank() over (order by b.bbsid DESC) as idRank "
						+ "from bbs b left outer join (select bbsid,count(*) as commentcount from comments group by bbsid) c on b.bbsid = c.bbsid "
						+ "join boards bs on b.boardid=bs.boardid "
						+ "join members m on b.username=m.username "
						+ "left outer join (select bbsid, count(*) as likecount from likes group by bbsid) l on b.bbsid=l.bbsid "
						+ "where b.boardid = ? ";
			if(map.containsKey("searchColumn")) {
				
				sql += " and "+(map.get("searchColumn").equals("nickname")?"m.":"b.")+map.get("searchColumn")+ " like '%"+map.get("searchWord")+"%'";
			}
			sql += "order by b.bbsid DESC) where idRank between ? and ? ";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, map.get("boardid"));
			psmt.setString(2, map.get(PagingUtil.START));
			psmt.setString(3, map.get(PagingUtil.END));
			rs = psmt.executeQuery();
			
			while(rs.next()) { //게시판 1개에 대한 모든 게시물
				BbsDto bbsDto = BbsDto.builder().bbsid(rs.getLong(1))
												.boardid(rs.getLong(2))
												.username(rs.getString(3))
												.title(rs.getString(4))
												.content(rs.getString(5))
												.postdate(rs.getTimestamp(6))
												.hitcount(rs.getInt(7))
												.files(rs.getString(8))
												.commentcount(rs.getInt(9))
												.boardname(rs.getString(10))
												.nickname(rs.getString(11))
												.likecount(rs.getString(12))
												.build();
				listAll.add(bbsDto);
			}/////while
		}////try
		catch (SQLException e) {e.printStackTrace();}
		return listAll;
	}/////////
	
	
	//공지글 리스트 받아오는 메소드
	public List<BbsDto> findNotice() {
		List<BbsDto> listAll = new Vector<>(); 
		try {
			String sql = "select b.*, commentcount, likecount "
						+ "from bbs b left outer join (select bbsid,count(*) as commentcount from comments group by bbsid) c on b.bbsid = c.bbsid "
						+ "left outer join (select bbsid, count(*) as likecount from likes group by bbsid) l on b.bbsid=l.bbsid "
						+ "where b.boardid = 2 order by b.bbsid DESC";
			
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			while(rs.next()) {
				BbsDto bbsDto = BbsDto.builder().bbsid(rs.getLong(1))
												.boardid(rs.getLong(2))
												.username(rs.getString(3))
												.title(rs.getString(4))
												.content(rs.getString(5))
												.postdate(rs.getTimestamp(6))
												.hitcount(rs.getInt(7))
												.files(rs.getString(8))
												.commentcount(rs.getInt(9))
												.likecount(rs.getString(10))
												.build();
				listAll.add(bbsDto);
			}
		}
		catch (SQLException e) {e.printStackTrace();}
		return listAll;
	}////////////
	
	//회원이 작성한 글 가져오기
	public List<BbsDto> findAllByUsername(String username){
		List<BbsDto> bbsesByUsername = new Vector<>();
		try {
			String sql = "select b.*, c.commentcount, bs.boardname, m.nickname, likecount "
					+ "from bbs b left outer join (select bbsid,count(*) as commentcount from comments group by bbsid) c on b.bbsid = c.bbsid "
					+ "join boards bs on b.boardid=bs.boardid "
					+ "join members m on b.username=m.username "
					+ "left outer join (select bbsid, count(*) as likecount from likes group by bbsid) l on b.bbsid=l.bbsid "
					+ "where b.boardid not in (1,2) and b.username = ? "
					+ "order by b.bbsid DESC";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, username);
			rs=psmt.executeQuery();
			while(rs.next()) {
				BbsDto bbsDto = BbsDto.builder().bbsid(rs.getLong(1))
												.boardid(rs.getLong(2))
												.username(rs.getString(3))
												.title(rs.getString(4))
												.content(rs.getString(5))
												.postdate(rs.getTimestamp(6))
												.hitcount(rs.getInt(7))
												.files(rs.getString(8))
												.commentcount(rs.getInt(9))
												.boardname(rs.getString(10))
												.nickname(rs.getString(11))
												.likecount(rs.getString(12))
												.build();
				bbsesByUsername.add(bbsDto);
			}
		}
		catch (SQLException e) {e.printStackTrace();}
		return bbsesByUsername;
	}/////

	public Map<String, BbsDto> prevNext(Map<String, String> map) {
		Map<String, BbsDto> prevNext = new HashMap<>(); 
		try {
			//이전글
			String sql = "SELECT b.bbsid, boardid, title, postdate, files, nickname, commentcount "
					+ "FROM bbs b JOIN members m ON b.username = m.username "
					+ "LEFT OUTER JOIN (select bbsid, count(*) commentcount from comments group by bbsid) c ON b.bbsid=c.bbsid "
					+ "WHERE b.bbsid = ("; 
			if(map.get("searchWord") != null) {
				sql += "SELECT MAX(bbsid) FROM bbs b join members m on b.username = m.username WHERE bbsid < ?  AND boardid = ?)"
						+ " and " + (map.get("searchColumn").equals("nickname") ? "m." : "b.") + map.get("searchColumn") + " like '%"+map.get("searchWord")+"%'";
			}
			else sql += "SELECT MAX(bbsid) FROM bbs WHERE bbsid < ?  AND boardid = ?)";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, map.get("bbsid"));
			psmt.setString(2, map.get("boardid"));
			rs = psmt.executeQuery();
			if(rs.next()) {
				System.out.println("담글, 이전글 있음");
				prevNext.put("prev",BbsDto.builder().bbsid(rs.getLong(1))
												.boardid(rs.getLong(2))
												.title(rs.getString(3))
												.postdate(rs.getTimestamp(4))
												.files(rs.getString(5))
												.nickname(rs.getString(6))
												.commentcount(rs.getInt(7))
												.build());
			}
			
			//다음글
			sql = "select b.bbsid, boardid, title, postdate, files, nickname, commentcount "
					+ "from bbs b join members m on b.username = m.username "
					+ "LEFT OUTER join (select bbsid, count(*) as commentcount from comments group by bbsid) c on b.bbsid=c.bbsid "
					+ "where b.bbsid = (";
			if(map.get("searchWord") != null) {
				sql += "SELECT MIN(bbsid) FROM bbs b join members m on b.username = m.username WHERE bbsid > ? "
						+ " and " + (map.get("searchColumn").equals("nickname") ? "m." : "b.") + map.get("searchColumn") + " like '%"+map.get("searchWord")+"%'";
			}
			else sql += "SELECT MIN(bbsid) FROM bbs WHERE bbsid > ?";
				sql += ") AND boardid = ? ORDER BY b.bbsid DESC";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, map.get("bbsid"));
			psmt.setString(2, map.get("boardid"));
			rs = psmt.executeQuery();
			if(rs.next()) {
				prevNext.put("next",BbsDto.builder().bbsid(rs.getLong(1))
												.boardid(rs.getLong(2))
												.title(rs.getString(3))
												.postdate(rs.getTimestamp(4))
												.files(rs.getString(5))
												.nickname(rs.getString(6))
												.commentcount(rs.getInt(7))
												.build());
			}
		}
		catch (SQLException e) {e.printStackTrace();}
		return prevNext;
	}

	public int createBoard(String boardname) {
		int num=0;
		try {
			psmt = conn.prepareStatement("insert into boards values(seq_boards.nextval, ?)");
			psmt.setString(1, boardname);
			psmt.executeUpdate();
			
			psmt = conn.prepareStatement("select max(boardid) from boards");
			rs = psmt.executeQuery();
			rs.next();
			num = rs.getInt(1);
		}
		catch (SQLException e) {e.printStackTrace();}
		return num;
	}
	
}
