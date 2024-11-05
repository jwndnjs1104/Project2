package model.members;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembersDto {
	private String username;
	private String password;
	private String name;
	private String nickname;
	private String phone;
	private String email;
	private Timestamp regidate;
}
