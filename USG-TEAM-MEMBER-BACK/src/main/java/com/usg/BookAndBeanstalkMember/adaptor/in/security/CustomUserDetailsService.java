package com.usg.BookAndBeanstalkMember.adaptor.in.security;

import com.usg.BookAndBeanstalkMember.application.usecases.FindOneMemberUseCase;
import com.usg.BookAndBeanstalkMember.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final FindOneMemberUseCase findOneMemberUseCase;

    @Override
    public UserDetails loadUserByUsername(String insertedUserId) throws UsernameNotFoundException {
        Optional<Member> findOne = findOneMemberUseCase.findOne(insertedUserId);
        Member member = findOne.orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다 ㅠ"));

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRoles())
                .build();
    }
}
