<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/template/Configue.jsp"/>
	<title>Edit.jsp</title>
	<style>
    	@import url("css/ContainerSize.css");
    </style>
</head>
<body>
	<div class="container">
		<div class="container-fluid">
			<jsp:include page="/WEB-INF/template/Header.jsp"/>

			<!-- 컨텐츠 시작 -->
			
			<div class="p-3 bg-warning text-white mb-5">
				<h1 class="text-center">회원제 게시판</h1>
			</div>
			
			
			<fieldset class="border rounded-2 p-4">
				<legend class="float-none w-auto px-3 fs-2">글 수정</legend>
				
				<c:if test="${!empty errorMsg }">
					<div >
						<div class="alert alert-success alert-dismissible my-2" id="errorMsg">
							<button type="button" class="btn-close" data-bs-dismiss="alert"></button>
							<strong>업로드 실패!</strong>
							${errorMsg }
						</div>
					</div>
				</c:if>
			
			
				<form action='<c:url value="/bbs/edit"/>' method="post" enctype="multipart/form-data">
					<input type="hidden" value="${dto.bbsid }" name="bbsid"/>
					<input type="hidden" value="${dto.boardid }" name="boardid"/>
					<input type="hidden" value="${nowPage }" name="nowPage"/>
					<c:if test="${searchWord != null }">
						<input type="hidden" value="${searchWord}" name="searchWord"/>
						<input type="hidden" value="${searchColumn }" name="searchColumn"/>
					</c:if>
					<div class=>
						<label for="title" class="form-label">제목</label>
						<input type="text" class="form-control" id="title" placeholder="제목을 입력하세요" name="title" value='${dto.title}'/>
					</div>
					
					<div class="my-3">
						<label for="files" class="form-label">추가할 파일</label>
						<input type="file" class="form-control" id="files" name="files" multiple="multiple">						
					</div>
					<c:if test="${dto.files != null }">
						<div class="my-3">
							<label class="form-label">기존 파일</label>
							<div class="d-flex">
								<c:forEach items="${fn:split(dto.files,',') }" var="file">
									<div class="alert alert-light alert-dismissible fade show mx-1">
										<button id="${file }" type="button" class="btn-close" data-bs-dismiss="alert"></button>
										${file }
										<input type="hidden" name="${file }" value="${file }">
									</div>
								</c:forEach>
							</div>
						</div>
					</c:if>
					
					<label for="content" class="form-label">내용</label>
					<textarea placeholder="내용을 입력하세요" class="form-control mb-3" rows="5" id="content" name="content">${dto.content}</textarea>
					
					<div class="d-flex justify-content-between">
						<button class="btn btn-sm btn-success"> 수정 </button>
						<a class="btn btn-sm btn-warning " href='<c:url value="/bbs/view">
																	<c:param name="bbsid" value="${dto.bbsid}"/>
																	<c:param name="boardid" value="${dto.boardid}"/>
																	<c:param name="nowPage" value="${nowPage}"/>
																	<c:if test="${searchWord != null }">
																		<c:param name="searchWord" value="${searchWord}"/>
																		<c:param name="searchColumn" value="${searchColumn}"/>
																	</c:if>
																</c:url>'>취소</a>
					</div>
				</form>
				
			</fieldset>
			<!-- 컨텐츠 끝 -->

			<jsp:include page="/WEB-INF/template/Footer.jsp" />
		</div>
	</div>
	<script>
		/*
			[0]은 첫번째 파일 의미
			파일 객체은 input type="file" 요소를 의미
		    파일 사이즈(바이트):파일객체(자스 DOM).files[0].size
			파일 명:파일객체(자스 DOM).files[0].name
			파일 컨텐츠 타입:파일객체(자스 DOM).files[0].type
		*/
		var files_ = document.querySelector('#files');
		files_.onchange=()=>{
			console.log(files_.files);
			console.log(files_.files.length == 0 ? "파일을 선택하지 않았어요":files_.files[0])
		};
		
		var form = document.querySelector('form[enctype="multipart/form-data"]');
		var totalSize = 0;
		form.onsubmit = function(){
			
			for(var i=0; i<files_.files.length; i++){
				console.log('파일명: %s, 크기: %s바이트, 컨텐츠 타입: %s',files_.files[i].name,files_.files[i].size,files_.files[i].type)
				totalSize+=files_.files[i].size;
				if(files_.files[i].size > 1024*2000){
					alert('파일 하나의 최대 업로드 용량(2MB)를 초과했어요');
					return false;
				}
			}
			if(totalSize > 1024*2000*5){
				alert('모든 파일의 최대 업로드 용량(10MB)를 초과했어요');
				return false;
			}
		};
	
	</script>
</body>
</html>