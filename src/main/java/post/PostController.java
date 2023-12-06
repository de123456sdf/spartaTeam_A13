package post;


import post.response.PostListResponseDto;
import post.response.PostResponseDto;
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
    public ResponseEntity<PostResponseDto> postPost(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostResponseDto responseDto = postService.createPost(postRequestDto, userDetails.getUser());

        return ResponseEntity.status(201).body(responseDto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponseDto> getPost(@PathVariable Long postId) {
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

        Map<UserDto, List<PostResponseDto>> responseDtoMap = postService.getUserPostMap();

        responseDtoMap.forEach((key, value) -> response.add(new PostListResponseDto(key, value)));

        return ResponseEntity.ok().body(response);
    }


    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> putPost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            PostResponseDto responseDto = postService.updatePost(postId, postRequestDto, userDetails.getUser());
            return ResponseEntity.ok().body(responseDto);
        } catch (RejectedExecutionException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new PostResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }


    @PatchMapping("/{postId}/complete")
    public ResponseEntity<PostResponseDto> completePost(@PathVariable Long postId,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            PostResponseDto responseDto = postService.completePost(postId, userDetails.getUser());
            return ResponseEntity.ok().body(responseDto);
        } catch (RejectedExecutionException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new PostResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
    
}