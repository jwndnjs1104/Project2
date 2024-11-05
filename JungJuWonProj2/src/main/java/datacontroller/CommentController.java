package datacontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.JWTokens;
import model.comments.CommentsDao;
import model.comments.CommentsDto;
import model.members.MembersDao;

@WebServlet("/comment")
public class CommentController extends HttpServlet{
	//댓글 입력
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("댓글 컨트롤러 진입");
		
		ObjectMapper objectMapper = new ObjectMapper();
		BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String requestBody = sb.toString();
        JsonNode jsonNode = objectMapper.readTree(requestBody);
        
		String comment= jsonNode.get("comment").asText(); //댓글내용
		long bbsid= Long.parseLong(jsonNode.get("bbsid").asText());
		String nickname= jsonNode.get("nickname").asText(); //닉네임
		String username = (String)JWTokens.getTokenPayloads(JWTokens.getTokenInCookie(req, req.getServletContext().getInitParameter("TOKEN-NAME"))).get("sub");
		String commentid = req.getParameter("commentid"); //댓글 번호(수정할때만 보냄)
		System.out.println("댓글 수정시 받는 commentid: "+commentid);
		
		CommentsDao dao = new CommentsDao(getServletContext());
		Map<String, String> jsonResponse = new HashMap<>(); //응답 데이터 넣을 map
		
		if(commentid != null) { //댓글 수정
			CommentsDto dto = CommentsDto.builder().content(comment).commentid(Long.parseLong(commentid)).build();
			int isSuccess = dao.update(dto);
			dao.close();
			
			//json 형식 응답 데이터 넣기
			if(isSuccess==0) {
				System.out.println("댓글 수정 쿼리 실패");
				jsonResponse.put("isSuccess", "false");
			}
			else {
				System.out.println("댓글 수정 성공");
				jsonResponse.put("isSuccess", "true");
			}
		}
		else { //댓글 입력
			CommentsDto dto = CommentsDto.builder().content(comment).bbsid(bbsid).username(username).build();
			int createdCommentid = dao.insert(dto); //생성된 댓글id
			//int commentCount = dao.getCountByBbsid(String.valueOf(bbsid));
			dao.close();
			
			//json 형식 응답
			if(createdCommentid==0) {
				System.out.println("댓글 입력 쿼리 실패");
				jsonResponse.put("isSuccess", "false");
			}
			else {
				System.out.println("댓글 입력 성공");
				jsonResponse.put("isSuccess", "true");
				jsonResponse.put("commentid", String.valueOf(createdCommentid));
			}
		}////////
		
		resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
	}/////////////////////
	
	
	//댓글 삭제
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("댓글 삭제 컨트롤러 진입");
		String mode = req.getParameter("mode");
		String commentid = req.getParameter("commentid");
		System.out.println(mode);
		System.out.println(commentid);
		
		if(mode.equals("delete")) {
			CommentsDao dao = new CommentsDao(getServletContext());
			CommentsDto dto =  CommentsDto.builder().commentid(Long.parseLong(commentid)).build();
			int isSuccess = dao.delete(dto);
			if(isSuccess == 1) System.out.println("댓글 삭제 완료우"); 
			dao.close();
		}
	}
}
