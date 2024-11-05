package model;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.crypto.SecretKey;

import org.apache.tomcat.util.file.ConfigurationSource.Resource;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class JWTokens {
	private static SecretKey scSecretKey; //인코딩된 키 담을 변수
	private static final String SCSECRETKEY_PATH="/resources/tokens"; //확장자는 생략 
	private static final String KEY="secretKey";
	
	static {
		//비밀키가 저장된 파일에서 비밀키값 읽어오기
		ResourceBundle resourceBundle = ResourceBundle.getBundle(SCSECRETKEY_PATH); //토큰 리소스 경로
		String secretKey = resourceBundle.getString(KEY);
		//비밀키를 Base64로 인코딩 후 byte[] 로 변환
		byte[] encodedSecretKey=Base64.getEncoder().encodeToString(secretKey.getBytes()).getBytes(StandardCharsets.UTF_8);
		scSecretKey = Keys.hmacShaKeyFor(encodedSecretKey);
		
	}
	/**
	 *JWT토큰을 생성해서 반환하는 메소드 
	 * @param username 사용자 아이디
	 * @param payloads 추가로 사용자 정보를 저장하기 위한 Claims
	 * @param expirationTime 토큰 만료 시간(15분에서 몇 시간이 적당). 단위는 천분의 1초
	 * @return
	 */
	public static String createToken(String username, Map<String,Object> payloads, long expirationTime) {
		//JWT 토큰의 만료 시간 설정
		long currentTimeMillis=System.currentTimeMillis(); //토큰 생성시간
		expirationTime+=currentTimeMillis; //토큰의 만료시간
		
		//Header 부분 설정
		Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "JWT");
		headers.put("alg", "HS256");
		
		/* Claims 객체 생성 후 JwtBuilder의 claims(claims) 이렇게 해도 됨.
		Claims claims=(Claims) Jwts.claims().setSubject(username);
		claims.putAll(payloads);
		claims.put("role", "ADMIN");
		*/
		
		//payload 부분 설정
		JwtBuilder builder = Jwts.builder()
				.header()
				.add(headers)
				.and()
				.claims(payloads) //Claims 설정(기타 페이로드)
				.subject(username) //"sub"키를 사용자 ID 로 설정
				.issuedAt(new Date()) //"iat"키로 토큰생성시간 설정
				.expiration(new Date(expirationTime)) //만료시간 설정(필수임)
				.signWith(scSecretKey,Jwts.SIG.HS256);//비밀키로 JWT 서명
		//JWT생성
		String jwt = builder.compact();
		return jwt;
	}///////
	
	/**
	 * 발급한 토큰의 payloads 부분을 반환하는 메소드 
	 * @param token 발급토큰
	 * @return 토큰의 payloads부분 반환
	 */
	public static Map<String, Object> getTokenPayloads(String token){
		Map<String, Object> claims = new HashMap<>();
		try {
			//유효하지 않은 토큰이면 아래 코드 실행시 예외 발생
			claims = Jwts.parser()
					.verifyWith(scSecretKey).build() //서명한 비밀키로 검증
					.parseSignedClaims(token) //parseClaimsJws메소드는 만기일자 체크
					.getPayload();
		}
		catch (Exception e) {
			claims.put("invalid", "유효하지 않은 토큰");
		}
		return claims;
	}//////
	
	/**
	 * 유효한 토큰인지 검증하는 메소드
	 * @param token 발급토큰
	 * @return 유효한 토큰이면 true, 만료가 됐거나 변조된 토큰인 경우 false 반환
	 */
	public static boolean verifyToken(String token) {
		try {
			//JWT토큰 파싱 및 검증
			Jws<Claims> claims = Jwts.parser().verifyWith(scSecretKey).build().parseSignedClaims(token);
			System.out.println("만기일자: "+claims.getPayload().getExpiration());
			return true;
		}
		catch (JwtException | IllegalArgumentException e) {
			System.out.println("유효하지 않은 토큰입니다: "+e.getMessage());
		}
		return false;
	}//////
	
	/**
	 * 
	 * @param request HttpServletRequest객체
	 * @param tokenName web.xml에 등록한 context 초기화 파라미터 값(파라미터명은 "TOKEN-NAME")
	 * @return 발급받은 토큰 값
	 */
	public static String getTokenInCookie(HttpServletRequest request, String tokenName) {
		Cookie[] cookies = request.getCookies();
		String token="";

		if(cookies!=null){
			for(Cookie cookie:cookies){
				if(cookie.getName().equals(tokenName)){
					token=cookie.getValue();
				}
			}
		}
		return token;
	}
}	
