package com.example.sparta_a13.comment.entity;

import com.example.sparta_a13.comment.dto.CommentRequestDto;
import com.example.sparta_a13.post.Post;
import com.example.sparta_a13.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private LocalDateTime createDate;

    public Comment(CommentRequestDto dto) {
        this.text = dto.getText();
        this.createDate = LocalDateTime.now();
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }
    public void setText(String text) {
        this.text = text;
    }


}
