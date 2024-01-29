package entity;

import dto.UserDto;
import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User{
    private int userId;
    private String userName;
}
