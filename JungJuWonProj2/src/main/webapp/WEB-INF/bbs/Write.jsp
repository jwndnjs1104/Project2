<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/template/Configue.jsp"/>
	<title>Write.jsp</title>
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
			
			
			<fieldset class="border rounded-2 p-4">
				<legend class="float-none w-auto px-3 fs-2">글 작성</legend>
				
				<c:if test="${!empty errorMsg }">
					<div >
						<div class="alert alert-success alert-dismissible my-2" id="errorMsg">
							<button type="button" class="btn-close" data-bs-dismiss="alert"></button>
							<strong>업로드 실패!</strong>
							${errorMsg }
						</div>
					</div>
				</c:if>
			
			
				<form action='<c:url value="/bbs/write"/>' method="post" enctype="multipart/form-data">
					<input type="hidden" value="${param.boardid }" name="boardid"/>
					<div class=>
						<label for="title" class="form-label">제목</label>
						<input type="text" class="form-control" id="title" placeholder="제목을 입력하세요" name="title"
						value='${empty param.title?"":param.title }'/>
					</div>
					
					<div class="my-3">
						<label for="files" class="form-label">자료파일</label>
						<input type="file" class="form-control" id="files" name="files" multiple>
					</div>
					
					<label for="content" class="form-label">내용</label>
					<textarea placeholder="내용을 입력하세요" class="form-control mb-3" rows="5" id="content" name="content">${empty param.content?"":param.content }</textarea>
					
					<button class="btn btn-success mt-2">등록</button>
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