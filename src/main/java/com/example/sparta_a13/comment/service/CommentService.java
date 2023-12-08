package com.example.sparta_a13.comment.service;

import com.example.sparta_a13.comment.dto.CommentRequestDto;
import com.example.sparta_a13.comment.dto.CommentResponseDto;
import com.example.sparta_a13.comment.entity.Comment;
import com.example.sparta_a13.comment.repository.CommentRepository;
import com.example.sparta_a13.post.Post;
import com.example.sparta_a13.post.PostRepository;
import com.example.sparta_a13.post.PostService;
import com.example.sparta_a13.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostService postService;
    //생성
    public CommentResponseDto createComment(CommentRequestDto dto, User user) {
        Post post = postService.getPost(dto.getPostId());
        Comment comment = new Comment(dto);
        comment.setUser(user);
        comment.setPost(post);

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    //조회
    public List<CommentResponseDto> getText(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return commentRepository.findAllByPostId(post.getId()).stream().map(CommentResponseDto::new).toList();
    }

//    수정
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = getUserComment(commentId, user);

        comment.setText(commentRequestDto.getText());
        return new CommentResponseDto(comment);
    }

//삭제
    public void deleteComment(Long commentId, User user){
        Comment comment = getUserComment(commentId, user);

        commentRepository.delete(comment);
    }


    private Comment getUserComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글 ID 입니다."));

        if(!user.getId().equals(comment.getUser().getId())) {
            throw new RejectedExecutionException("작성자만 수정할 수 있습니다.");
        }
        return comment;
    }
}

