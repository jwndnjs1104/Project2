package model;

import java.util.Map;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import service.DaoService;

public class PagingUtil {
	
	public static final String START="start";
	public static final String END="end";
	public static final String TOTAL_RECORD_COUNT="totalRecordCount";
	public static final String PAGE_SIZE="pageSize";
	public static final String BLOCK_PAGE="blockPage";
	public static final String TOTAL_PAGE="totalPage";
	public static final String NOWPAGE="nowPage";

	public static String pagingText(int totalRecordCount,int pageSize,int blockPage,int nowPage,String page){
		String pagingStr="";
		
		//1.전체 페이지 구하기
		int totalPage= (int)(Math.ceil(((double)totalRecordCount/pageSize)));
		
		int intTemp = ((nowPage - 1) / blockPage) * blockPage + 1;

		//처음 및 이전을 위한 로직
		if(intTemp != 1){
                
			pagingStr+="<a class=\"page-link\" href='"+page+NOWPAGE+"=1'>◀◀</a>&nbsp;<a class=\"page-link\" href='"+page+NOWPAGE+"="+(intTemp -blockPage)+"'>◀</a>";
		}
		
		//페이지 표시 제어를 위한 변수
		int blockCount = 1;
		
		//페이지를 뿌려주는 로직
		//블락 페이지 수만큼 혹은 마지막 페이지가 될때까지 페이지를 표시한다1 
		while(blockCount <= blockPage && intTemp <= totalPage){  // 페이지 오버 를 체크
				//현재 페이지를 의미함
			if(intTemp == nowPage){  
				pagingStr+="<span class=\"page-link text-danger\" >"+intTemp+"</span>&nbsp;";
			}
		     else
		    	 pagingStr+="<a class=\"page-link\" href='"+page+NOWPAGE+"="+intTemp+"'>"+intTemp+"</a>&nbsp;";
		       
			intTemp = intTemp + 1;
			blockCount = blockCount + 1;
		
		}

		//다음 및 마지막을 위한 로직
			
		if(intTemp <= totalPage){

			pagingStr+="<a class=\"page-link\" href='"+page+NOWPAGE+"="+intTemp+"'>▶</a>&nbsp;<a class=\"page-link\" href='"+page+NOWPAGE+"="+totalPage+"'>▶▶</a>"; 

				   
		}
		
		return pagingStr;
	}
	
	
	/**
	 * 부트스트랩4 스타일의 페이지네이션 UI를 문자열로 반환하는 메소드
	 * @param totalRecordCount  총 레코드수
	 * @param pageSize  한 페이지에 보여줄 레코드 수
	 * @param blockPage  페이지싱시 보여줄 페이지 번호의 수
	 * @param nowPage 사용자가 클릭한 페이지 번호
	 * @param page   페이징을 적용할 페이지명(예:List.jsp?) 반드시 ?포함
	 * @return
	 */
	public static String pagingBootStrapStyle(int totalRecordCount,int pageSize,int blockPage,int nowPage,String page){
		String pagingStr="<ul class=\"pagination pagination-sm justify-content-center\">";
		
		//1.전체 페이지 구하기
		int totalPage= (int)(Math.ceil(((double)totalRecordCount/pageSize)));
		int intTemp = ((nowPage - 1) / blockPage) * blockPage + 1;

		//처음 및 이전을 위한 로직
		if(intTemp != 1){
			pagingStr+="<li class=\"page-item\">\r\n" + 
					"<a  class=\"page-link\" href='"+page+NOWPAGE+"=1'>\r\n" + 
					"<span >&laquo;</span>\r\n" + 
					"</a>\r\n" + 
					"</li>\r\n" + 
					"<li class=\"page-item\">\r\n" + 
					"<a class=\"page-link\" href='"+page+NOWPAGE+"="+(intTemp -blockPage)+"' >\r\n" + 
					"<span>&lsaquo;</span>\r\n" + 
					"</a>\r\n" + 
					"</li>";   
		}
		
		//페이지 표시 제어를 위한 변수
		int blockCount = 1;
		
		//페이지를 뿌려주는 로직
		//블락 페이지 수만큼 혹은 마지막 페이지가 될때까지 페이지를 표시한다1 
		while(blockCount <= blockPage && intTemp <= totalPage){  // 페이지 오버 를 체크
				//현재 페이지를 의미함
			if(intTemp == nowPage){  
				pagingStr+="<li class=\"page-item\"><a class=\"page-link\" href='#'><span class=\"text-danger\">"+intTemp+"</span></a></li>";
			}
		     else
		    	 pagingStr+="<li class=\"page-item\"><a class=\"page-link\" href='"+page+NOWPAGE+"="+intTemp+"'>"+intTemp+"</a></li>";
		       
			intTemp = intTemp + 1;
			blockCount = blockCount + 1;
		}

		//다음 및 마지막을 위한 로직
		if(intTemp <= totalPage){
			pagingStr+="<li class=\"page-item\">\r\n" + 
					"<a class=\"page-link\" href='"+page+NOWPAGE+"="+intTemp+"'>\r\n" + 
					"<span>&rsaquo;</span>\r\n" + 
					"</a>\r\n" + 
					"</li>\r\n" + 
					"<li class=\"page-item\">\r\n" + 
					"<a class=\"page-link\" href='"+page+NOWPAGE+"="+totalPage+"' >\r\n" + 
					"<span >&raquo;</span>\r\n" + 
					"</a>\r\n" + 
					"</li>";
							   
		}
		pagingStr+="</ul>";
		return pagingStr;
	}////////////////////////////////
	
	
	//페이징시 필요 속성(키)를 저장한는 맵 설정용 메소드
	//Map map:필요한 키와 값을 담을 비어있는 맵
	//T dao :  DAO계열을 받는다
	//제너릭 메소드로 구현:DAOService 를 상속받은 모든 DAO계열을 인자로 받기위해서-중복 코딩 방지
	/**
	 * 빈 맵을 인자로 받아서 페이징과 관련된 값을 맵에 설정하는 메소드
	 * @param <T>  제너릭 메소드라는 의미
	 * @param map  페이징과 관련된 정보를 설정할 빈 맵
	 * @param dao  DaoService를 상속받은 DAO계열 객체
	 * @param request 현재 페이지 번호를 받기위한 HttpServletRequest객체
	 * */
	public static <T extends DaoService> void setMapForPaging(Map<String,String> map,T dao,HttpServletRequest request) {
		//페이징을 위한 로직 시작
		//전체 레코드수
		int totalRecordCount = dao.getTotalRecordCount(map);
		//페이지 사이즈와 블락페이지는 서블릿 초기화 파라미터로 설정해 놓은 값
		//페이지사이즈
		int pageSize = Integer.parseInt(map.get(PAGE_SIZE));
		//블락페이지
		int blockPage= Integer.parseInt(map.get(BLOCK_PAGE));
		//전체 페이지수
		int totalPage = (int)Math.ceil((double)totalRecordCount/pageSize);
		//현재 페이지 번호
		int nowPage =request.getParameter(NOWPAGE)==null? 1 : Integer.parseInt(request.getParameter(NOWPAGE));
		
		//시작 및 끝 ROWNUM구하기
		int start = (nowPage-1)*pageSize+1;
		int end = nowPage*pageSize;	
		//페이징을 위한 로직 끝	
		map.put(START,String.valueOf(start));
		map.put(END,String.valueOf(end));
		map.put(TOTAL_RECORD_COUNT, String.valueOf(totalRecordCount));
		map.put(PAGE_SIZE, String.valueOf(pageSize));
		map.put(BLOCK_PAGE, String.valueOf(blockPage));
		map.put(TOTAL_PAGE, String.valueOf(totalPage));
		map.put(NOWPAGE, String.valueOf(nowPage));
	}////////////////////////////
}
