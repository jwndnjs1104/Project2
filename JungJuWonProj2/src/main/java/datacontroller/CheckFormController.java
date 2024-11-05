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
import model.members.MembersDao;

@WebServlet("/checkform")
public class CheckFormController extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		
		BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String requestBody = sb.toString();
        JsonNode jsonNode = objectMapper.readTree(requestBody);
        String username = null;
        String nickname = null;
        if(jsonNode.get("username") != null) username = jsonNode.get("username").asText();
        if(jsonNode.get("nickname") != null) nickname = jsonNode.get("nickname").asText();
        System.out.println(username);
        System.out.println(nickname);
        
        Map<String, String> jsonResponse = new HashMap<>(); 
		if(username != null) {
			//데이터 베이스에 값 확인
			MembersDao dao = new MembersDao(getServletContext());
			boolean exist = dao.existByUsername(username);
			System.out.println("아이디 존재하냐? "+exist);
			dao.close();
			jsonResponse.put("existUsername", String.valueOf(exist));
		}
		
		if(nickname != null) {
			//데이터 베이스에 값 확인
			MembersDao dao = new MembersDao(getServletContext());
			boolean exist = dao.existByNickname(nickname);
			if(username != null && dao.findById(username).getNickname().equals(nickname)) {
				exist=false;
			}
			System.out.println("닉네임 존재하냐? "+exist);
			dao.close();
			jsonResponse.put("existNickname", String.valueOf(exist));
		} 
		
		// JSON 응답 반환
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
	}
}
