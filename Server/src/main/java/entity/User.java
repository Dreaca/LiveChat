package entity;

import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User{
    private int userId;
    private String userName;
    private String passWord;
}
