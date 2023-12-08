package com.example.sparta_a13.user;

import lombok.Getter;

@Getter
public class UserInfoRequestDTO extends UserRequestDTO{
    private String email;
    private String introduce;

    public UserInfoRequestDTO(String username, String password, String email, String introduce){
        super(username, password);
        this.email = email;
        this.introduce = introduce;
    }
}
