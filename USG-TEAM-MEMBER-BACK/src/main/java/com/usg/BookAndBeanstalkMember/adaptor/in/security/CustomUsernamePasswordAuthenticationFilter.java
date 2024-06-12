package com.usg.BookAndBeanstalkMember.adaptor.in.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 요청에서 사용자 이름과 암호 추출
            UserCredentials userCredentials = objectMapper.readValue(request.getInputStream(), UserCredentials.class);
            String username = userCredentials.getEmail();
            String password = userCredentials.getPassword();

            // 사용자 인증 토큰 생성
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

            // 인증 관리자를 사용하여 인증 시도
            return authenticationManager.authenticate(authRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String username = authResult.getName();

        String token = jwtService.generateToken(username);

        // JSON 응답 데이터 생성
        Map<String, String> responseData = new HashMap<>();
        responseData.put("token", token);

        // JSON 형식으로 변환하여 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }
}