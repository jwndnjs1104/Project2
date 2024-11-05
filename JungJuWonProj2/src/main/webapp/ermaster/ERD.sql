
/* Drop Tables */

DROP TABLE comments CASCADE CONSTRAINTS;
DROP TABLE likes CASCADE CONSTRAINTS;
DROP TABLE bbs CASCADE CONSTRAINTS;
DROP TABLE boards CASCADE CONSTRAINTS;
DROP TABLE members CASCADE CONSTRAINTS;

drop sequence seq_bbs;
drop sequence seq_comments;
drop sequence seq_likes;
drop sequence seq_boards;


/* Create Tables */

CREATE TABLE bbs
(
	bbsid number NOT NULL,
	boardid number NOT NULL,
	username varchar2(20) NOT NULL,
	title nvarchar2(50) NOT NULL,
	content nvarchar2(2000),
	postdate timestamp DEFAULT SYSDATE,
	hitcount number DEFAULT 0,
	files nvarchar2(1000),
	PRIMARY KEY (bbsid)
);


CREATE TABLE boards
(
	boardid number NOT NULL,
	boardname nvarchar2(20) NOT NULL,
	PRIMARY KEY (boardid)
);


CREATE TABLE comments
(
	commentid number NOT NULL,
	bbsid number NOT NULL,
	username varchar2(20) NOT NULL,
	content nvarchar2(100) NOT NULL,
	postdate timestamp DEFAULT SYSDATE NOT NULL,
	PRIMARY KEY (commentid)
);


CREATE TABLE likes
(
	likeid number NOT NULL,
	bbsid number NOT NULL,
	username varchar2(20) NOT NULL,
	PRIMARY KEY (likeid)
);


CREATE TABLE members
(
	username varchar2(20) NOT NULL,
	password varchar2(20) NOT NULL,
	name nvarchar2(10) NOT NULL,
	nickname nvarchar2(8) UNIQUE,
	phone varchar2(11) NOT NULL UNIQUE,
	email varchar2(30) NOT NULL UNIQUE,
	regidate timestamp DEFAULT SYSDATE,
	PRIMARY KEY (username)
);



/* Create Foreign Keys */

ALTER TABLE comments
	ADD FOREIGN KEY (bbsid)
	REFERENCES bbs (bbsid)
	ON DELETE CASCADE
;


ALTER TABLE likes
	ADD FOREIGN KEY (bbsid)
	REFERENCES bbs (bbsid)
	ON DELETE CASCADE
;


ALTER TABLE bbs
	ADD FOREIGN KEY (boardid)
	REFERENCES boards (boardid)
	ON DELETE CASCADE
;


ALTER TABLE bbs
	ADD FOREIGN KEY (username)
	REFERENCES members (username)
	ON DELETE CASCADE
;


ALTER TABLE comments
	ADD FOREIGN KEY (username)
	REFERENCES members (username)
	ON DELETE CASCADE
;


ALTER TABLE likes
	ADD FOREIGN KEY (username)
	REFERENCES members (username)
	ON DELETE CASCADE	
;


create sequence seq_bbs nocache nocycle;
create sequence seq_comments nocache nocycle;
create sequence seq_likes nocache nocycle;
create sequence seq_boards nocache nocycle;

insert into BOARDS values(seq_boards.nextval, '테스트게시판');
insert into BOARDS values(seq_boards.nextval, '공지사항');
insert into members values('admin','1234','관리자','관리자','0','admin@admin.bbs', default);