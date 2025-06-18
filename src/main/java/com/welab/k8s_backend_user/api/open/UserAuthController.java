package com.welab.k8s_backend_user.api.open;

import com.welab.k8s_backend_user.common.dto.ApiResponseDto;
import com.welab.k8s_backend_user.doamin.dto.SiteUserLoginDto;
import com.welab.k8s_backend_user.doamin.dto.SiteUserRefreshDto;
import com.welab.k8s_backend_user.doamin.dto.SiteUserRegisterDto;
import com.welab.k8s_backend_user.secret.jwt.dto.TokenDto;
import com.welab.k8s_backend_user.service.SiteUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserAuthController {
    private final SiteUserService siteUserService;

    @PostMapping(value = "/register")
    public ApiResponseDto<String> register(@RequestBody @Valid SiteUserRegisterDto registerDto){
        siteUserService.registerUser(registerDto);
        return ApiResponseDto.defaultOk();
    }

    @PostMapping(value = "/login")
    public ApiResponseDto<TokenDto.AccessRefreshToken> login(@RequestParam @Valid SiteUserLoginDto loginDto){
        TokenDto.AccessRefreshToken token = siteUserService.login(loginDto);
        return ApiResponseDto.createOk(token);
    }

    @PostMapping(value = "/refresh")
    public ApiResponseDto<TokenDto.AccessToken> refresh(@RequestParam @Valid SiteUserRefreshDto refreshDto){
        TokenDto.AccessToken token = siteUserService.refresh(refreshDto);
        return ApiResponseDto.createOk(token);
    }

    // aroCD-> 블루/그린 배포 테스트
    @PostMapping(value = "/test")
    public ApiResponseDto<String> test(){
        return ApiResponseDto.createOk("버전 1입니다.");
    }
}
