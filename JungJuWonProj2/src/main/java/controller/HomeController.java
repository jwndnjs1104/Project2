package controller;

import java.io.IOException;
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
import model.members.MembersDao;
import model.members.MembersDto;

@WebServlet("/home")
public class HomeController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BbsDao dao = new BbsDao(getServletContext());
		List<BbsDto> boards = dao.findBoardsAll();
		req.setAttribute("boards", boards); //@@@@@@@@@@ board map
		
		//공지 리스트
		List<BbsDto> noticeBbs = dao.findNotice();
		req.setAttribute("noticeBbs", noticeBbs);
		
		//전체글 리스트
		List<BbsDto> totalBbs = dao.findAll(null);
		req.setAttribute("totalBbs", totalBbs);
		
		//Map<String,List<BbsDto>> bbsAll = dao.findAllByBoards(boards);
		//req.setAttribute("bbsAll", bbsAll);
		
		//로그인 되어있을시 닉네임 표시용
		String token = JWTokens.getTokenInCookie(req, req.getServletContext().getInitParameter("TOKEN-NAME"));
		boolean isAuthenticated = JWTokens.verifyToken(token);
		if(isAuthenticated) {
			String username = JWTokens.getTokenPayloads(token).get("sub").toString();
			MembersDao membersDao = new MembersDao(req.getServletContext());
			MembersDto memberDto =membersDao.findById(username);
			membersDao.close();
			req.setAttribute("nickname", memberDto.getNickname());
		}
		
		dao.close();
		req.setAttribute("auth", isAuthenticated);
		req.getRequestDispatcher("/WEB-INF/bbs/Home.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
