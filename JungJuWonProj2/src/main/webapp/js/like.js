//좋아요
function like(){
	const like = document.querySelector('#like');
	const commentform = document.querySelector('#commentForm');
	const bbsid = commentform.bbsid.value;
	
	if(like.classList.contains('fa-regular')){
		like.classList.remove('fa-regular');
		like.classList.add('fa-solid');
		likecount.textContent=Number(likecount.textContent)+1;
		fetch('/JungJuWonProj2/like',{method:'post',body:JSON.stringify({'bbsid':bbsid,'type':'insert'}),headers:{'Content-Type':'application/json'}})
		.catch(err=>console.log(err));
	}/////
	else if(like.classList.contains('fa-solid')){
		like.classList.remove('fa-solid');
		like.classList.add('fa-regular');
		likecount.textContent=Number(likecount.textContent)-1;
		fetch('/JungJuWonProj2/like',{method:'post',body:JSON.stringify({'bbsid':bbsid,'type':'delete'}),headers:{'Content-Type':'application/json'}})
		.catch(err=>console.log(err));
	}/////
};/////////////