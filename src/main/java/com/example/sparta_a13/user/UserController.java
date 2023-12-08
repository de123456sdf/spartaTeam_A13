package com.example.sparta_a13.user;

import com.example.sparta_a13.CommonResponseDto;
import com.example.sparta_a13.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto> signup(@Valid @RequestBody UserInfoRequestDTO infoRequestDTO){
        try {
            userService.signup(infoRequestDTO);
        }catch (IllegalArgumentException exception){
            return ResponseEntity.badRequest().body(new CommonResponseDto("중복된 username", HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(new CommonResponseDto("회원 가입 성공", HttpStatus.CREATED.value()));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto> login(@RequestBody UserRequestDTO userRequestDto, HttpServletResponse response){
        try {
            userService.login(userRequestDto);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        response.setHeader(JwtUtil.AUTH_HEADER, jwtUtil.createToken(userRequestDto.getUsername()));

        return ResponseEntity.ok().body(new CommonResponseDto("로그인 성공", HttpStatus.OK.value()));
    }
}
