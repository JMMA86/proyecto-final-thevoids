package org.thevoids.oncologic.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thevoids.oncologic.dto.entity.AuthRequest;
import org.thevoids.oncologic.dto.custom.ApiResponse;
import org.thevoids.oncologic.dto.custom.AuthResponseDTO;
import org.thevoids.oncologic.service.impl.CustomUserDetailsServiceImpl;
import org.thevoids.oncologic.utils.JwtService;

@RestController
@RequestMapping("/api/v1/auth")
public class RestAuthController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Login method to authenticate user and generate JWT token.
     * @param request AuthRequest object containing username and password
     * @return JWT token if authentication is successful
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(request.getUsername());
            String token = jwtService.generateToken(userDetails);
            
            AuthResponseDTO authResponse = new AuthResponseDTO(token, request.getUsername());
            
            return ResponseEntity.ok(ApiResponse.exito("Inicio de sesión exitoso", authResponse));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(ApiResponse.error("Autenticación fallida: " + e.getMessage()));
        }
    }
}
