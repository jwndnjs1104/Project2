package model.comments;


import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentsDto {
	private long commentid;
	private long bbsid;
	private String username;
	private String content;
	private Timestamp postdate;
	
	//추가 정보용(내 정보용)
	private String nickname;
	private String boardname;
	private String boardid;
	private String bbstitle;
}
