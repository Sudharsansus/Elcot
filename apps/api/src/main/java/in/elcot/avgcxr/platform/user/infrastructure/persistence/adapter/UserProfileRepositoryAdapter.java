package in.elcot.avgcxr.platform.user.infrastructure.persistence.adapter;

import in.elcot.avgcxr.platform.user.application.port.output.UserProfileRepositoryPort;
import in.elcot.avgcxr.platform.user.domain.model.UserProfile;
import in.elcot.avgcxr.platform.user.infrastructure.persistence.entity.UserProfileEntity;
import in.elcot.avgcxr.platform.user.infrastructure.persistence.mapper.UserProfileMapper;
import in.elcot.avgcxr.platform.user.infrastructure.persistence.repository.JpaUserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserProfileRepositoryAdapter implements UserProfileRepositoryPort {
    private final JpaUserProfileRepository jpaRepository;

    @Override
    public UserProfile save(UserProfile entity) {
        return UserProfileMapper.toDomain(jpaRepository.save(UserProfileMapper.toEntity(entity)));
    }

    @Override
    public Optional<UserProfile> findById(UUID id) {
        return jpaRepository.findById(id).map(UserProfileMapper::toDomain);
    }

    @Override
    public Page<UserProfile> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(UserProfileMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
