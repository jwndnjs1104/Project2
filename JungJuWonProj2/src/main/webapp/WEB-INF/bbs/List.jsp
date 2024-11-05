<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/template/Configue.jsp"/>
	<title>List.jsp</title>
	<style>
    	@import url('<c:url value="/css/ContainerSize.css"/>');
    </style>
    <script src='<c:url value="/js/board.js"/>'></script>
	<script>
		window.addEventListener('DOMContentLoaded',function(){
			var noticeToggle = false;
			const hideNoticeCheckNode = document.querySelector('#hideNoticeCheck');
			
			hideNoticeCheckNode.addEventListener('click', function(){
				noticeToggle = !noticeToggle;
	        	if(noticeToggle)
	            	document.querySelector('#notice').classList.add("visually-hidden");
	        	else
	        		document.querySelector('#notice').classList.remove("visually-hidden");	
			});
		});
    </script>
</head>
<body>
	<div class="container">
		<div class="container-fluid">
            <jsp:include page="/WEB-INF/template/Header.jsp"/>
            
			<!-- 컨텐츠 시작 -->
			<jsp:include page="/WEB-INF/template/Jumbotron.jsp"/>
			
            <div class="d-flex h-100" style="font-size: .9em;">
            
            	<!-- boards 목록 -->
                <jsp:include page="/WEB-INF/template/Boards.jsp"/>
                
                <!-- bbs -->
                <div class="container-fluid" style="padding-right: 0;">
					<div class="d-flex justify-content-between mb-1">
						<div >
							<c:forEach items="${boards}" var="board" >
								<c:if test="${board.boardid == boardid }">
									<span class="fs-3 ">${board.boardname}</span>
								</c:if>
							</c:forEach>
							<c:if test="${boardid == 0}">
								<span class="fs-3 ">전체글보기</span>
							</c:if>
						</div>
						
						<div class="text-end align-content-end mx-1"> 
			                <label class="form-check-label mx-3 align-middle " style="padding-bottom: 2px">
			                    <input class="form-check-input " type="checkbox" id="hideNoticeCheck" name="remember"/> <span>공지 숨기기</span>
			                </label>
			                <c:if test="${nickname == '관리자' }">
				                <a href='<c:url value="/bbs/write?boardid=2"/>' class="btn btn-sm btn-warning">공지글 쓰기</a>
			                </c:if>
			                <c:if test="${!(boardid == 0 || boardid==2) }">
				                <a href='<c:url value="/bbs/write?boardid=${boardid }"/>' class="btn btn-sm btn-success">글쓰기</a>
			                </c:if>
						</div>
					</div>
					<div>
						<c:if test="${searchWord != null && searchWord != '' }">
							<span class="fs-5"> &nbsp;${searchColumn=='title'?'제목':searchColumn=='content'?'내용':'닉네임' } : '${searchWord}' &nbsp;검색결과 </span>
						</c:if>
					</div>
					
                    <table class="table table-hover text-center">
                        <thead class="table-success ">
                            <tr class="">
                                <th class="col-1 p-0 align-middle">번호</th>
                                <c:if test="${boardid==0 }">
                                	<th class="col-2 p-0 align-middle">게시판</th>
                                </c:if>
                                <th class="col-5 align-middle">제목</th>
                                <th class="col-1 text-start p-1 align-middle">작성자</th>
                                <th class="col-1 p-0 align-middle">작성일</th>
                                <th class="col-1 p-0 align-middle">조회</th>
                                <th class="col-1 p-0 align-middle">좋아요</th>
                            </tr>
                        </thead>
						
						<c:if test="${searchWord == null || searchWord == '' }">
							<tbody id="notice" > <!-- 공지글 -->
								<c:if test="${noticeBbs.size() == 0 }" var="isEmpty" >
									<tr>
		                                <td colspan="${boardid==0?7:6 }">공지글이 없습니다</td>
									</tr>
								</c:if>
								<c:if test="${!isEmpty }">
									<c:forEach items="${noticeBbs }" var="notice">
										<tr>
											<td class="bg-light text-danger fw-bold" >공지</td>
											<c:if test="${boardid==0 }"> <td class="bg-light text-danger fw-bold" >공지사항</td> </c:if>
											<td class="bg-light text-start fw-bold px-3">
												<a class="text-decoration-none text-danger" href='<c:url value="/bbs/view">
		                                														<c:param name="boardid" value="${notice.boardid}"/>
		                                														<c:param name="bbsid" value="${notice.bbsid}"/>
		                                														<c:param name="nowPage" value="${pagemap.nowPage}"/>
		                                														<c:param name="list" value="list"/>
		                                														<c:if test="${searchWord != null && searchWord != '' }">
		                                															<c:param name="searchWord" value="${searchWord}"/>
		                                															<c:param name="searchColumn" value="${searchColumn}"/>
		                                														</c:if>
		                                													</c:url>'>
		                                			${notice.title }
		                                			<c:if test="${notice.files != null }"> <i class="fa-solid fa-paperclip"></i></c:if> 
		                                			<c:if test="${notice.commentcount > 0 }" var="isNull"><span class="text-danger"> [${notice.commentcount }]</span></c:if>
		                                		</a>
											</td>
											<td class="bg-light text-start fw-bold align-middle p-1">관리자</td>
											<td class="bg-light"><fmt:formatDate value="${notice.postdate}" pattern="yy.MM.dd."/></td>
											<td class="bg-light">${notice.hitcount }</td>
											<td class="bg-light">${notice.likecount==null?0:notice.likecount }</td>
										</tr>
									</c:forEach>
								</c:if>
							</tbody>
						</c:if>

						
                        <tbody>
                        	<c:if test="${bbses.size() == 0 }" var="isEmpty_" >
	                            <tr>
	                                <td colspan="${boardid==0?7:6 }">등록된 글이 없습니다</td>
	                            </tr>
                            </c:if>
                            <c:if test="${!isEmpty_ }">
                            	<c:forEach items="${bbses }" var="bbs">
		                            <tr>
		                                <td class="align-middle" style="font-size: .8em">${bbs.bbsid }</td>
		                                <c:if test="${boardid==0 }">
		                                	<td class="text-start text-center">${bbs.boardname }</td>
		                                </c:if>
		                                <td class="text-start px-3">
		                                	<a class="text-decoration-none text-dark" href='<c:url value="/bbs/view">
		                                														<c:param name="boardid" value="${bbs.boardid}"/>
		                                														<c:param name="bbsid" value="${bbs.bbsid}"/>
		                                														<c:param name="nowPage" value="${pagemap.nowPage}"/>
		                                														<c:param name="list" value="list"/>
		                                														<c:if test="${searchWord != null && searchWord != '' }">
		                                															<c:param name="searchWord" value="${searchWord}"/>
		                                															<c:param name="searchColumn" value="${searchColumn}"/>
		                                														</c:if>
		                                													</c:url>'>
		                                		${bbs.title } 
			                                	<c:if test="${bbs.files != null }"> <i class="fa-solid fa-paperclip"></i></c:if> 
	                                			<c:if test="${bbs.commentcount > 0 }" var="isNull"><span class="text-danger"> [${bbs.commentcount }]</span></c:if>
                                			</a>
		                                </td>
		                                <td class="text-start align-middle p-1">${bbs.nickname }</td>
		                                <td><fmt:formatDate value="${bbs.postdate}" pattern="yy.MM.dd."/></td>
		                                <td>${bbs.hitcount }</td>
		                                <td>${bbs.likecount==null?0:bbs.likecount }</td>
		                            </tr>
	                            </c:forEach>
                            </c:if>
                        </tbody>
                        
                    </table>

                    <!-- 페이징 출력 -->
                    <div class="text-center text-decoration">${paging }</div>

                    <div>
                        <!-- 검색 UI -->
                        <form method="post" class="row justify-content-center mt-4" action="#" style="font-size: .9em;">
                        	<input type="hidden" value="${bbses[0].boardid!=null?bbses[0].boardid:boardid }" name="boardid" />
                            <div class="col-2">
                                <select class="form-select form-select-sm" name="searchColumn">
                                    <option value="title" <c:if test="${searchWord != '' && searchColumn=='title' }">selected</c:if> >제목</option>
                                    <option value="content" <c:if test="${searchWord != '' && searchColumn=='content' }">selected</c:if> >내용</option>
                                    <option value="nickname" <c:if test="${searchWord != '' && searchColumn=='nickname' }">selected</c:if> >닉네임</option>
                                </select>
                            </div>
                            
                            <div class="col-5">
                                <input type="text" class="form-control form-control-sm mx-1" placeholder="검색어를 입력하세요" name="searchWord" value="${searchWord!=null?searchWord:'' }"/>
                            </div>
                            
                            <div class="col-auto">
                                <button type="submit" class="btn btn-sm btn-success">검색</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
			<!-- 컨텐츠 끝 -->

			<jsp:include page="/WEB-INF/template/Footer.jsp"/>
		</div>
	</div>
</body>
</html>