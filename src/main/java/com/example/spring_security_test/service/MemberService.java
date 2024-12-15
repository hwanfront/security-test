package com.example.spring_security_test.service;

import com.example.spring_security_test.domain.member.Member;
import com.example.spring_security_test.domain.member.MemberRepository;
import com.example.spring_security_test.domain.member.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void create(String email, String username, String password) {
        Member member = Member.builder()
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();
        memberRepository.save(member);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return User.builder()
                .username(username)
                .password(member.getPassword())
                .roles(Role.USER.name())
                .build();

    }
}
