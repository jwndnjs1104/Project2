async function writecomment(e) {
	const commentForm = document.querySelector('#commentForm');
	const formData = new FormData(commentForm); //FormData 객체에 form 객체 넣어 데이터 생성
	const json = Object.fromEntries(formData); //json 형식으로 변경
	const commentBtn = document.querySelector('#writeComment');
	console.log('입력버튼:',commentBtn.textContent);
	
	if(commentBtn.textContent==='입력'){ //댓글 입력
		//빈 문자열일때 알림창
		if(commentForm.comment.value === ''){ 
			alert('댓글을 입력해주세요');
		}
		else{
			const tbodyNode = document.querySelector('#commentTable');
			const tr = document.createElement('tr');
			const today2 = new Date();
	        const date = today2.toLocaleDateString()+' '+today2.getHours()+':'+today2.getMinutes();
	        
	        
			await fetch('/JungJuWonProj2/comment',{method:'post', body:JSON.stringify(json), headers:{'Content-Type':'application/json'}})
			.then(resp=>resp.json())
			.then(data=>{
				console.log(data);
				console.log(data.commentid);
				tr.id = data.commentid;
			})
			.catch(err=>console.log(err));
			
			//눈속임용 자스로 댓글 넣기
			tr.innerHTML='<td class="col-1 fs-3"><i class="fa-regular fa-circle-user"></i></td>'
				+'<td class="col-8" ><span class="fw-bold">'+commentForm.nickname.value+'</span><br/><span>'
				+commentForm.comment.value+'</span><br/><span class="text-secondary">'
				+date
				+'</span></td><td class="col-3 text-end align-content-end"><a href="#" class="text-decoration-none text-secondary" onclick="editcomment(event)">수정 </a>'
				+'&nbsp;<a href="#" class="text-decoration-none text-secondary" onclick="deletecomment(event)">삭제</a></td>';
			tbodyNode.prepend(tr);
			let commentcount =document.querySelector('#commentcount');
			commentcount.textContent=Number(commentcount.textContent)+1;
			document.querySelector('#comment').value='';	
		}
	}
	else{ //댓글 수정
		//빈 문자열일때 알림창
		if(commentForm.comment.value === ''){ 
			alert('댓글을 입력해주세요');
		}
		else{
			//댓글번호 얻기
			const commentid = e.target.nextElementSibling.name;
			console.log(commentid);
			const url = '/JungJuWonProj2/comment?commentid='+commentid;
			
			//컨트롤러로 JSON형식 데이터 보내기(쿼리스트링으로 댓글번호 보내기, 나머지 JSON에는 댓글내용, 게시글번호 들어있음)
			await fetch(url, {method:'post', body:JSON.stringify(json), headers:{'Content-Type':'application/json'}})
			.then(resp=>resp.json())
			.then(data=>console.log(data))
			.catch(err=>console.log(err));
			
			//눈속임용 수정, 내용만 수정처리하면 됨
			const temp = 'tr[id="'+commentid+'"]'
			document.querySelector(temp).children[1].children[2].textContent=commentForm.comment.value;
			e.target.textContent="입력";
			e.target.nextElementSibling.remove();
		}
	}
};///////


//댓글 삭제
function deletecomment(e){
	if(confirm('정말 삭제하시겠습니까?')){
		e.target.parentElement.parentElement.remove();
		const commentcount = document.querySelector('#commentcount');
		commentcount.textContent = Number(commentcount.textContent)-1; 
		const commentid = e.target.parentElement.parentElement.id;
		
		let url = '/JungJuWonProj2/comment?mode=delete&commentid='+commentid;
		fetch(url);
	}
}//////////


///수정버튼 누르면 댓글 입력창 바꾸기
function editcomment(e){
	//수정 누른 댓글 내용
	const content = e.target.parentElement.previousElementSibling.children[2].textContent;
	//수정 누른 댓글번호
	const commentid = e.target.parentElement.parentElement.id;
	const commentBtn = document.querySelector('#writeComment');
	const commentInput = document.querySelector('#comment');
	const commentForm = document.querySelector('#commentForm');
	
	//입력버튼을 수정으로 바꾸기
	commentBtn.textContent = '수정';
	
	//입력창에 수정내용 뿌리기, 포커스 주기
	commentInput.value = content;
	commentInput.focus();
	
	if(document.querySelector('#cancelbtn') == null){
		//취소버튼 붙이기
		const cancelbtn = document.createElement('input');
		cancelbtn.onclick=editCommentCancel;
		cancelbtn.type="button";
		cancelbtn.value="취소";
		cancelbtn.classList.add("btn");
		cancelbtn.classList.add("btn-sm");
		cancelbtn.classList.add("btn-danger");
		cancelbtn.name=commentid;
		cancelbtn.id='cancelbtn';
		commentForm.firstElementChild.append(cancelbtn);
	}
};/////


//댓글 입력창 밑에 만들어진 취소버튼 기능
function editCommentCancel(){
	const commentBtn = document.querySelector('#writeComment');
	const commentInput = document.querySelector('#comment');
	
	commentBtn.textContent="입력";
	commentInput.value="";
	commentBtn.nextElementSibling.remove();
};/////