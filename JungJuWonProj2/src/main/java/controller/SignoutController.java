package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BbsUtils;
import model.JWTokens;
import model.members.MembersDao;
import model.members.MembersDto;

@WebServlet("/members/signout")
public class SignoutController extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String password = req.getParameter("password");
		String username = (String)JWTokens.getTokenPayloads(JWTokens.getTokenInCookie(req, req.getServletContext().getInitParameter("TOKEN-NAME"))).get("sub");
		MembersDto dto = MembersDto.builder().password(password).username(username).build();
		
		MembersDao dao = new MembersDao(getServletContext());
		boolean ismember = dao.isMember(dto);
		if(ismember) {
			int isSuccess = dao.delete(dto);
			dao.close();
			if(isSuccess == 1) {
				resp.sendRedirect(req.getContextPath()+"/members/logout");
				return;
			}
		}
		
		BbsUtils.historyBack(resp, "회원 탈퇴 실패. 관리자에게 문의하세요");
	}
}
