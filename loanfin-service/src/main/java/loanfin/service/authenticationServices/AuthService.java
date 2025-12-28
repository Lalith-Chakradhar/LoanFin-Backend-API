package loanfin.service.authenticationServices;

import loanfin.dto.LoginRequest;
import loanfin.dto.LoginResponse;


public interface AuthService {
    public LoginResponse login(LoginRequest request);
}
