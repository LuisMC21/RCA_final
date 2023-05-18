package com.rca.RCA.repository;

import com.rca.RCA.entity.ApoderadoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApoderadoRepository extends JpaRepository<ApoderadoEntity, Integer> {

    //Función para obtener los apoderaods con filtro por nombre, apellidos, documento
    @Query(value = "SELECT a FROM ApoderadoEntity a " +
            "WHERE a.status = :status " +
            "AND (a.code like concat('%', :filter, '%') or a.pa_surname like concat('%', :filter, '%') or " +
            "a.ma_surname like concat('%', :filter, '%') or a.name like concat('%', :filter, '%') or a.numdoc like concat('%', :filter, '%')) ")
    Optional<List<ApoderadoEntity>> findEntities(String status, String filter, Pageable pageable);

    //Función para contar los apoderados
    @Query(value = "SELECT count(a) FROM ApoderadoEntity a " +
            "WHERE a.status = :status " +
            "AND (a.code like concat('%', :filter, '%') or a.pa_surname like concat('%', :filter, '%') or " +
            "a.ma_surname like concat('%', :filter, '%') or a.name like concat('%', :filter, '%') or a.numdoc like concat('%', :filter, '%')) ")
    Long findCountEntities(String status, String filter);

    //Funcion para obtener un apoderado por su identificador
    @Query(value = "SELECT apo FROM ApoderadoEntity apo " +
            "WHERE apo.uniqueIdentifier = :id " +
            "AND apo.status = :status ")
    Optional<ApoderadoEntity> findByUniqueIdentifier(String id, String status);
    
    Optional<ApoderadoEntity> findByCode(String code);

    //Funcion para obtener un apoderado por su email
    Optional<ApoderadoEntity> findByEmail(String email);

    @Query(value = "select a from ApoderadoEntity a " +
            "where a.email = :email and a.uniqueIdentifier <> :uniqueIdentifier ")
    Optional<ApoderadoEntity> findByEmail(String email, String uniqueIdentifier);

    //Función para eliminar al usuario asociado al Apoderado

    @Query(value = "select count(a)>0 from ApoderadoEntity a " +
            "where a.status = :status " +
            "and a.email = :email " +
            "and a.uniqueIdentifier != :id ")
    boolean existsByEmail(String status, String email, String id);
}
