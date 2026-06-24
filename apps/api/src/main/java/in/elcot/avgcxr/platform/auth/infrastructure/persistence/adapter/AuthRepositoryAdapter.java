package in.elcot.avgcxr.platform.auth.infrastructure.persistence.adapter;

import in.elcot.avgcxr.platform.auth.application.port.output.AuthRepositoryPort;
import in.elcot.avgcxr.platform.auth.domain.model.Auth;
import in.elcot.avgcxr.platform.auth.infrastructure.persistence.entity.AuthUserEntity;
import in.elcot.avgcxr.platform.auth.infrastructure.persistence.mapper.AuthMapper;
import in.elcot.avgcxr.platform.auth.infrastructure.persistence.repository.JpaAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AuthRepositoryAdapter implements AuthRepositoryPort {

    private final JpaAuthRepository jpaRepository;

    @Override
    public Auth save(Auth user) {
        AuthUserEntity entity = AuthMapper.toEntity(user);
        AuthUserEntity saved = jpaRepository.save(entity);
        return AuthMapper.toDomain(saved);
    }

    @Override
    public Optional<Auth> findById(UUID id) {
        return jpaRepository.findById(id).map(AuthMapper::toDomain);
    }

    @Override
    public Optional<Auth> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(AuthMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
