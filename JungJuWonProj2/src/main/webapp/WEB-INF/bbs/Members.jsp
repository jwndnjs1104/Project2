
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/template/Configue.jsp"/>
	<title>MyPage.jsp</title>
	<style>
    	@import url('<c:url value="/css/ContainerSize.css"/>');
    </style>
</head>
<body>
	<div class="container">
		<div class="container-fluid">

			<jsp:include page="/WEB-INF/template/Header.jsp"/>
			<!-- 컨텐츠 시작 -->
			<jsp:include page="/WEB-INF/template/Jumbotron.jsp"/>
			
			<fieldset id="frame" class="border rounded-3 p-3">
				<legend class="float-none w-auto px-3">회원관리</legend>
				<div class="container" style="font-size: .9em">
					
					<!-- 회원 정보 -->
					<div class="d-flex justify-content-between">
						<div class="d-flex">
							<h1 class="fs-3 ">회원 정보</h1>
						</div>
					</div>
					<table class="table table-hover text-center">
		                <thead class="table-success ">
		                    <tr class="">
		                        <th class="col-2">아이디</th>
		                        <th class="col-2">닉네임</th>
		                        <th class="col-2  px-2">이름</th>
		                        <th class="col-2">연락처</th>
		                        <th class="col-3">이메일</th>
		                        <th class="col-1">가입일</th>
		                    </tr>
		                </thead>
		                
						<tbody>
	                   		<c:if test="${members.size() == 0 }" var="isEmpty" >
								<tr>
	                            	<td colspan="6">가입한 회원이 없습니다.</td>
	                        	</tr>
							</c:if>
							<c:if test="${!isEmpty }">
		                       	<c:forEach items="${members }" var="member">
									<tr>
										<td class="align-content-center p-0">${member.username }</td>
										<td class="align-content-center">${member.nickname }</td>
										<td class="align-content-center">${member.name }</td>
										<td class="align-content-center">${member.phone }</td>
										<td class="align-content-center">${member.email }</td>
										<td><fmt:formatDate value="${member.regidate}" pattern="yy.MM.dd."/></td>
									</tr>
		                        </c:forEach>
							</c:if>
						</tbody>
					</table>
					
				</div>
			</fieldset>
			
		</div>
	</div>
</body>
</html>