package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.JWTokens;
import model.bbs.BbsDao;
import model.bbs.BbsDto;
import model.comments.CommentsDao;
import model.comments.CommentsDto;
import model.likes.LikesDao;
import model.members.MembersDao;
import model.members.MembersDto;

@WebServlet("/bbs/view")
public class ViewController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("뷰 컨트롤러 진입");
		String boardid = req.getParameter("boardid");
		String bbsid = req.getParameter("bbsid");
		String searchWord = req.getParameter("searchWord");
		String searchColumn = req.getParameter("searchColumn");
		System.out.println("boardid: "+boardid);
		System.out.println("bbsid: "+bbsid);
		
		String list = req.getParameter("list"); //수정 컨트롤러에서 왔는지 검사
		
		//게시글 1개
		BbsDao bDao = new BbsDao(getServletContext());
		BbsDto bDto = null;
		if(list!=null) bDto = bDao.findById(bbsid,"hit");
		else bDto = bDao.findById(bbsid);
		
		bDto.setContent(bDto.getContent().replace("\r\n", "<br/>"));
		req.setAttribute("dto", bDto);
		
		//게시판 목록
		List<BbsDto> boards = bDao.findBoardsAll();
		req.setAttribute("boards", boards);
		
		//이전 다음글
		Map<String, String> map = new HashMap<>();
		map.put("bbsid", bbsid);
		map.put("boardid", boardid);
		if(searchWord !=null && searchWord.length() > 0) {
			map.put("searchWord", searchWord);
			map.put("searchColumn", searchColumn);
		}
		Map<String, BbsDto> prevnext = bDao.prevNext(map);
		req.setAttribute("prevNext", prevnext);
		bDao.close();
		
		//댓글 목록
		CommentsDao cDao = new CommentsDao(getServletContext());
		List<CommentsDto> cList = cDao.findByBbsid(bDto.getBbsid());
		req.setAttribute("cList", cList);
		cDao.close();
		
		//좋아요 여부 및 좋아요 개수
		LikesDao lDao = new LikesDao(getServletContext());
		String username = (String)JWTokens.getTokenPayloads(JWTokens.getTokenInCookie(req, req.getServletContext().getInitParameter("TOKEN-NAME"))).get("sub");
		int likeCount = lDao.getCountByBbsid(bbsid);
		req.setAttribute("likecount", likeCount);
		boolean isLike = lDao.findByBbsidAndUsername(bbsid, username);
		req.setAttribute("islike", isLike);
		lDao.close();
		
		//페이징 관련 파라미터 보내기
		req.setAttribute("nowPage", req.getParameter("nowPage"));
		
		//검색 관련 파라미터 보내기
		req.setAttribute("searchWord", searchWord);
		req.setAttribute("searchColumn", searchColumn);
		
		req.getRequestDispatcher("/WEB-INF/bbs/View.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
