
<%@page import="model.JWTokens"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- 상단 네비게이션 바 -->
<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
	<div class="container-fluid">
		<a class="navbar-brand" href="<c:url value="/home"/>"><i class="fa-solid fa-house-chimney"></i></a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#collapsibleNavbar">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse justify-content-end" id="collapsibleNavbar">
			<ul class="navbar-nav">
				<%--
				<c:set value='<%=JWTokens.verifyToken(JWTokens.getTokenInCookie(request, application.getInitParameter("TOKEN-NAME"))) %>' var='isValidToken' />
				 --%>
				
				<c:if test="${auth}" var="isUser">
					<c:set value="${nickname }" var="nickname"/>
					<li class="nav-item dropdown mx-2">
						<a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#"><i class="fa-regular fa-user"></i> ${nickname} 님</a>
						<ul class="dropdown-menu">
							<li><a class="dropdown-item " href="<c:url value='/bbs/mypage'/>">내 정보</a></li>
							<li><a class="dropdown-item " href='<c:url value="/bbs/mypagebbscomments"/>'>내 작성글</a></li>
							<li><a class="dropdown-item " href='<c:url value="/bbs/mypagebbscomments"/>?mode=comments'>내 댓글</a></li>
						</ul>
					</li>
					
					<c:if test="${nickname == '관리자' }">
						<li class="nav-item">
							<a class="nav-link" href="<c:url value='/bbs/memberlist'/>"><i class="fa-solid fa-users"></i> 회원관리 </a>
						</li>
					</c:if>

					<li class="nav-item">
						<a class="nav-link" href="<c:url value='/members/logout'/>"><i class="fas fa-sign-out-alt"></i> 로그아웃</a>
					</li>
				</c:if>
				
				<c:if test="${!isUser}">
					<li class="nav-item">
						<a class="nav-link" href="<c:url value='/members/login'/>"><i class="fas fa-sign-in-alt"></i> 로그인</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="<c:url value='/members/signup'/>"><i class="fas fa-sign-in-alt"></i> 회원가입</a>
					</li>
				</c:if>
			</ul>
			
		</div>
	</div>
</nav>
<!-- 상단 네비게이션 바 끝 -->