package com.example.sparta_a13.post;

import com.example.sparta_a13.user.User;
import com.example.sparta_a13.user.UserRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.sparta_a13.post.response.PostResponseDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponseDto createPost(PostRequestDto dto, User user) {
        Post post = new Post(dto);
        post.setUser(user);

        var saved = postRepository.save(post);

        return new PostResponseDto(saved);
    }

    public PostResponseDto getPostDto(Long postId) {
        Post post = getPost(postId);
        return new PostResponseDto(post);
    }

    public Map<UserRequestDTO, List<PostResponseDto>> getUserPostMap() {
        Map<UserRequestDTO, List<PostResponseDto>> userPostMap = new HashMap<>();

        List<Post> postList = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate")); // 작성일 기준 내림차순

        postList.forEach(post -> {
            var userDto = new UserRequestDTO(post.getUser());
            var postDto = new PostResponseDto(post);
            if (userPostMap.containsKey(userDto)) {
                // 유저 할일목록에 항목을 추가
                userPostMap.get(userDto).add(postDto);
            } else {
                // 유저 할일목록을 새로 추가
                userPostMap.put(userDto, new ArrayList<>(List.of(postDto)));
            }
        });

        return userPostMap;
    }

    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto postRequestDto, User user) {
        Post post = getUserPost(postId, user);

        post.setTitle(postRequestDto.getPostTitle());
        post.setContent(postRequestDto.getPostContent());

        return new PostResponseDto(post);
    }

    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = getUserPost(postId, user);

        postRepository.delete(post);
    }

    public Post getPost(Long postId) {

        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 할일 ID 입니다."));
    }

    public Post getUserPost(Long postId, User user) {
        Post post = getPost(postId);

        if(!user.getId().equals(post.getUser().getId())) {
            throw new RejectedExecutionException("작성자만 수정할 수 있습니다.");
        }
        return post;
    }
    
    
}
