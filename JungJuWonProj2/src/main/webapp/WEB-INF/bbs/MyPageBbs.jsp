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
					<!-- 내 작성글 -->
					<div class="d-flex justify-content-between">
						<h1 class="fs-3 mt-3">내 작성글</h1>
					</div>
					<table class="table table-hover text-center">
		                <thead class="table-success ">
		                    <tr class="">
		                        <th class="col-1">글번호</th>
		                        <th class="col-2">게시판</th>
		                        <th class="col-5  px-2">글 제목</th>
		                        <th class="col-2">작성일</th>
		                        <th class="col-1">조회</th>
		                        <th class="col-1">좋아요</th>
		                    </tr>
		                </thead>
		                
						<tbody>
	                   		<c:if test="${bbsAll.size() == 0 }" var="isEmpty" >
								<tr>
	                            	<td colspan="6">등록된 글이 없습니다.</td>
	                        	</tr>
							</c:if>
							<c:if test="${!isEmpty }">
								<%-- 
		                       	<c:forEach begin="0" end="${bbsAll.size()>5?4:bbsAll.size()-1 }" var="i">
		                       	--%>
		                       	<c:forEach items="${bbsAll }" var="bbs">
									<tr>
										<td class="align-content-center p-0">${bbs.bbsid }</td>
										<td class="align-content-center">${bbs.boardname }</td>
										<td class="text-start">
											<a class="text-decoration-none text-dark" href='<c:url value="/bbs/view"/>?boardid=${bbs.boardid}&bbsid=${bbs.bbsid}'> ${bbs.title } 
												<c:if test="${!empty bbs.files}">
		                                			 <i class="fa-solid fa-paperclip"></i>
		                                		</c:if> 
												<span class="text-danger">
													<c:if test="${bbs.commentcount !=0 }"> [${bbs.commentcount }]</c:if>
												</span>
											</a>
										</td>
										<td><fmt:formatDate value="${bbs.postdate}" pattern="yy.MM.dd."/></td>
										<td>${bbs.hitcount }</td>
										<td>${bbs.likecount==null?0:bbs.likecount }</td>
									</tr>
		                        </c:forEach>
							</c:if>
						</tbody>
					</table>
					
					<a href='<c:url value="/bbs/mypage"/>' class="btn btn-sm btn-warning mb-2">뒤로가기</a>
				</div>
			</fieldset>
			<jsp:include page="/WEB-INF/template/Footer.jsp"/>
			
		</div>
	</div>
</body>
</html>