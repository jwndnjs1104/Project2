package controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.BbsUtils;
import model.FileUtils;
import model.bbs.BbsDao;
import model.bbs.BbsDto;

@WebServlet("/bbs/edit")
@MultipartConfig(maxFileSize = 1024*2000, maxRequestSize = 1024*2000*5)
public class EditController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String bbsid = req.getParameter("bbsid");
		BbsDao dao = new BbsDao(getServletContext());
		BbsDto dto = dao.findById(bbsid);
		dao.close();
		req.setAttribute("nowPage", req.getParameter("nowPage"));
		req.setAttribute("searchColumn", req.getParameter("searchColumn"));
		req.setAttribute("searchWord", req.getParameter("searchWord"));
		req.setAttribute("dto", dto);
		
		req.getRequestDispatcher("/WEB-INF/bbs/Edit.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//기존에 저장된 건 alert로 되어있는걸로 없어진거 판단
		//파일 있는건 input hidden 타입으로 name, value 속성은 그 파일 이름으로 만듦.
		//따라서 기존 파일 리스트를 하나씩 꺼내와서 체크해보면 없어진 파일을 알 수 있고, 그 파일은 서버에서 삭제하면 된다.
		//새롭게 추가한 파일들은 part에서만 확인하고 위에서 체크한 파일들에 더해서 글 업데이트하면 됨
		
		System.out.println("게시글 수정 컨트롤러 진입");
		
		//새롭게 추가한 파일들 받기
		Collection<Part> parts = req.getParts();
		
		//변경될 수 있는 글제목, 글내용 받기
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		String boardid = req.getParameter("boardid");
		
		String saveDirectroy = req.getServletContext().getRealPath("/upload");
		
		//기존파일 변경된 점 있는지 확인
		String bbsid = req.getParameter("bbsid");
		BbsDao dao = new BbsDao(getServletContext());
		BbsDto fdto = dao.findById(bbsid);
		StringBuffer deletefilebuffer = new StringBuffer(); //수정페이지에서 없어진 파일들 담기(서버에서 삭제할 파일), 빈 문자열
		StringBuffer existFilebuffer = new StringBuffer(); //없어질 파일 뺀 나머지
		String existfile = "";
		
		if(fdto.getFiles()!=null) {
			String[] fileList = fdto.getFiles().split(","); //기존 파일들
			for(String file:fileList) {
				if(req.getParameter(file)==null) deletefilebuffer.append(file+",");
				else existFilebuffer.append(file+",");
			}
			if(deletefilebuffer.length() != 0 ) {
				deletefilebuffer = deletefilebuffer.deleteCharAt(deletefilebuffer.length()-1);
				System.out.println("삭제할 파일: "+deletefilebuffer.toString());
			}
			if(existFilebuffer.length() != 0) {
				existfile = existFilebuffer.deleteCharAt(existFilebuffer.length()-1).toString();
				System.out.println("새 파일: "+existfile);
			}
		}
		
		//새롭게 추가된 파일이름
		StringBuffer buffer = new StringBuffer();
		buffer = FileUtils.upload(parts, saveDirectroy); //업로드
		BbsDto dto = BbsDto.builder().title(title).content(content).bbsid(Long.parseLong(bbsid)).build();
		
		//새롭게 받은 파일 있으면 기존 파일에 이어 붙여야 함
		if(buffer != null) {
			System.out.println("새롭게 추가된 파일: "+buffer.toString());
			//기존 파일에 추가하기
			if(!existfile.equals("")) existfile = existfile+","+buffer.toString();
			else existfile = buffer.toString();
			System.out.println("최종 업데이트 할 파일 목록: "+existfile);
		}
		dto.setFiles(existfile);

		//업데이트
		int isSuccess = dao.update(dto);
		dao.close();
		
		//검색어 전달하기
		String searchWord = req.getParameter("searchWord");
		String searchColumn = req.getParameter("searchColumn");
		
		if(isSuccess==1) {
			if(deletefilebuffer != null) FileUtils.deletes(deletefilebuffer, saveDirectroy, ","); //데이터베이스 업데이트 성공하면 기존파일들에서 없어진 파일 서버에서 삭제
			String url = req.getContextPath()+"/bbs/view?bbsid="+bbsid+"&boardid="+boardid+"&nowPage="+req.getParameter("nowPage");
			if(searchWord != null) {
				String encodedSearchWord = URLEncoder.encode(searchWord, StandardCharsets.UTF_8);
				url+="&searchColumn="+searchColumn+"&searchWord="+encodedSearchWord;
			}
			resp.sendRedirect(url);
			return;
		}
		else {
			if(buffer != null) FileUtils.deletes(buffer, saveDirectroy, ","); //데이터베이스 업데이트 실패하면 새롭게 추가한 파일 서버에서 삭제
			BbsUtils.historyBack(resp, "글수정 실패"); //뒤로가기
			return;
		}
	}
}
