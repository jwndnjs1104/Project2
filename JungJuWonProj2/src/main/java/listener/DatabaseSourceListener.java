package listener;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jakarta.servlet.ServletContextAttributeListener;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DatabaseSourceListener implements ServletContextListener, ServletContextAttributeListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("웹 어플리케이션이 시작되었습니다");
		System.out.println("서버 정보: "+sce.getServletContext().getServerInfo());
		System.out.println("컨텍스트 루트: "+sce.getServletContext().getContextPath());
		
		DataSource source;
		try {
			//데이터소스를 어플리케이션 영역에 저장
			Context ctx = new InitialContext();
			source = (DataSource)ctx.lookup(sce.getServletContext().getInitParameter("JNDI-ROOT")+"/proj2");
			sce.getServletContext().setAttribute("DATA-SOURCE", source);
		}
		catch (NamingException e) {e.printStackTrace();}
		
	}
}
