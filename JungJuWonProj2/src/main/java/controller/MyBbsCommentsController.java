package controller;

import java.io.IOException;
import java.util.List;


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
import model.members.MembersDao;
import model.members.MembersDto;

@WebServlet("/bbs/mypagebbscomments")
public class MyBbsCommentsController extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String username = (String)JWTokens.getTokenPayloads(JWTokens.getTokenInCookie(req, req.getServletContext().getInitParameter("TOKEN-NAME"))).get("sub");
		
		BbsDao bbsDao = new BbsDao(getServletContext());
		List<BbsDto> bbsAllByUsername = bbsDao.findAllByUsername(username);
		req.setAttribute("bbsAll", bbsAllByUsername); //@@@@@@@@
		bbsDao.close();
		
		CommentsDao commentsDao = new CommentsDao(getServletContext());
		List<CommentsDto> commentsAll = commentsDao.findAllByUsername(username);
		req.setAttribute("commentsAll", commentsAll);
		commentsDao.close();
		
		if(req.getParameter("mode")!= null) {
			req.getRequestDispatcher("/WEB-INF/bbs/MyPageComments.jsp").forward(req, resp);
			return;
		}
		
		req.getRequestDispatcher("/WEB-INF/bbs/MyPageBbs.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}