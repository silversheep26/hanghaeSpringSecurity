package com.sparta.hanghaejwt.service;

// user 회원가입, 로그인을 위한 클래스

import com.sparta.hanghaejwt.dto.LoginRequestDto;
import com.sparta.hanghaejwt.dto.MessageStatusResponseDto;
import com.sparta.hanghaejwt.dto.SignupRequestDto;
import com.sparta.hanghaejwt.entity.User;
import com.sparta.hanghaejwt.entity.UserRoleEnum;
import com.sparta.hanghaejwt.jwt.JwtUtil;
import com.sparta.hanghaejwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 회원 가입
    @Transactional
    public MessageStatusResponseDto signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        String strRole = String.valueOf(signupRequestDto.getAdminToken());
        if (strRole.equals(ADMIN_TOKEN)) {
            role = UserRoleEnum.ADMIN;
        } else if (strRole.equals("null")) {
            role = UserRoleEnum.USER;
        } else if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "관리자 암호가 틀렸습니다.");
        }

        User user = new User(username, password, role);
        userRepository.save(user);

        return new MessageStatusResponseDto("회원 가입 성공", HttpStatus.OK);
    }

    // 로그인
    @Transactional(readOnly = true)
    public MessageStatusResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        // 아이디 일치 확인
        if (!user.getUsername().equals(username)) {
            return new MessageStatusResponseDto("아이디가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
        return new MessageStatusResponseDto("로그인 성공", HttpStatus.OK);
    }
}



/*
에러 처리 코드
() -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다")
 */
