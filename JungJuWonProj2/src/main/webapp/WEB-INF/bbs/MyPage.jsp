
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
					<h1 class="fs-3">${member.nickname }<small> 님 정보</small></h1>
					<a href='<c:url value="/bbs/mypageupdate"/>' class="btn btn-sm btn-warning mb-2">내 정보 수정</a>
					<table class="table table-hover text-center">
		                <thead class="table-success ">
							<tr class="">
		                        <th class="col-1">아이디</th>
		                        <th class="col-1">이름</th>
		                        <th class="col-2">닉네임</th>
		                        <th class="col-2">전화번호</th>
		                        <th class="col-1">이메일</th>
		                        <th class="col-1">가입일</th>
		                    </tr>
		                </thead>
		                <tbody>
		                	<tr>
		                		<td>${member.username }</td>
		                		<td>${member.name }</td>
		                		<td>${member.nickname }</td>
		                		<td>${member.phone }</td>
		                		<td>${member.email }</td>
		                		<td><fmt:formatDate value="${member.regidate }" pattern="yy.MM.dd."/></td>
		                	</tr>
		                </tbody>
					</table>
					
					
					<!-- 내 작성글 -->
					<div class="d-flex justify-content-between">
						<div class="d-flex">
							<h1 class="fs-3 mt-5">내 작성글 &nbsp;<span class="fs-5 ">${bbsAll.size()==0?'':bbsAll.size() }</span></h1>
						</div>
						<a href='<c:url value="/bbs/mypagebbscomments"/>' class="align-content-end text-decoration-none text-secondary py-1 " style="font-size:0.9em">더보기 ></a>
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
		                       	<c:forEach begin="0" end="${bbsAll.size()>5?4:bbsAll.size()-1 }" var="i">
									<tr>
										<td class="align-content-center p-0">${bbsAll[i].bbsid }</td>
										<td class="align-content-center">${bbsAll[i].boardname }</td>
										<td class="text-start">
											<a class="text-decoration-none text-dark" href='<c:url value="/bbs/view">
		                                														<c:param name="boardid" value="${bbsAll[i].boardid}"/>
		                                														<c:param name="bbsid" value="${bbsAll[i].bbsid}"/>
		                                														<c:param name="nowPage" value="1"/>
		                                													</c:url>'> 
												${bbsAll[i].title } 
												<c:if test="${!empty bbsAll[i].files}">
		                                			 <i class="fa-solid fa-paperclip"></i>
		                                		</c:if>
												<span class="text-danger">
													<c:if test="${bbsAll[i].commentcount !=0 }">[${bbsAll[i].commentcount }]</c:if>
												</span>
											</a>
										</td>
										<td><fmt:formatDate value="${bbsAll[i].postdate}" pattern="yy.MM.dd."/></td>
										<td>${bbsAll[i].hitcount }</td>
										<td>${bbsAll[i].likecount==null?0:bbsAll[i].likecount }</td>
									</tr>
		                        </c:forEach>
							</c:if>
						</tbody>
					</table>
					
					
					<!-- 내 댓글 -->
					<div class="d-flex justify-content-between">
						<h1 class="fs-3 mt-5">내 댓글 &nbsp;<span class="fs-5 ">${commentsAll.size()==0?'':commentsAll.size() }</span></h1>
						<a href="<c:url value="/bbs/mypagebbscomments"/>?mode=comments" style="font-size:0.9em" class="text-decoration-none text-secondary align-content-end py-1">더보기 ></a>
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
								<c:forEach begin="0" end="${commentsAll.size()>5?4:commentsAll.size()-1 }" var="i">
									<tr>
										<td class="align-content-center">${commentsAll[i].boardname}</td>
										<td class="align-content-center p-0">${commentsAll[i].bbsid}</td>
										<td class="text-start align-content-center px-1">
											<a class="text-decoration-none text-dark px-2" href='<c:url value="/bbs/view">
			                                														<c:param name="boardid" value="${commentsAll[i].boardid}"/>
			                                														<c:param name="bbsid" value="${commentsAll[i].bbsid}"/>
			                                														<c:param name="nowPage" value="1"/>
			                                													</c:url>'> ${commentsAll[i].bbstitle } </a>
										</td>
										<td class="text-start align-content-center px-1">${commentsAll[i].content }</td>
										<td class="align-content-center"><fmt:formatDate value="${commentsAll[i].postdate}" pattern="yy.MM.dd."/></td>
									</tr>
		                        </c:forEach>
							</c:if>
						</tbody>
					</table>
					
					
					<!-- href='<c:url value="/member/delete"/>'  -->
					<button type="button" class="btn btn-sm btn-dark mt-5 mb-2" data-bs-toggle="modal" data-bs-target="#myModal">회원 탈퇴</button>
				</div>
			</fieldset>
			<!-- 컨텐츠 끝 -->
			<jsp:include page="/WEB-INF/template/Footer.jsp"/>
			
			<div class="modal fade" id="myModal">
				<div class="modal-dialog">
					<div class="modal-content">
			
						<!-- Modal Header -->
						<div class="modal-header">
							<h4 class="modal-title"><i class="fa-solid fa-triangle-exclamation"></i> 회원 탈퇴</h4>
							<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
						</div>
				
						<!-- Modal body -->
						<div class="modal-body">
							정말로 탈퇴하시겠습니까?<br/>
							탈퇴하면 모든 게시물과 댓글이 사라집니다
				        	<button type="button" class="btn btn-danger mx-4 align-bottom"  data-bs-toggle="modal" data-bs-target="#signoutpassword">탈퇴하기</button>
						</div>
			
					</div>
				</div>
			</div>
			
			<div class="modal fade" id="signoutpassword">
				<div class="modal-dialog">
					<div class="modal-content">
			
						<!-- Modal Header -->
						<div class="modal-header">
							<h4 class="modal-title"><i class="fa-solid fa-triangle-exclamation"></i> 회원 탈퇴</h4>
							<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
						</div>
				
						<!-- Modal body -->
						<div class="modal-body">
							비밀번호를 입력해주세요<br/>
							확인버튼 클릭 즉시 회원정보와 모든 게시물, 댓글이 사라집니다<br/>
							이용해주셔서 감사합니다
							<form method="post" action="<c:url value='/members/signout'/>" >
								<div class=" my-2">
						    		<input type="password" class="form-control p-3" placeholder="비밀번호를 입력하세요" name="password"/>
								</div>
								<div >
						    		<button class="btn btn-warning w-100 p-3" >확인</button>
								</div>
							</form>
						</div>
			
					</div>
				</div>
			</div>
			
		</div>
	</div>
</body>
</html>