package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.JWTokens;
import model.members.MembersDao;
import model.members.MembersDto;

@WebServlet("/members/login")
public class LoginController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//resp.sendRedirect(req.getContextPath()+"/WEB-INF/bbs/Login.jsp");
		req.getRequestDispatcher("/WEB-INF/bbs/Login.jsp").forward(req, resp);
	}//////////
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//로그인한 사용자 판별 조건 추가해야됨
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		MembersDto dto = MembersDto.builder().username(username).password(password).build();
		MembersDao dao = new MembersDao(getServletContext());
		boolean isMember = dao.isMember(dto);
		dao.close();
		
		if(isMember) { //회원인 경우
			Map<String, Object> payloads = new HashMap<>();
			long expirationTime = 1000*60*60*3;
			String token = JWTokens.createToken(username, payloads, expirationTime);
			Cookie cookie = new Cookie(req.getServletContext().getInitParameter("TOKEN-NAME"),token);
			cookie.setPath(req.getContextPath());
			resp.addCookie(cookie);
			resp.sendRedirect(req.getContextPath()+"/home");
			return;
		}
		else { //회원이 아닌 경우
			req.setAttribute("errorMsg", "아이디 및 비밀번호가 일치하지 않습니다");
			req.getRequestDispatcher("/WEB-INF/bbs/Login.jsp").forward(req, resp);
		}
	}//////////
}
