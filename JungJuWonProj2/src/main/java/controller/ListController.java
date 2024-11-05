package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.PagingUtil;
import model.bbs.BbsDao;
import model.bbs.BbsDto;

@WebServlet(urlPatterns =  "/bbs/list" ,initParams = {@WebInitParam(name = "pageSize", value = "15"), @WebInitParam(name = "blockPage", value = "5")})
public class ListController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("리스트 컨트롤러 진입");
		//보드 아이디
		String boardid = req.getParameter("boardid");
		req.setAttribute("boardid", boardid); //@@@@@@@@@@ boardid
		
		
		//게시판 목록
		BbsDao dao = new BbsDao(getServletContext());
		List<BbsDto> boards = dao.findBoardsAll();
		req.setAttribute("boards", boards); //@@@@@@@@@@ boards map
		
		
		//공지 목록
		List<BbsDto> noticeBbs = dao.findNotice();
		req.setAttribute("noticeBbs", noticeBbs); //@@@@@@@@@@ notice list
		
		
		//검색, 페이징 관련 파라미터
		String searchColumn = req.getParameter("searchColumn");
		System.out.println("searchColumn: "+searchColumn);
		String searchWord = req.getParameter("searchWord")!=null?req.getParameter("searchWord").trim():req.getParameter("searchWord");
		
		Map<String, String> map = new HashMap<>();
		String page = req.getContextPath()+"/bbs/list?boardid="+boardid+"&";
		String searchQuery = "";
		if(searchColumn != null && searchWord.length() !=0 ){
			System.out.println("searchWord: "+searchWord);
			map.put("searchColumn", searchColumn);
			map.put("searchWord", searchWord);
			searchQuery=String.format("searchColumn=%s&searchWord=%s&", searchColumn, searchWord);
			page+=searchQuery;
		}
		map.put("boardid", boardid);
		map.put(PagingUtil.PAGE_SIZE, getInitParameter(PagingUtil.PAGE_SIZE));
		map.put(PagingUtil.BLOCK_PAGE, getInitParameter(PagingUtil.BLOCK_PAGE));
		PagingUtil.setMapForPaging(map, dao, req);
		
		
		//bbs
		if(!boardid.equals("0")) {
			List<BbsDto> bbses = dao.findAllByBoard(map);
			req.setAttribute("bbses", bbses); //@@@@@@@@@@ dto list
		}
		else {
			List<BbsDto> bbses = dao.findAll(null);
			req.setAttribute("bbses", bbses);
		}
		
		//페이징
		String paging = PagingUtil.pagingBootStrapStyle(Integer.parseInt(map.get(PagingUtil.TOTAL_RECORD_COUNT)), 
														Integer.parseInt(map.get(PagingUtil.PAGE_SIZE)), 
														Integer.parseInt(map.get(PagingUtil.BLOCK_PAGE)), 
														Integer.parseInt(map.get(PagingUtil.NOWPAGE)), 
														page);
		req.setAttribute("paging", paging);
		req.setAttribute("pagemap", map);
		req.setAttribute("searchWord", searchWord);
		req.setAttribute("searchColumn", searchColumn);
		dao.close();
		
		req.getRequestDispatcher("/WEB-INF/bbs/List.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
