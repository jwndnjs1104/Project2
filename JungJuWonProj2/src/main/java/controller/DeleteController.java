package controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BbsUtils;
import model.bbs.BbsDao;
import model.bbs.BbsDto;
import model.comments.CommentsDao;
import model.likes.LikesDao;

@WebServlet("/bbs/delete")
public class DeleteController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String boardid = req.getParameter("boardid");
		String bbsid = req.getParameter("bbsid");
		BbsDto dto = BbsDto.builder().bbsid(Long.parseLong(bbsid)).build();
		
		//검색어 전달하기
		String searchWord = req.getParameter("searchWord");
		String searchColumn = req.getParameter("searchColumn");
		
		
		/*
		//댓글 삭제
		CommentsDao cdao = new CommentsDao(getServletContext());
		cdao.deleteAllByBbsid(bbsid);
		cdao.close();
		
		//좋아요 삭제
		LikesDao ldao = new LikesDao(getServletContext());
		ldao.deleteAllByBbsid(bbsid);
		ldao.close();
		*/
		
		//게시글 삭제
		BbsDao dao = new BbsDao(getServletContext());
		int ok = dao.delete(dto);
		dao.close();
		if(ok==1) {
			String url = req.getContextPath()+"/bbs/list?boardid="+boardid+"&nowPage="+req.getParameter("nowPage");
			if(searchWord != null) {
				String encodedSearchWord = URLEncoder.encode(searchWord, StandardCharsets.UTF_8);
				url+="&searchColumn="+searchColumn+"&searchWord="+encodedSearchWord;
			}
			resp.sendRedirect(url);
			return;
		}
		else {
			BbsUtils.historyBack(resp, "게시글 삭제 실패");
			return;
		}
	}
}


