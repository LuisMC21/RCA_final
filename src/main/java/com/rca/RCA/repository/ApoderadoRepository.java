package com.rca.RCA.repository;

import com.rca.RCA.entity.ApoderadoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ApoderadoRepository extends JpaRepository<ApoderadoEntity, Integer> {

    //Función para obtener los apoderaods con filtro por nombre, apellidos, documento
    @Query(value = "SELECT a FROM UsuarioEntity u " +
            "JOIN u.apoderadoEntity a " +
            "WHERE u = a.usuarioEntity " +
            "AND a.status = :status " +
            "AND u.status = :status " +
            "AND (a.code like concat('%', :filter, '%') or u.pa_surname like concat('%', :filter, '%') or " +
    "       u.ma_surname like concat('%', :filter, '%') or u.name like concat('%', :filter, '%') or u.numdoc like concat('%', :filter, '%'))")
    Optional<List<ApoderadoEntity>> findEntities(String status, String filter, Pageable pageable);

    //Función para contar los apoderados
    @Query(value = "SELECT count(a) FROM UsuarioEntity u " +
            "JOIN u.apoderadoEntity a " +
            "WHERE u = a.usuarioEntity " +
            "AND a.status = :status " +
            "AND u.status = :status " +
            "AND (a.code like concat('%', :filter, '%') or u.pa_surname like concat('%', :filter, '%') "+
            "or u.ma_surname like concat('%', :filter, '%') or u.name like concat('%', :filter, '%') or u.numdoc like concat('%', :filter, '%'))")
    Long findCountEntities(String status, String filter);

    //Funcion para obtener un apoderado por su identificador
    Optional<ApoderadoEntity> findByUniqueIdentifier(String uniqueIdentifier);

    //Funcion para obtener un apoderado por su email
    Optional<ApoderadoEntity> findByEmail(String email);

    @Query(value = "select a from ApoderadoEntity a " +
            "where a.email = :email and a.uniqueIdentifier <> :uniqueIdentifier ")
    Optional<ApoderadoEntity> findByEmail(String email, String uniqueIdentifier);

    //Función para eliminar al usuario asociado al Apoderado
    @Transactional
    @Modifying
    @Query(value = "update usuario u JOIN apoderado a  SET u.tx_status = 'DELETED', u.tx_delete_at = :fecha " +
            "where a.user_id = u.id " +
            "and a.tx_unique_identifier = :uniqueIdentifier", nativeQuery = true)
    void eliminarUsuario(@Param("uniqueIdentifier") String uniqueIdentifier, @Param("fecha")LocalDateTime fecha);
}
