package in.elcot.avgcxr.platform.user.infrastructure.persistence.adapter;

import in.elcot.avgcxr.platform.user.application.port.output.UserRepositoryPort;
import in.elcot.avgcxr.platform.user.domain.model.User;
import in.elcot.avgcxr.platform.user.domain.model.UserId;
import in.elcot.avgcxr.platform.user.infrastructure.persistence.entity.UserEntity;
import in.elcot.avgcxr.platform.user.infrastructure.persistence.mapper.UserMapper;
import in.elcot.avgcxr.platform.user.infrastructure.persistence.repository.JpaUserRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final JpaUserRepository jpaRepo;
    private final UserMapper mapper;

    public UserRepositoryAdapter(JpaUserRepository jpaRepo, UserMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public User save(User u) {
        return mapper.toDomain(jpaRepo.save(mapper.toEntity(u, null)));
    }

    @Override
    public User saveWithCredentials(User u, String passwordHash) {
        UserEntity entity = mapper.toEntity(u, passwordHash);
        UserEntity saved = jpaRepo.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepo.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepo.findByUsername(username).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepo.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepo.existsByEmail(email);
    }

    @Override
    public boolean existsByMobileNumber(String mobile) {
        return jpaRepo.existsByMobileNumber(mobile);
    }
}
