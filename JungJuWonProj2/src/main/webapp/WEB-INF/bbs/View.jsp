<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/template/Configue.jsp"/>
	<title>View.jsp</title>
	<style>
	
    	@import url('<c:url value="/css/ContainerSize.css"/>');
    
		li:hover{
			text-decoration: underline;
		}
	</style>
	
	<!-- 댓글관련 함수 모음 -->
	<script src='<c:url value="/js/comment.js"/>'></script>
	<script src='<c:url value="/js/like.js"/>'></script>
	
	<script>
		window.addEventListener('DOMContentLoaded', function(){
			const like = document.querySelector('#like');
			const likecount = document.querySelector('#likecount');
			
			//댓글입력 함수 버튼에 등록
			const writecommentBtn = document.querySelector('#writeComment');
			writecommentBtn.onclick = writecomment; 
			
			//view 페이지 로드시 사용자 좋아요 유무에 따른 상태 변경
			if(${islike}){
				like.classList.remove('fa-regular');
				like.classList.add('fa-solid');
			}////if
		});
	</script>
</head>
<body>
	<div class="container">
		<div class="container-fluid">
            <jsp:include page="/WEB-INF/template/Header.jsp"/>
            
			<!-- 컨텐츠 시작 -->
			<jsp:include page="/WEB-INF/template/Jumbotron.jsp"/>
	
	
            <div class="d-flex" style="height:100%; font-size: .9em;">
				<!-- 게시판 메뉴 목록 -->
				<jsp:include page="/WEB-INF/template/Boards.jsp"/>
                
                
                <!-- @@@@@@@@@ 게시판 @@@@@@@@@ -->
                <div class="container-fluid" style="padding-right: 0;">
                    <!-- view -->
                    <table class="table mb-5">
						<tbody>
							<tr>
								<td class="fs-3" colspan="4">${dto.boardname }</td>
								<td class="col-2 text-end align-bottom" colspan="2"><a href='<c:url value="/bbs/list">
																								<c:param name="boardid" value="${dto.boardid}"/>
																								<c:param name="nowPage" value="${nowPage}"/>
																								<c:if test="${searchWord != null && searchWord != '' }">
																									<c:param name="searchWord" value="${searchWord}"/>
																									<c:param name="searchColumn" value="${searchColumn}"/>
																								</c:if>
																							</c:url>' class="btn btn-sm btn-success">목록</a></td>
							</tr>
							<tr>
								<th class="table-success col-1 text-center align-content-center">제목</th>
								<td class=" fs-4" colspan="5">${dto.title }</td>
							</tr>
							<tr >
								<th class="table-success col-1 text-center">작성자</th>
								<td class="col-4 fw-bold">${dto.nickname }</td>
								<th class="table-success col-1 text-center">작성일</th>
								<td class="col-4"><fmt:formatDate value="${dto.postdate}" pattern="yy.MM.dd HH:mm"/></td>
								<th class="table-success col-1 text-center">조회수</th>
								<td class="col-1">${dto.hitcount }</td>
							</tr>
							<tr>
								<th class="table-success col-1 text-center align-middle px-0">첨부파일</th>
								<td class="col-11 align-middle" colspan="5">
									<ul class="list-unstyled d-flex">
										<c:if test="${dto.files != null }">
											<c:forEach var="file" items="${fn:split(dto.files,',') }">
												<li class="mx-2"><a href='<c:url value="/bbs/download?filename=${file }"/>' class="text-decoration-none text-dark mt-1 down-file${loop.count }">${file } <i class="fa-solid fa-floppy-disk"></i></a></li>
											</c:forEach>
										</c:if>
									</ul>
								</td>
							</tr>
							<tr><th class="table-success text-center" colspan="6">내용</th></tr>
							<tr>
								<td colspan="6" class="p-4">
									${dto.content }
								</td>
							</tr>
						</tbody>
					</table>
					
					
					<div class="d-flex my-0">
						<div class="fw-bold ">
							<i class="fa-regular fa-heart text-danger fs-6" id="like" onclick="like()"></i> 좋아요 <span id="likecount">${likecount }</span><br/>
						</div>
						<div class="fw-bold mx-3"><i class="fa-regular fa-comment-dots fs-6"></i> 댓글 <span id="commentcount">${dto.commentcount }</span></div>
					</div>
					<hr/>
					
					
					
					<table class="table">
						<tbody id="commentTable">
							<c:if test="${cList.size() != 0 }">
								<c:forEach var="comment" items="${cList }">
									<tr id="${comment.commentid }">
										<td class="col-1 fs-3"><i class="fa-regular fa-circle-user"></i></td>
										<td class="col-8" >
											<span class="fw-bold">${comment.nickname }</span><br/>
											<span>${comment.content }</span><br/>
											<span class="text-secondary"><fmt:formatDate value="${comment.postdate}" pattern="yy.MM.dd HH:mm"/></span>
										</td>
										<td class="col-3 text-end align-content-end">
											<c:if test="${comment.nickname == nickname }">
												<a href="#" class="text-decoration-none text-secondary" onclick="editcomment(event)">수정</a>&nbsp;
												<a href="#" class="text-decoration-none text-secondary" onclick="deletecomment(event)">삭제</a>
											</c:if>
										</td>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
					
					<form class="mb-5" id="commentForm" action="javascript:writecomment(event)">
						<fieldset class="border border-2 rounded-2 p-2">
							<span class="fw-bold mx-2"> ${nickname }</span>
							<input type="hidden" value="${nickname }" name="nickname"/>
							<input type="hidden" value="${dto.bbsid }" name="bbsid"/>
							<input type="text" class="form-control form-control-sm my-2" id="comment" name="comment" placeholder="댓글을 입력하세요" />
							<a class="btn btn-sm btn-success align-content-center" id="writeComment">입력</a>
						</fieldset>
					</form>
					
					
					<!-- 이전글/다음글 -->
					<div>
						<table class="table border-top border-bottom">
							<tbody>
								<tr>
									<th class="table-success text-center col-1">다음글</th>
									<c:if test="${prevNext.next != null }" var="existNext">
										<td class="col-7">
											<a class="text-decoration-none text-dark px-4" href='<c:url value="/bbs/view">
																									<c:param name="bbsid" value="${prevNext.next.bbsid}"/>
																									<c:param name="boardid" value="${prevNext.next.boardid}"/>
																									<c:param name="nowPage" value="${nowPage}"/>
			                                														<c:if test="${searchWord != null && searchWord != '' }">
			                                															<c:param name="searchWord" value="${searchWord}"/>
			                                															<c:param name="searchColumn" value="${searchColumn}"/>
			                                														</c:if>
			                                													</c:url>&'>
												${prevNext.next.title } 
												<c:if test="${prevNext.next.files != null }"> <i class="fa-solid fa-paperclip"></i></c:if>
												<c:if test="${prevNext.next.commentcount > 0 }" var="isNull"><span class="text-danger"> [${prevNext.next.commentcount }]</span></c:if>
											</a>
										</td>
										<td class="col-2 fw-bold">${prevNext.next.nickname }</td>
										<td class="col-2"><fmt:formatDate value="${prevNext.next.postdate}" pattern="yy.MM.dd HH:mm"/></td>
									</c:if>
									<c:if test="${!existNext }">
										<td class="px-4" colspan="3"> &nbsp;다음글이 없습니다</td>
									</c:if>
								</tr>
								<tr>
									<th class="table-success text-center col-1" style="width: 7%">이전글</th>
									<c:if test="${prevNext.prev != null }" var="existPrev">
										<td class="col-7">
											<a class="text-decoration-none text-dark px-4" href='<c:url value="/bbs/view">
																									<c:param name="bbsid" value="${prevNext.prev.bbsid}"/>
																									<c:param name="boardid" value="${prevNext.prev.boardid}"/>
																									<c:param name="nowPage" value="${nowPage}"/>
			                                														<c:if test="${searchWord != null && searchWord != '' }">
			                                															<c:param name="searchWord" value="${searchWord}"/>
			                                															<c:param name="searchColumn" value="${searchColumn}"/>
			                                														</c:if>
			                                													</c:url>&'>
												${prevNext.prev.title } 
												<c:if test="${prevNext.prev.files != null }"> <i class="fa-solid fa-paperclip"></i></c:if>
												<c:if test="${prevNext.prev.commentcount > 0 }" var="isNull"><span class="text-danger"> [${prevNext.prev.commentcount }]</span></c:if>
											</a>
										</td>
										<td class="col-2 fw-bold">${prevNext.prev.nickname }</td>
										<td class="col-2"><fmt:formatDate value="${prevNext.prev.postdate}" pattern="yy.MM.dd HH:mm"/></td>
									</c:if>
									<c:if test="${!existPrev}">
										<td class="px-4" colspan="3"> &nbsp;이전글이 없습니다</td>
									</c:if>
								</tr>
							</tbody>
						</table>
					</div>
				
					<!-- 수정/삭제/목록 컨트롤 버튼 -->
					<div class="text-center">
						<c:if test="${dto.nickname == nickname || nickname=='관리자' }">
							<a href='<c:url value="/bbs/edit">
										<c:param name="bbsid" value="${dto.bbsid}"/>
										<c:param name="nowPage" value="${nowPage}"/>
										<c:if test="${searchWord != null }">
											<c:param name="searchWord" value="${searchWord}"/>
											<c:param name="searchColumn" value="${searchColumn}"/>
										</c:if>
									</c:url>' class="btn btn-sm btn-success">수정</a>
							<button class="btn btn-sm btn-success mx-1" data-bs-toggle="modal" data-bs-target="#myModal">삭제</button>
						</c:if>
						<a href='<c:url value="/bbs/list">
									<c:param name="boardid" value="${dto.boardid}"/>
									<c:param name="nowPage" value="${nowPage}"/>
									<c:if test="${searchWord != null && searchWord != '' }">
										<c:param name="searchWord" value="${searchWord}"/>
										<c:param name="searchColumn" value="${searchColumn}"/>
									</c:if>
								</c:url>' class="btn btn-sm btn-success">목록</a>
					</div>
					
				</div>

			</div>
			<!-- 컨텐츠 끝 -->
			<div class="modal fade" id="myModal">
				<div class="modal-dialog modal-sm">
					<div class="modal-content">
						<!-- Modal Header -->
						<div class="modal-header">
							<h4 class="modal-title">삭제 확인</h4>
							<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
						</div>
						<!-- Modal body -->
						<div class="modal-body d-flex justify-content-between">
							정말로 삭제하시겠습니까?
							<a class="btn btn-sm btn-danger" href='<c:url value="/bbs/delete">
																		<c:param name="bbsid" value="${dto.bbsid}"/>
																		<c:param name="boardid" value="${dto.boardid}"/>
																		<c:param name="nowPage" value="${nowPage}"/>
																		<c:if test="${searchWord != null }">
																			<c:param name="searchWord" value="${searchWord}"/>
																			<c:param name="searchColumn" value="${searchColumn}"/>
																		</c:if>
																	</c:url>'>확인</a>
						</div>
					</div>
				</div>
			</div>
			
			<jsp:include page="/WEB-INF/template/Footer.jsp"/>
		</div>
	</div>
</body>
</html>