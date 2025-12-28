package loanfin.controller.authenticationAPIs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import loanfin.dto.*;
import loanfin.exception.IException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth APIs", description = "APIs for registering and login")
public interface AuthController {

    @Operation(
            summary = "Login in into the LoanFin application",
            description = "User must type in their credentials - email and password"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Logged in successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<IResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) throws IException;
}
