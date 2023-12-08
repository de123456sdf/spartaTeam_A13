package com.example.sparta_a13.post.response;

import com.example.sparta_a13.user.UserRequestDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class PostListResponseDto {

    private UserRequestDTO user;
    private List<PostResponseDto> postList;

    public PostListResponseDto(UserRequestDTO user, List<PostResponseDto> postList) {
        this.user = user;
        this.postList = postList;
    }


}
