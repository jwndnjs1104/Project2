package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import model.JWTokens;
import model.members.MembersDao;
import model.members.MembersDto;

import java.io.IOException;

@WebFilter(urlPatterns = {"/bbs/*"})
public class AuthenticationFilter implements Filter {
	@Override
	public void doFilter(jakarta.servlet.ServletRequest req, jakarta.servlet.ServletResponse resp,
			jakarta.servlet.FilterChain chain) throws IOException, jakarta.servlet.ServletException {
		
		HttpServletRequest request =(HttpServletRequest)req;
		String token = JWTokens.getTokenInCookie(request, request.getServletContext().getInitParameter("TOKEN-NAME"));
		
		boolean isAuthenticated = JWTokens.verifyToken(token);
		if(!isAuthenticated) {
			request.setAttribute("errorMsg", "게시판을 이용하시려면 로그인 해주세요");
			request.getRequestDispatcher("/WEB-INF/bbs/Login.jsp").forward(req, resp);
			return;
		}
		
		String username = JWTokens.getTokenPayloads(token).get("sub").toString();
		MembersDao membersDao = new MembersDao(req.getServletContext());
		MembersDto memberDto =membersDao.findById(username);
		membersDao.close();
		req.setAttribute("nickname", memberDto.getNickname());
		req.setAttribute("auth", isAuthenticated);
		
		chain.doFilter(req, resp);
	}/////
}
