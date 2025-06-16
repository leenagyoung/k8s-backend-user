package com.welab.k8s_backend_user.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

// getter없으면 스프링이 json변환 못함 -> 에러
@Getter
@NoArgsConstructor
public class ApiResponseDto<T> {
    private String code;
    private String message;
    private T data;

    // 생성자 만들기
    // private으로 만들어서 다른 코드에서 객체 생성시 new로 새로운 응답 ex)new ApiResponseDto<>("OKay", "요청이 성공하였습니다", data)
    // 생성 불가능 -> 데이터가 변경되지 않고 일관성을 가질 수 있음
    private ApiResponseDto(String code, String message){
        this.code = code;
        this.message = message;
    }

    // data를 파라미터로 받는 생성자
    private ApiResponseDto(String code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public static <T> ApiResponseDto<T> createOk(T data){
        return new ApiResponseDto<>("OK", "요청이 성공하였습니다", data);
    }

    // data 필요없이 성공만 알면 되는 경우 -> 더 간단하고 명확하게 성공을 나타낼 수 있음
    // 함수 이름만 보고도 data없이 성공을 반환하는 기본 응답임을 알 수 있어서 가독성이 좋아짐
    public static ApiResponseDto<String> defaultOk(){
        return ApiResponseDto.createOk(null);
    }

    // 에러가 난 경우 data는 필요 없으니깐
    public static ApiResponseDto<String> createError(String code, String message){
        return new ApiResponseDto<>(code, message);
    }


    // 회원가입할때 어떤 파라미터에서 에러인지 확인하기 위해
    public static <T> ApiResponseDto<T> createError(String code, String message, T data) {
        return new ApiResponseDto<>(code, message, data);
    }
}
