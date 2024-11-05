package controller;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.BbsUtils;
import model.FileUtils;
import model.JWTokens;
import model.bbs.BbsDao;
import model.bbs.BbsDto;

@WebServlet("/bbs/write")
@MultipartConfig(maxFileSize = 1024*2000, maxRequestSize = 1024*2000*5)
public class WriteController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/bbs/Write.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Collection<Part> parts = req.getParts();
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		String boardid = req.getParameter("boardid");
		String username = (String)JWTokens.getTokenPayloads(JWTokens.getTokenInCookie(req, req.getServletContext().getInitParameter("TOKEN-NAME"))).get("sub");
		String saveDirectroy = req.getServletContext().getRealPath("/upload");
		StringBuffer buffer = FileUtils.upload(parts, saveDirectroy);
		BbsDto dto;
		
		if(buffer == null) {
			dto = BbsDto.builder().title(title).content(content).boardid(Long.parseLong(boardid)).username(username).files(null).build();
		}
		else {
			dto = BbsDto.builder().title(title).content(content).boardid(Long.parseLong(boardid)).username(username).files(buffer.toString()).build();
		}
		
		BbsDao dao = new BbsDao(getServletContext());
		int isSuccess = dao.insert(dto);
		dao.close();
		if(isSuccess==0) {
			FileUtils.deletes(buffer, saveDirectroy, ",");
			BbsUtils.historyBack(resp, "글등록 실패");
			return;
		}
		
		resp.sendRedirect(req.getContextPath()+"/bbs/list?boardid="+boardid);
	}
}
