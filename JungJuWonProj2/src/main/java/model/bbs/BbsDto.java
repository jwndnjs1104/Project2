package model.bbs;



import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BbsDto {
	private long bbsid;
	private long boardid;
	private String username;
	private String title;
	private String content;
	private Timestamp postdate;
	private int hitcount;
	private String files;
	
	//추가 정보
	private String boardname;
	private int commentcount;
	private String nickname;
	private String likecount;
}
