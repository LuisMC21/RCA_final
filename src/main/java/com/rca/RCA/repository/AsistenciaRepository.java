package com.rca.RCA.repository;

import com.rca.RCA.entity.AsistenciaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<AsistenciaEntity, Integer> {

    //Obtener asistencia por clase (código) o por alumno (nombre, apellido, código)
    @Query(value = "select a from AsistenciaEntity a JOIN a.alumnoEntity al JOIN a.claseEntity c "+
            "where al = a.alumnoEntity and c = a.claseEntity " +
            "and a.status = :status and al.status =:status and c.status = :status " +
            "and (al.code like concat('%', :filter, '%') or al.uniqueIdentifier like concat('%', :filter, '%') " +
            "or c.code like concat('%', :filter, '%') or c.uniqueIdentifier like concat('%', :filter, '%'))")
    Optional<List<AsistenciaEntity>> findEntities(String status, String filter, Pageable pageable);


    //Contar el número de registros de  asistencia según el filtro que se aplique
    @Query(value = "select count(a) from AsistenciaEntity a JOIN a.alumnoEntity al JOIN a.claseEntity c "+
            "where al = a.alumnoEntity and c = a.claseEntity " +
            "and a.status = :status and al.status =:status and c.status = :status " +
            "and (al.code like concat('%', :filter, '%') or al.uniqueIdentifier like concat('%', :filter, '%') " +
            "or c.code like concat('%', :filter, '%') or c.uniqueIdentifier like concat('%', :filter, '%'))")
    Long findCountEntities(String status, String filter);

    //Obtener una asistencia por su identificador
    Optional<AsistenciaEntity> findByUniqueIdentifier(String uniqueIdentifier);

}
