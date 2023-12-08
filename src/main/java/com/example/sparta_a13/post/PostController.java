package com.example.sparta_a13.post;


import com.example.sparta_a13.CommonResponseDto;
import com.example.sparta_a13.user.UserDetailsImpl;
import com.example.sparta_a13.user.UserRequestDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.sparta_a13.post.response.PostListResponseDto;
import com.example.sparta_a13.post.response.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

@RequestMapping("/api/posts")
@RestController
@RequiredArgsConstructor
public class PostController {


    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostResponseDto responseDto = postService.createPost(postRequestDto, userDetails.getUser());

        return ResponseEntity.status(201).body(responseDto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId) {
        try {
            PostResponseDto responseDto = postService.getPostDto(postId);
            return ResponseEntity.ok().body(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @GetMapping
    public ResponseEntity<List<PostListResponseDto>> getPostList() {
        List<PostListResponseDto> response = new ArrayList<>();

        Map<UserRequestDTO, List<PostResponseDto>> responseDtoMap = postService.getUserPostMap();

        responseDtoMap.forEach((key, value) -> response.add(new PostListResponseDto(key, value)));

        return ResponseEntity.ok().body(response);
    }


    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            PostResponseDto responseDto = postService.updatePost(postId, postRequestDto, userDetails.getUser());
            return ResponseEntity.ok().body(responseDto);
        } catch (RejectedExecutionException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new PostResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<PostResponseDto> deletePost (@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            postService.deletePost(postId, userDetails.getUser());
            return ResponseEntity.ok().body(new PostResponseDto("정상적으로 삭제 되었습니다.", HttpStatus.OK.value()));
        } catch (RejectedExecutionException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new PostResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    
}
