<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/template/Configue.jsp"/>
	<title>Home.jsp</title>
	<style>
    	@import url('<c:url value="/css/ContainerSize.css"/>');
    </style>
    <script src='<c:url value="/js/board.js"/>'></script>
</head>
<body>
	<div class="container">
		<div class="container-fluid" >
            <jsp:include page="/WEB-INF/template/Header.jsp"/>
            
			<!-- 컨텐츠 시작 -->
			<jsp:include page="/WEB-INF/template/Jumbotron.jsp"/>
			
            <div class="d-flex" style="height:100%; font-size: .9em;">
            
            	<!-- boards 목록 -->
            	<jsp:include page="/WEB-INF/template/Boards.jsp"/>
                
                <!-- 공지글 -->
                <div class="container-fluid" style="padding-right: 0;">
					<span class="fs-3 ">공지글</span>
					<table class="table table-hover text-center">
						<thead class="table-success">
                            <tr >
                                <th class="col-1 p-0 align-middle">번호</th>
                                <th class="col-6 align-middle">제목</th>
                                <th class="col-2 p-0 text-start align-middle p-0">작성자</th>
                                <th class="col-1 p-0 align-middle">작성일</th>
                                <th class="col-1 p-0 align-middle">조회</th>
                                <th class="col-1 p-0 align-middle">좋아요</th>
                            </tr>
                        </thead>
                        
						<c:if test="${noticeBbs.size() == 0 }" var="isEmpty" >
							<tr>
								<td colspan="6" >등록된 공지글이 없습니다.</td>
							</tr>
						</c:if>
                        
                        <c:if test="${!isEmpty }">
	                        <tbody id="notice" class="" >
		                        <c:forEach begin="0" end="${noticeBbs.size() }" step="1" items="${noticeBbs }" var="notice"> 
		                            <tr >
		                                <td class="bg-light text-danger fw-bold">공지</td>
		                                <td class="bg-light text-start fw-bold px-3">
		                                	<a class="text-decoration-none text-danger" href='<c:url value="/bbs/view">
		                                														<c:param name="boardid" value="${notice.boardid}"/>
		                                														<c:param name="bbsid" value="${notice.bbsid}"/>
		                                														<c:param name="nowPage" value="1"/>
		                                														<c:param name="list" value="list"/>
		                                													</c:url>'>
		                                		${notice.title }
		                                		<c:if test="${notice.files != null }"> <i class="fa-solid fa-paperclip"></i></c:if> 
	                                			<c:if test="${notice.commentcount > 0 }" var="isNull"><span class="text-danger"> [${notice.commentcount }]</span></c:if>
		                                	</a>
		                                </td>
		                                <td class="bg-light text-start fw-bold">관리자</td>
		                                <td class="bg-light"><fmt:formatDate value="${notice.postdate}" pattern="yy.MM.dd."/></td>
		                                <td class="bg-light">${notice.hitcount }</td>
		                                <td class="bg-light">${notice.likecount==null?0:notice.likecount }</td>
									</tr>
								</c:forEach>
	                        </tbody>
                        </c:if>
					</table>
					
					<!-- 전체글 -->
					<div class="d-flex justify-content-between h-auto">
						<div class="d-flex">
							<span class="fs-3 ">전체글보기</span>
						</div>
						<a href='<c:url value="/bbs/list?boardid=0"/>' class="align-content-end text-decoration-none text-secondary py-1 h-auto" style="font-size:0.9em">더보기 ></a>
					</div>
					<table class="table table-hover text-center">
						<thead class="table-success ">
                            <tr >
                                <th class="col-1 p-0 align-middle">번호</th>
                                <th class="col-2 p-0 align-middle">게시판</th>
                                <th class="col-5 align-middle">제목</th>
                                <th class="col-1 text-start p-0 align-middle">작성자</th>
                                <th class="col-1 p-0 align-middle">작성일</th>
                                <th class="col-1 p-0 align-middle">조회</th>
                                <th class="col-1 p-0 align-middle">좋아요</th>
                            </tr>
                        </thead>

                        <tbody>
                        	<c:if test="${totalBbs.size() == 0 }" var="isEmpty" >
	                            <tr>
	                                <td colspan="7">등록된 글이 없습니다.</td>
	                            </tr>
                            </c:if>
                            
                            <c:if test="${!isEmpty }">
                            	<c:forEach begin="0" end="${totalBbs.size()>15?14:totalBbs.size()-1 }" var="i">
		                            <tr>
		                                <td class="align-content-center" style="font-size: .8em">${totalBbs[i].bbsid }</td>
		                                <td class="p-0 align-content-center">${totalBbs[i].boardname }</td>
		                                <td class="text-start px-3 align-content-center">
		                                	<a class="text-decoration-none text-dark" href='<c:url value="/bbs/view">
		                                														<c:param name="boardid" value="${totalBbs[i].boardid}"/>
		                                														<c:param name="bbsid" value="${totalBbs[i].bbsid}"/>
		                                														<c:param name="nowPage" value="1"/>
		                                														<c:param name="list" value="list"/>
		                                													</c:url>'>
		                                		${totalBbs[i].title } 
		                                		<c:if test="${!empty totalBbs[i].files}">
		                                			 <i class="fa-solid fa-paperclip"></i>
		                                		</c:if> 
		                                		<span class="text-danger">
		                                			<c:if test="${totalBbs[i].commentcount != 0}" > [${totalBbs[i].commentcount }]</c:if>
		                                		</span>
		                                	</a>
		                                </td>
		                                <td class="text-start align-content-center p-0">${totalBbs[i].nickname }</td>
		                                <td><fmt:formatDate value="${totalBbs[i].postdate}" pattern="yy.MM.dd."/></td>
		                                <td>${totalBbs[i].hitcount }</td>
		                                <td>${totalBbs[i].likecount==null?0:totalBbs[i].likecount }</td>
		                            </tr>
	                            </c:forEach>
                            </c:if>
                        </tbody>
                    </table>

                    <!-- 페이징 출력 -->
					
                    <div style="font-size:.9em">
                        <!-- 검색 UI -->
                        <!-- 
                        <form method="post" class="row justify-content-center mt-4" action="#">
                            <div class="col-2">
                                <select class="form-select" name="searchColumn">
                                    <option value="title">제목</option>
                                    <option value="content">내용</option>
                                    <option value="nickname">닉네임</option>
                                </select>
                            </div>
                            
                            <div class="col-5">
                                <input type="text" class="form-control  mx-2" placeholder="검색어를 입력하세요" name="searchWord" />
                            </div>
                            
                            <div class="col-auto">
                                <button type="submit" class="btn btn-success">검색</button>
                            </div>
                        </form>
                        -->
                    </div>
                     
                </div>
            </div>
			<!-- 컨텐츠 끝 -->

			<jsp:include page="/WEB-INF/template/Footer.jsp"/>
		</div>
	</div>
</body>
</html>