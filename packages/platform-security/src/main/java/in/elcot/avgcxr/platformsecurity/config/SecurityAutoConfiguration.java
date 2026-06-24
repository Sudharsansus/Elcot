package in.elcot.avgcxr.platformsecurity.config;

import in.elcot.avgcxr.platformsecurity.jwt.JwtAuthenticationFilter;
import in.elcot.avgcxr.platformsecurity.jwt.JwtTokenProvider;
import in.elcot.avgcxr.platformsecurity.rbac.RbacService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@AutoConfiguration
public class SecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    @ConditionalOnMissingBean
    public RbacService rbacService() {
        return new RbacService();
    }
}