package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BbsUtils;
import model.members.MembersDao;
import model.members.MembersDto;

@WebServlet("/members/signup")
public class SignupController extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String[]> signupParams = req.getParameterMap();
		MembersDto dto = MembersDto.builder().username(signupParams.get("username")[0])
											 .password(signupParams.get("password")[0])
											 .name(signupParams.get("name")[0])
											 .nickname(signupParams.get("nickname")[0])
											 .phone("010"+signupParams.get("phone1")[0]+signupParams.get("phone2")[0])
											 .email(signupParams.get("email")[0])
											 .build();
		if(signupParams.get("nickname")[0].length() == 0) {
			if(dto.getUsername().length() > 8) {
				dto.setNickname(dto.getUsername().substring(0,8));
			}
			else {
				dto.setNickname(dto.getUsername());	
			}
		}
		
		MembersDao dao = new MembersDao(getServletContext());
		int isCorrect = dao.insert(dto);
		dao.close();
		if(isCorrect == 1) {
			req.setAttribute("signupSuccessMsg",signupParams.get("username")[0]+"님 회원가입에 성공했습니다");
			req.getRequestDispatcher("/WEB-INF/bbs/Login.jsp").forward(req, resp);
			return;
		}
		else {
			BbsUtils.historyBack(resp, "회원가입 실패");
		}
	}/////////
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.getRequestDispatcher("/WEB-INF/bbs/Signup.jsp").forward(req, resp);
	}
	
}
