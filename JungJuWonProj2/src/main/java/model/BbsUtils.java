package model;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletResponse;

public class BbsUtils {
	
	public static void historyBack(HttpServletResponse resp,String message) {
		try {
			HttpServletResponse response = (HttpServletResponse)resp;
			resp.setContentType("text/html; charset=utf-8;");
			PrintWriter out=resp.getWriter();
			out.println("<script>");
			out.println("alert('"+message+"')");
			out.println("history.back();");
			out.println("</script>");
		}
		catch (IOException e) {e.printStackTrace();}
	}/////
}
