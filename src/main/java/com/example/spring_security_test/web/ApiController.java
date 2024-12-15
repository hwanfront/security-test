package com.example.spring_security_test.web;

import com.example.spring_security_test.domain.member.MemberDTO;
import com.example.spring_security_test.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class ApiController {
    private final MemberService memberService;

    @PostMapping("/api/join")
    public String join(
            @Valid @ModelAttribute MemberDTO memberDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "입력값이 유효하지 않습니다.");
            return "join";
        }

        if (!memberDTO.isPasswordMatching()) {
            bindingResult.rejectValue("passwordConfirm", "error.passwordConfirm", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "join";
        }


        memberService.create(memberDTO.getEmail(), memberDTO.getUsername(), memberDTO.getPassword());
        return "redirect:/login";
    }
}
