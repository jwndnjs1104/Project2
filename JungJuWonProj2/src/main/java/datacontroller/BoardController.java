package datacontroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.bbs.BbsDao;

@WebServlet("/board")
public class BoardController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String boardname = req.getParameter("boardname");
		
		BbsDao dao = new BbsDao(getServletContext());
		int boardid = dao.createBoard(boardname);
		dao.close();
		
		Map<String, String> respJson = new HashMap<>();
		if(boardid == 0) respJson.put("err", "게시판 생성 실패");
		else respJson.put("boardid", String.valueOf(boardid));
		resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(objectMapper.writeValueAsString(respJson));
	}
}
