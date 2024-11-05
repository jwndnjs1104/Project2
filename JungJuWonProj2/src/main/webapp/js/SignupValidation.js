/**
 * 
 */

 window.addEventListener('DOMContentLoaded', function() {
	//닉네임 유효성 검사
	const username = document.querySelector('#username');
	username.onblur = async function() {
		let respData='';
		if(username.value != '') {
			await fetch('/JungJuWonProj2/checkform',{method:'post',body:JSON.stringify({'username':username.value}), headers:{'Content-Type':'application/json'}})
			.then(resp=>resp.json())
			.then(data=>{
				respData=data.existUsername;
				console.log('data:',data); /////////////
			});
		}
		
		if((/^[a-zA-Z0-9]{3,20}$/.test(this.value)) && (respData === 'false')) {
			this.classList.remove("border-danger");
			this.nextElementSibling.innerHTML='아이디 <i class="fa-solid fa-check" style="color: #63E6BE;"></i>';
		}
		else{
			this.classList.add("border-danger");
			this.nextElementSibling.nextElementSibling.textContent="사용불가능한 아이디입니다"
			this.nextElementSibling.textContent="아이디*";
		}
	};
	username.onfocus=function() {
		this.classList.remove("border-danger");
		this.nextElementSibling.textContent="아이디 - 영문/숫자 3~20자리";
		this.nextElementSibling.nextElementSibling.innerHTML="&nbsp;"
	};
	
	
	//비밀번호 유효성 검사
	const password = document.querySelector('#password');
	const passwordComfirm = document.querySelector('#passwordConfirm');
	password.onblur=function(){
		if(!(/^[a-zA-Z0-9]{4,20}$/.test(this.value))){
			this.classList.add("border-danger");
			this.nextElementSibling.nextElementSibling.textContent="영문/숫자 4~20자리를 입력해주세요"
			this.nextElementSibling.textContent="비밀번호*";
		}
		else {
			this.classList.remove("border-danger");
			this.nextElementSibling.innerHTML='비밀번호 <i class="fa-solid fa-check" style="color: #63E6BE;"></i>';
		}
	};
	password.onfocus=function(){
		this.classList.remove("border-danger");
		passwordComfirm.classList.remove("border-danger");
		this.nextElementSibling.textContent="비밀번호 - 영문/숫자 4~20자리";
		this.nextElementSibling.nextElementSibling.innerHTML="&nbsp;"
		passwordComfirm.nextElementSibling.nextElementSibling.innerHTML="&nbsp;"
	};
	passwordComfirm.onblur=function(){
		if(this.value != password.value){
			this.classList.add("border-danger");
			this.nextElementSibling.nextElementSibling.textContent="비밀번호가 일치하지 않습니다"
			this.nextElementSibling.textContent="비밀번호 확인";
		}
		else if((this.value == password.value) && (/^[a-zA-Z0-9]{4,20}$/.test(this.value))) {
			this.classList.remove("border-danger");
			this.nextElementSibling.innerHTML='비밀번호 확인 <i class="fa-solid fa-check" style="color: #63E6BE;"></i>';
			this.nextElementSibling.nextElementSibling.innerHTML="&nbsp;"
		}
	};
	passwordComfirm.onfocus=function(){
		if(!(/^[a-zA-Z0-9]{4,20}$/.test(password.value))){
			this.classList.add("border-danger");
			this.nextElementSibling.nextElementSibling.textContent="비밀번호 먼저 설정해주세요"
		}
	};
	
	
	//닉네임 유효성 검사
	const nickname = document.querySelector('#nickname');
	nickname.onblur = async function() {
		if(this.value != ''){
			await fetch('/JungJuWonProj2/checkform',{method:'post',body:JSON.stringify({'nickname':nickname.value}), headers:{'Content-Type':'application/json'}})
			.then(resp=>resp.json())
			.then(data=>{
				console.log('data: ',data);
				respData=data.existNickname;});
		}
		console.log(respData);
		if(this.value==''){
			this.classList.remove("border-danger");
			this.nextElementSibling.textContent="닉네임";
			this.nextElementSibling.nextElementSibling.innerHTML="&nbsp;"
		}
		else if((/^[가-힣a-zA-Z0-9]{2,8}$/.test(this.value)) && respData === 'false'){
			this.classList.remove("border-danger");
			this.nextElementSibling.innerHTML='닉네임 <i class="fa-solid fa-check" style="color: #63E6BE;"></i>';
		}
		else {
			this.classList.add("border-danger");
			this.nextElementSibling.nextElementSibling.textContent="사용불가능한 닉네임입니다"
			this.nextElementSibling.textContent="닉네임";
		}
	};
	nickname.onfocus=function() {
		this.classList.remove("border-danger");
		this.nextElementSibling.textContent="닉네임 - 한글/영문/숫자 2~8자, 미입력시 아이디로 등록됩니다";
		this.nextElementSibling.nextElementSibling.innerHTML="&nbsp;"
	};
	
	
	//이름 유효성 검사
	const name = document.querySelector('#name');
	name.onblur = function() {
		if(!(/^[가-힣a-zA-Z]{2,10}$/.test(this.value))){
			this.classList.add("border-danger");
			this.nextElementSibling.nextElementSibling.textContent="필수항목입니다"
			this.nextElementSibling.textContent="이름*";
		}
		else {
			this.classList.remove("border-danger");
			this.nextElementSibling.innerHTML='이름 <i class="fa-solid fa-check" style="color: #63E6BE;"></i>';
		}
	};
	name.onfocus=function() {
		this.classList.remove("border-danger");
		this.nextElementSibling.textContent="이름 - 한글/영문 최대 10자까지 입력 가능합니다";
		this.nextElementSibling.nextElementSibling.innerHTML="&nbsp;"
	};
	
	
	//전화번호 유효성 검사
	const phone1 = document.querySelector('#phone1');
	const phone2 = document.querySelector('#phone2');
	phone2.onblur = function() {
		if(!(/^[0-9]{4}$/.test(this.value)) || !(/^[0-9]{4}$/.test(phone1.value))){
			this.parentElement.nextElementSibling.textContent="휴대폰번호를 정확히 입력해주세요"
				this.parentElement.previousElementSibling.previousElementSibling.innerHTML='휴대폰 번호*'
		}
		else if((/^[0-9]{4}$/.test(this.value)) && (/^[0-9]{4}$/.test(phone1.value))){
			this.parentElement.previousElementSibling.previousElementSibling.innerHTML='휴대폰 번호 <i class="fa-solid fa-check" style="color: #63E6BE;"></i>';
		}
	};
	phone1.onfocus=function() {
		this.parentElement.nextElementSibling.innerHTML="&nbsp;"
	};
	phone2.onfocus=function() {
		this.parentElement.nextElementSibling.innerHTML="&nbsp;"
	};
	
	
	//이메일 유효성 검사
	const email = document.querySelector('#email');
	email.onblur = function() {
		if(!(/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(this.value))){
			this.classList.add("border-danger");
			this.nextElementSibling.nextElementSibling.textContent="사용불가능한 이메일입니다"
			this.nextElementSibling.textContent="이메일*";
		}
		else {
			this.classList.remove("border-danger");
			this.nextElementSibling.innerHTML='이메일 <i class="fa-solid fa-check" style="color: #63E6BE;"></i>';
		}
	};
	email.onfocus=function() {
		this.classList.remove("border-danger");
		this.nextElementSibling.textContent="이메일*";
		this.nextElementSibling.nextElementSibling.innerHTML="&nbsp;"
	};
	
	document.querySelector('#username').focus();
});
		
		function isValidate(e,form) {
			const nickname = document.querySelector('#nickname');
			const iNodes = document.querySelectorAll('i[class="fa-solid fa-check"]');
			console.log(iNodes);
			if(nickname.value==''){
				if(iNodes.length == 6) form.submit();
			}
			else if(iNodes.length == 7) form.submit();
			document.querySelector('#username').focus();
			document.querySelector('#password').focus();
			document.querySelector('#name').focus();
			document.querySelector('#phone2').focus();
			document.querySelector('#email').focus();
			document.querySelector('#email').blur();
		};