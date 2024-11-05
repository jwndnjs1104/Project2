<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/template/Configue.jsp"/>
	<title>Login.jsp</title>
	<style>
    	@import url('<c:url value="/css/ContainerSize.css"/>');
    	
    	fieldset{
    		width:600px;
    	}
    </style>
</head>
<body>
	<div class="container">
		<div class="container-fluid ">

			<jsp:include page="/WEB-INF/template/Header.jsp"/>
			<!-- 컨텐츠 시작 -->
			<jsp:include page="/WEB-INF/template/Jumbotron.jsp"/>
			
			<div class="d-flex justify-content-center">
				<fieldset class="border rounded-3 p-3">
					<legend class="float-none w-auto px-3 text-center fs-2">로그인</legend>
					
					<c:if test="${(!empty errorMsg) or (!empty signupSuccessMsg)}">
						<div class="alert alert-<c:if test="${!empty errorMsg }" var="isErrorMsg">danger</c:if><c:if test="${!isErrorMsg }">success</c:if> alert-dismissible my-3" id="errorMsg">
							<button type="button" class="btn-close" data-bs-dismiss="alert"></button>
							<strong>${(!empty errorMsg)?errorMsg:signupSuccessMsg}</strong>
							<c:if test="${!empty signupSuccessMsg}"><small class="fw-bold">로그인 후 이용하세요</small></c:if> 
						</div>
					</c:if>
					
					<form method="post" action="<c:url value='/members/login'/>" >
						<div >
							<input type="text" class="form-control p-3" placeholder="아이디를 입력하세요" name="username"/>
						</div>
						<div class=" my-2">
				    		<input type="password" class="form-control p-3" placeholder="비밀번호를 입력하세요" name="password"/>
						</div>
						<div >
				    		<button class="btn btn-success w-100 p-3" >확인</button>
						</div>
					</form>
					
					<%--
					<div class="text-end my-2">
						<a class="text-decoration-none text-secondary mx-3" href='<c:url value="/members/signup"/>'>아이디 찾기</a> <span class="text-secondary">|</span>
						<a class="text-decoration-none text-secondary mx-3" href='<c:url value="/members/signup"/>'>비밀번호 찾기</a>
					</div>
					 --%>
					
					<a class="btn btn-outline-dark w-100 p-2 mt-5" href='<c:url value="/members/signup"/>'>회원 가입</a>
						
				</fieldset>
			</div>
			<!-- 컨텐츠 끝 -->
			<jsp:include page="/WEB-INF/template/Footer.jsp"/>
		</div>
	</div>
</body>
</html>