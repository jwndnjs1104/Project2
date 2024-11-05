<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- boards 목록 -->
<div style="width: 250px; margin-top:42px">
	<c:if test="${nickname == '관리자' }">
		<div class="border border-2 rounded-3 p-2 mb-2">
			<label for="boardname" class="form-label fs-6">게시판 이름</label> <button class="btn btn-sm btn-success mb-1 mx-3 align-bottom" onclick="board()">등록</button>
			<input type="text" class="form-control form-control-sm" id="boardname" placeholder="게시판 이름 입력" name="boardname"
			value='${empty param.title?"":param.title }'/>
		</div>
	</c:if>
    <table class="table" >
        <thead class="table-success">
            <tr>
                <th class="text-center ">게시판 목록</th>
            </tr>
        </thead>
        <tr>
            <td>
                <div class="list-group list-group-flush" id="boardlist">
                	<a href='<c:url value="/bbs/list?boardid=0"/>' class="list-group-item list-group-item-action ">전체글보기</a>
                	<c:forEach items="${boards}" var="board" >
                		<c:if test="${board.boardname != '테스트게시판' }">
	                		<a href='<c:url value="/bbs/list?boardid=${board.boardid }"/>' class="list-group-item list-group-item-action ">${board.boardname }</a>
                		</c:if>
                	</c:forEach>
                </div>
            </td>
        </tr>
    </table>
</div>