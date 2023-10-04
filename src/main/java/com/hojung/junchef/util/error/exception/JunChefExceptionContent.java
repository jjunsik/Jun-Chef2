package com.hojung.junchef.util.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JunChefExceptionContent {
    NETWORK_ERROR(1000, "네트워크 연결 오류", "인터넷 연결 상태를 확인해 주세요."),
    API_ERROR(2000, "통신 오류", "잠시 후 다시 시도해 주세요."),
    SEARCH_WORD_ERROR(3000, "검색어 오류",  "검색어를 다시 입력해 주세요."),
    NON_EXIST_RECIPE_ERROR(4000, "레시피 오류","레시피가 없는 음식입니다.\n검색어 확인 후, 다시 검색해 주세요."),
    DUPLICATE_MEMBER_ERROR(5000, "중복 회원 오류", "중복된 이메일입니다.\n다른 이메일로 회원 가입해 주세요."),
    NON_EXIST_MEMBER_ERROR(5100, "회원 오류", "존재하지 않는 회원입니다.\n회원 가입 후 이용해 주세요."),
    NON_EXIST_MEMBER_EMAIL_ERROR(5200, "이메일 오류", "존재하지 않는 이메일입니다.\n이메일을 확인해 주세요."),
    MEMBER_PASSWORD_ERROR(5300, "비밀번호 오류", "비밀번호가 일치하지 않습니다.\n비밀번호 확인해 주세요.");

    private final int code;
    private final String title;
    private final String message;
}
