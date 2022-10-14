package com.rca.RCA.repository;

import com.rca.RCA.entity.GradoEntity;
import com.rca.RCA.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<RolEntity, Integer> {

    Optional<RolEntity> findByUniqueIdentifier(String uniqueIdentifier);

    Optional<RolEntity> findByName(String name);
}
