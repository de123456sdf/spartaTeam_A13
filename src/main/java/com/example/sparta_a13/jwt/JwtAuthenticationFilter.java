package com.example.sparta_a13.jwt;

import com.example.sparta_a13.user.UserDetailsImpl;
import com.example.sparta_a13.user.UserRequestDTO;
import com.example.sparta_a13.user.UserRoleEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/");
    }

    @Override
    public Authentication attemptAuthentication (
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserRequestDTO requestDto = new ObjectMapper().readValue(request.getInputStream(), UserRequestDTO.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    // 인증완료시 액세스토큰, 리프레시토큰 생성
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authen)
        throws IOException, ServletException {

        String username = ((UserDetailsImpl) authen.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authen.getPrincipal()).getUser().getRole();

        String accessToken = jwtUtil.createToken(username, role);
        jwtUtil.addAccessTokenToCookie(accessToken, response);

        String refToken = jwtUtil.createRefreshToken(username, role);
        jwtUtil.addRefreshTokenToCookie(refToken, response);

        response.setHeader(JwtUtil.AUTH_HEADER,accessToken);
        System.out.println("success");
    }

    @Override
    protected void unsuccessfulAuthentication (HttpServletRequest request, HttpServletResponse response,
                                               AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }


}
