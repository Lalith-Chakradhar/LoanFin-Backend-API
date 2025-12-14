package loanfin.service;

import loanfin.dto.LoginRequest;
import loanfin.dto.LoginResponse;
import loanfin.entity.UserEntity;
import loanfin.repository.UserRepository;
import loanfin.util.EmailHasher;
import loanfin.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService
{
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request)
    {
        String emailHash = EmailHasher.hash(request.getEmail());

        UserEntity user = userRepository.findByEmailHash(emailHash)
                .orElseThrow(()-> new RuntimeException("Invalid credentials"));

        if(!passwordService.verify(user.getPassword(),request.getPassword()))
        {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);

        return new LoginResponse(token, user.getRole().name());
    }

}
