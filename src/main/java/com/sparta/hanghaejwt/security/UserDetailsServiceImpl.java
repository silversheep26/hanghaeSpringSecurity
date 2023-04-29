package com.sparta.hanghaejwt.security;

// DB 에 접근하여 user 객체를 갖고 온 다음에 검증하는 부분
import com.sparta.hanghaejwt.entity.User;
import com.sparta.hanghaejwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {  // username : 사용자가 입력한 유저네임
        // 사용자를 DB 에서 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new UserDetailsImpl(user, user.getPassword(), user.getUsername());
    }

}