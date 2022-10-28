package com.rca.RCA.repository;

import com.rca.RCA.entity.EvaluacionEntity;
import com.rca.RCA.entity.UsuarioEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EvaluacionRepository extends JpaRepository<EvaluacionEntity, Integer> {

    @Query(value = "select e from EvaluacionEntity e JOIN e.alumnoEntity a JOIN a.usuarioEntity u WHERE a = e.alumnoEntity " +
            "and  u = a.usuarioEntity and e.status = :status " +
            "and (u.code like concat('%', :filter, '%') or u.pa_surname like concat('%', :filter, '%') or " +
            "u.ma_surname like concat('%', :filter, '%') or u.name like concat('%', :filter, '%') or " +
            "u.numdoc like concat('%', :filter, '%'))")
    Optional<List<EvaluacionEntity>> findEntities(String status, String filter, Pageable pageable);

    @Query(value = "select count(e) from EvaluacionEntity e JOIN e.alumnoEntity a JOIN a.usuarioEntity u WHERE a = e.alumnoEntity " +
            "and  u = a.usuarioEntity and e.status = :status " +
            "and (u.code like concat('%', :filter, '%') or u.pa_surname like concat('%', :filter, '%') or " +
            "u.ma_surname like concat('%', :filter, '%') or u.name like concat('%', :filter, '%') or " +
            "u.numdoc like concat('%', :filter, '%'))")
    Long findCountEntities(String status, String filter);


    Optional<EvaluacionEntity> findByUniqueIdentifier(String uniqueIdentifier);

}
