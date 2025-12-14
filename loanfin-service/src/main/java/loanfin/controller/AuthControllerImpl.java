package loanfin.controller;

import jakarta.validation.Valid;
import loanfin.constant.ApiStatus;
import loanfin.dto.*;
import loanfin.exception.IException;
import loanfin.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    @PostMapping(value = "/auth/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) throws IException {

        LoginResponse loginDetails = authService.login(request);

        return ResponseEntity.ok(IResponse.<LoginResponse>builder()
                .data(loginDetails)
                .status(ApiStatus.LOGIN_SUCCESSFUL.name())
                .message(ApiStatus.LOGIN_SUCCESSFUL.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }
}
