package com.rca.RCA.repository;

import com.rca.RCA.entity.EvaluacionEntity;
import com.rca.RCA.entity.UsuarioEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EvaluacionRepository extends JpaRepository<EvaluacionEntity, Integer> {

    //Obtener evaluaciones por docentexCurso o por alumno
    @Query(value = "select e from EvaluacionEntity e JOIN e.alumnoEntity a JOIN e.docentexCursoEntity dc " +
            "WHERE a = e.alumnoEntity and dc = e.docentexCursoEntity " +
            "and e.status = :status " +
            "and (a.code like concat('%', :filter, '%') or dc.code like concat('%', :filter, '%'))")
    Optional<List<EvaluacionEntity>> findEntities(String status, String filter, Pageable pageable);

    @Query(value = "select count(e) from EvaluacionEntity e JOIN e.alumnoEntity a JOIN e.docentexCursoEntity dc "+
            "WHERE a = e.alumnoEntity and dc = e.docentexCursoEntity " +
            "and e.status = :status " +
            "and (a.code like concat('%', :filter, '%') or dc.code like concat('%', :filter, '%'))")
    Long findCountEntities(String status, String filter);


    Optional<EvaluacionEntity> findByUniqueIdentifier(String uniqueIdentifier);

}
