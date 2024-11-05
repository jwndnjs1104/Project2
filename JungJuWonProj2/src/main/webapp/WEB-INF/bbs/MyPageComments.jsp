
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
				<legend class="float-none w-auto px-3">마이페이지</legend>
				<div class="container" style="font-size: .9em">
					<!-- 내 댓글 -->
					<div class="d-flex justify-content-between">
						<h1 class="fs-3 mt-3">내 댓글 &nbsp;<span class="fs-5 ">${commentsAll.size()==0?'':commentsAll.size() }</span></h1>
					</div>
					<table class="table table-hover text-center">
		                <thead class="table-success ">
		                    <tr class="">
		                        <th class="col-2">게시판</th>
		                        <th class="col-1">글 번호</th>
		                        <th class="col-3 ">글 제목</th>
		                        <th class="col-5 ">댓글 내용</th>
		                        <th class="col-1">작성일</th>
		                    </tr>
		                </thead>
		                
						<tbody>
	                   		<c:if test="${commentsAll.size() == 0 }" var="isEmpty" >
								<tr>
	                            	<td colspan="6">등록된 글이 없습니다.</td>
	                        	</tr>
							</c:if>
							<c:if test="${!isEmpty }">
								<c:forEach items="${commentsAll }" var="comment">
									<tr>
										<td class="align-content-center">${comment.boardname}</td>
										<td class="align-content-center p-0">${comment.bbsid}</td>
										<td class="text-start align-content-center px-1">
											<a class="text-decoration-none text-dark px-2" href='<c:url value="/bbs/view"/>?boardid=${comment.boardid}&bbsid=${comment.bbsid}'> ${comment.bbstitle } </a>
										</td>
										<td class="text-start align-content-center px-1">${comment.content }</td>
										<td class="align-content-center"><fmt:formatDate value="${comment.postdate}" pattern="yy.MM.dd."/></td>
									</tr>
		                        </c:forEach>
							</c:if>
						</tbody>
					</table>
					<!-- href='<c:url value="/member/delete"/>'  -->
					<a href='<c:url value="/bbs/mypage"/>' class="btn btn-sm btn-warning mb-2">뒤로가기</a>
					
				</div>
			</fieldset>
			<!-- 컨텐츠 끝 -->
			<jsp:include page="/WEB-INF/template/Footer.jsp"/>
			
		</div>
	</div>
</body>
</html>