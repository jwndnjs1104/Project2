package controller;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BbsUtils;
import model.JWTokens;
import model.members.MembersDao;
import model.members.MembersDto;

@WebServlet("/bbs/mypageupdate")
public class MyPageUpdateController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = (String)JWTokens.getTokenPayloads(JWTokens.getTokenInCookie(req, req.getServletContext().getInitParameter("TOKEN-NAME"))).get("sub");
		
		MembersDao dao = new MembersDao(getServletContext());
		MembersDto dto = dao.findById(username);
		req.setAttribute("dto", dto);
		//System.out.println(username);
		dao.close();
		
		req.getRequestDispatcher("/WEB-INF/bbs/MyPageUpdate.jsp").forward(req, resp);
	}////////
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String[]> map = req.getParameterMap();
		String username = JWTokens.getTokenPayloads(JWTokens.getTokenInCookie(req, req.getServletContext().getInitParameter("TOKEN-NAME"))).get("sub").toString();
		MembersDto dto = MembersDto.builder().password(map.get("password")[0])
				.nickname(map.get("nickname")[0])
				.phone("010"+map.get("phone1")[0]+map.get("phone2")[0])
				.email(map.get("email")[0])
				.username(username).build();
		
		if(map.get("nickname")[0].length() == 0) {
			if(username.length() > 8) dto.setNickname(username.substring(0,8));
			else dto.setNickname(username);	
		}
		
		MembersDao dao = new MembersDao(getServletContext());
		int isUpdate = dao.update(dto);
		dao.close();
		if(isUpdate == 0) {
			BbsUtils.historyBack(resp, "회원정보 수정 실패");
		}
		resp.sendRedirect(req.getContextPath()+"/bbs/mypage");
	}////////
}
