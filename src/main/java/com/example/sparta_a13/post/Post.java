package com.example.sparta_a13.post;

import com.example.sparta_a13.comment.entity.Comment;
import com.example.sparta_a13.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
public class Post implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String postTitle;

    @Column
    private String username;

    @Column
    private String postContent;

    @Column
    private LocalDateTime createDate;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;


    @Builder

    public Post(String postTitle,String username, String postContent) {
        this.postTitle = postTitle;
        this.username = username;
        this.postContent = postContent;
    }

    public Post(PostRequestDto dto) {
        this.postTitle = dto.getPostTitle();
        this.username = dto.getUsername();
        this.postContent = dto.getPostContent();
        this.createDate = LocalDateTime.now();
    }

    // 연관관계 메서드
    public void setUser(User user) {
        this.user = user;
    }

    // 서비스 메서드
    public void setTitle(String postTitle) {
        this.postTitle = postTitle;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setContent(String postContent) {
        this.postContent = postContent;
    }

}