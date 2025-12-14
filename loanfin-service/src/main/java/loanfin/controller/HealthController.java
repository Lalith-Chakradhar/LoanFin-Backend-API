package loanfin.controller;

import io.swagger.v3.oas.annotations.Hidden;
import loanfin.entity.UserEntity;
import loanfin.repository.UserRepository;
import loanfin.service.PasswordService;
import loanfin.util.EmailHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    private final PasswordService passwordService;
    private final UserRepository userRepository;

    @PostMapping
    ResponseEntity<UserEntity> createUser(@RequestBody UserEntity userEntity)
    {
        UserEntity newUserEntity = UserEntity.builder()
                .userName(userEntity.getUserName())
                .email(userEntity.getEmail())
                .emailHash(EmailHasher.hash(userEntity.getEmail()))
                .password(passwordService.hashPassword(userEntity.getPassword()))
                .role(userEntity.getRole())
                .build();

        return ResponseEntity.ok(userRepository.save(newUserEntity));
    }
}
