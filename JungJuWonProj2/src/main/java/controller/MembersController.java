package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.members.MembersDao;
import model.members.MembersDto;

@WebServlet("/bbs/memberlist")
public class MembersController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//회원정보 리스트
		MembersDao dao = new MembersDao(getServletContext());
		List<MembersDto> members = dao.findAll(null);
		req.setAttribute("members", members);
		dao.close();
		
		req.getRequestDispatcher("/WEB-INF/bbs/Members.jsp").forward(req, resp);
	}
}
