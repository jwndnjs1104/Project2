
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/template/Configue.jsp"/>
	<title>MyPageUpdate.jsp</title>
	<style>
	
    	@import url('<c:url value="/css/ContainerSize.css"/>');
    
		label{
			font-size:.9em;
		}
		strong{
			margin-top:0;
			font-size:.7em;
			color:red;
		}
	</style>
	<script src='<c:url value="/js/MypageUpdateValidation.js"/>'></script>
</head>
<body>
	<div class="container">
		<div class="container-fluid">

			<jsp:include page="/WEB-INF/template/Header.jsp"/>
			<!-- 컨텐츠 시작 -->
			<jsp:include page="/WEB-INF/template/Jumbotron.jsp"/>
		
			
			<div class="d-flex justify-content-center">
				<fieldset class="border rounded-3 p-3 w-50">
					<legend class="float-none w-auto px-3 text-center fs-2">내 정보 수정</legend>
					
					<form action='<c:url value="/bbs/mypageupdate"/>' method="post">
						<div class="form-floating mb-3">
							<input type="text" class="form-control" id="username" placeholder="아이디 입력" name="username" value="${dto.username }" disabled>
							<label for="username" class="form-label">아이디*</label>
							<strong>&nbsp; </strong>
						</div>
						
						<div class="form-floating mb-0 ">
							<input type="password" class="form-control" id="password" placeholder="비밀번호 입력" name="password">
							<label for="password" class="form-label">비밀번호*</label>
							<strong>&nbsp; </strong>
						</div>
						<div class="form-floating mb-3 ">
							<input type="password" class="form-control" id="passwordConfirm" placeholder="비밀번호 확인" name="passwordConfirm">
							<label for="password" class="form-label">비밀번호 확인*</label>
							<strong>&nbsp; </strong>
						</div>
						
						<div class="form-floating mb-3 ">
							<input type="text" class="form-control" id="nickname" placeholder="닉네임 입력" name="nickname" value="${dto.nickname }">
							<label for="nickname" class="form-label">닉네임</label>
							<strong>&nbsp; </strong>
						</div>
						
						<div class="form-floating my-3 ">
							<input type="text" class="form-control" id="name" placeholder="이름 입력" name="name" value="${dto.name }" disabled>
							<label for="name" class="form-label" >이름*</label>
							<strong>&nbsp; </strong>
						</div>
						
						<label for="phone" class="form-label px-1 mb-0">휴대폰 번호*</label><br/>
						<div class="input-group w-50">
							<span class="input-group-text">010-</span>
							<input type="text" class="form-control text-center col-2 p-2" name="phone1" id="phone1" value="${fn:substring(dto.phone,3,7) }">
							<span class="input-group-text">-</span>
							<input type="text" class="form-control text-center col-2 p-2" name="phone2" id="phone2" value="${fn:substring(dto.phone,7,-1) }">
						</div>
						<strong >&nbsp; </strong>
						
						<div class="form-floating my-3">
							<input type="text" class="form-control" id="email" placeholder="이메일 입력" name="email" value="${dto.email }">
							<label for="email" class="form-label">이메일*</label>
							<strong>&nbsp; </strong>
						</div>
						
						<div>
							<button type="button" class="btn btn-outline-success p-2 mt-4 w-100" onclick="isValidate(event,this.form)">수정</button>
						</div>
					</form>
					<div>
						<a href="<c:url value='/bbs/mypage'/>" class="btn btn-outline-danger p-2 mt-3 w-100" >취소</a>
					</div>
				</fieldset>
			</div>
			<!-- 컨텐츠 끝 -->

			<jsp:include page="/WEB-INF/template/Footer.jsp"/>
			<script>
			
			
			</script>
		</div>
	</div>
</body>
</html>