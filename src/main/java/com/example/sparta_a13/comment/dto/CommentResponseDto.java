package com.example.sparta_a13.comment.dto;

import com.example.sparta_a13.CommonResponseDto;
import com.example.sparta_a13.comment.entity.Comment;
import com.example.sparta_a13.user.UserRequestDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CommentResponseDto extends CommonResponseDto {
    private Long id;
    private String text;
    private UserRequestDTO user;
    private LocalDateTime createDate;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.user = new UserRequestDTO(comment.getUser());
        this.createDate = comment.getCreateDate();
    }
}
