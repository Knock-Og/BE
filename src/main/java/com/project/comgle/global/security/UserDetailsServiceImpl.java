package com.project.comgle.global.security;

import com.project.comgle.global.exception.ExceptionEnum;
import com.project.comgle.member.entity.Member;
import com.project.comgle.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberEmail) throws UsernameNotFoundException {

        Optional<Member> findMember = memberRepository.findByEmail(memberEmail);

        if(findMember.isEmpty() || !findMember.get().isValid()){
            throw new UsernameNotFoundException(ExceptionEnum.NOT_EXIST_MEMBER.getMsg());
        } else if (!findMember.get().getCompany().isValid()) {
            throw new UsernameNotFoundException(ExceptionEnum.NOT_EXIST_MEMBER.getMsg());
        }

        return UserDetailsImpl.from(findMember.get());
    }

}
