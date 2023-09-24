package com.hojung.junchef.controller.member;

import com.hojung.junchef.controller.member.dto.request.ChangePasswordRequest;
import com.hojung.junchef.controller.member.dto.request.LoginRequest;
import com.hojung.junchef.controller.member.dto.request.MemberJoinRequest;
import com.hojung.junchef.controller.member.dto.response.GetMemberResponse;
import com.hojung.junchef.controller.member.dto.response.MemberResponse;
import com.hojung.junchef.domain.member.Member;
import com.hojung.junchef.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("jun-chef/v1/members")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<GetMemberResponse> getAllMembers() {
        List<Member> members = memberService.findAll();

        return members.stream()
                .map(GetMemberResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public GetMemberResponse getMember(@PathVariable("id") Long memberId) {
        Member member = memberService.findById(memberId);

        return new GetMemberResponse(member);
    }

    @PostMapping
    public MemberResponse join(@RequestBody MemberJoinRequest joinRequest) {
        Long memberId = memberService.join(joinRequest.toMember());

        return new MemberResponse(memberId);
    }

    @PutMapping("/{id}")
    public MemberResponse changePassword(@PathVariable("id") Long memberId, @RequestBody ChangePasswordRequest changePasswordRequest) {
        memberService.changePassword(memberId, changePasswordRequest.getNewPasswd());

        return new MemberResponse(memberId);
    }

    @DeleteMapping("/{id}")
    public MemberResponse delete(@PathVariable("id") Long memberId) {
        memberService.delete(memberId);

        return new MemberResponse(memberId);
    }

    @PostMapping("/login")
    public MemberResponse login(@RequestBody LoginRequest loginRequest) {
        Long memberId = memberService.login(loginRequest.getEmail(), loginRequest.getPasswd());

        return new MemberResponse(memberId);
    }

    @GetMapping("/logout/{memberId}")
    public MemberResponse logout(@PathVariable("memberId") Long memberId) {
        Long logoutMemberId = memberService.logout(memberId);

        return new MemberResponse(logoutMemberId);
    }

}
