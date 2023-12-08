package com.example.sparta_a13.post;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class PostRequestDto {

    private String postTitle;
    private String username;
    private String postContent;

}
