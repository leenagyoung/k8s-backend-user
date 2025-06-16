package com.welab.k8s_backend_user.advice;

import com.welab.k8s_backend_user.advice.parameter.ParameterErrorDto;
import com.welab.k8s_backend_user.common.dto.ApiResponseDto;
import com.welab.k8s_backend_user.common.exception.BadParameter;
import com.welab.k8s_backend_user.common.exception.ClientError;
import com.welab.k8s_backend_user.common.exception.NotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@Order(value = 1)
@RestControllerAdvice
public class ApiCommonAdvice {
    // 모든 예외를 다루는 핸들러 (모두 서버 에러로 나옴)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ApiResponseDto<String> handleException(Exception e){
        return ApiResponseDto.createError(
                "ServerError",
                "서버 에러입니다."
        );
    }
    // 예외 상황에 따라 분기해서 작성해줘야 함

    // return에 중단점 찍고 디버그 모드로 실행하면 어떤 예외상황인지 나옴
    @ExceptionHandler({NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND) // 적절한 에러 찾아서 작성하면 됨
    public ApiResponseDto<String> handleNoResourceFoundException(NoResourceFoundException e){
        return ApiResponseDto.createError(
                "NoResource",
                "리소스를 찾을 수 없습니다."
        );
    }

    // 예외 발생했을때 모든 return에 다 중단점 찍고 실행하는게 간단한 방법

    @ExceptionHandler({BadParameter.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseDto<String> handleBadParameter(BadParameter e){
        return ApiResponseDto.createError(
                e.getErrorCode(),
                e.getErrorMessage()
        );
    }

    @ExceptionHandler({NotFound.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponseDto<String>handleNotFound(NotFound e){
        return ApiResponseDto.createError(
                e.getErrorCode(),
                e.getErrorMessage()
        );
    }

    // 위 두 예외가 아닌 경우는 여기서 처리될 것임 (더 구체적인 핸들러가 먼저 처리함)
    // api exception설계 후 사용
    @ExceptionHandler({ClientError.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseDto<String> handleClientError(ClientError e){
        return ApiResponseDto.createError(
                e.getErrorCode(),
                e.getErrorMessage()
        );
    }


    // 어느 파라미터에서 에러가 난건지 확인하기 위해 추가한 코드
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ApiResponseDto<ParameterErrorDto.FieldList> handleArgumentNotValidException(MethodArgumentNotValidException e){
        BindingResult result = e.getBindingResult();
        ParameterErrorDto.FieldList fieldList = ParameterErrorDto.FieldList.of(result);

        String errorMessage = fieldList.getErrorMessage();
        return ApiResponseDto.createError("ParameterNotValid", errorMessage, fieldList);
    }
}
