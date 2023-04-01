package com.project.comgle.global.security;

import com.project.comgle.member.entity.Member;
import com.project.comgle.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String useremail) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByEmail(useremail).orElseThrow(
                () -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new UserDetailsImpl(findMember, findMember.getEmail(),findMember.getCompany());
    }

}
