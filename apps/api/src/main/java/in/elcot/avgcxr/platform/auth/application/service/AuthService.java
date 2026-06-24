package in.elcot.avgcxr.platform.auth.application.service;

import in.elcot.avgcxr.platform.auth.application.command.CreateAuthCommand;
import in.elcot.avgcxr.platform.auth.application.port.input.CreateAuthUseCase;
import in.elcot.avgcxr.platform.auth.application.port.input.GetAuthUseCase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class AuthService implements CreateAuthUseCase, GetAuthUseCase {
    private final JwtEncoder jwtEncoder; private final PasswordEncoder passwordEncoder;
    public AuthService(JwtEncoder jwtEncoder, PasswordEncoder passwordEncoder) { this.jwtEncoder = jwtEncoder; this.passwordEncoder = passwordEncoder; }

    @Override
    public String authenticate(CreateAuthCommand cmd) {
        JwtClaimsSet claims = JwtClaimsSet.builder().subject(cmd.username()).issuedAt(Instant.now()).expiresAt(Instant.now().plusSeconds(1800)).claim("ip", cmd.ipAddress()).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public boolean validateToken(String token) { return token != null && !token.isBlank(); }
}

