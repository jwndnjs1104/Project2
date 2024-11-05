

async function board(){
	const boardnameNode = document.querySelector('#boardname');
	const boardname =  boardnameNode.value;
	const url = '/JungJuWonProj2/board?boardname='+boardname;
	
	const boardlist = document.querySelector('#boardlist');
	const a = document.createElement('a');
	await fetch(url)
		.then(resp=>resp.json())
		.then(data=>{
			console.log(data);
			a.href='/JungJuWonProj2/bbs/list?boardid='+data.boardid;
			a.classList.add("list-group-item");
			a.classList.add("list-group-item-action");
			a.innerHTML=boardname;
			boardlist.append(a);
		})
		.catch(err=>console.log(err));
};/////////////