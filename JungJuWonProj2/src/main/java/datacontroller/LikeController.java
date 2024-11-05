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
import model.likes.LikesDao;
import model.likes.LikesDto;

@WebServlet("/like")
public class LikeController extends HttpServlet{
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
        
        String username = (String)JWTokens.getTokenPayloads(JWTokens.getTokenInCookie(req, req.getServletContext().getInitParameter("TOKEN-NAME"))).get("sub");
        String bbsid = jsonNode.get("bbsid").asText();
        String type = jsonNode.get("type").asText();
        LikesDto dto = LikesDto.builder().bbsid(bbsid).username(username).build();
        LikesDao dao = new LikesDao(getServletContext());
        Map<String, String> respJson = new HashMap<>();
        int isSuccess = 0;
        
        if(type.equals("insert")) {
        	isSuccess = dao.insert(dto);
        	dao.close();
        }
        else if(type.equals("delete")){
        	isSuccess = dao.delete(dto);
        	dao.close();
        }
        else dao.close();
        
        if(isSuccess == 0 ) respJson.put("isSuccess", "false");
    	else respJson.put("isSuccess", "true");
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(objectMapper.writeValueAsString(respJson));
	}
}
