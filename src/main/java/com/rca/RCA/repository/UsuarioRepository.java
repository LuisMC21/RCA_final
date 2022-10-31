package com.rca.RCA.repository;


import com.rca.RCA.entity.UsuarioEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer>{


    //Función para obtener un usaurio con filtro por codigo,nombre, apellidos
    @Query(value = "select u from UsuarioEntity u " +
            "where u.status = :status " +
            "and (u.code like concat('%', :filter, '%') or u.pa_surname like concat('%', :filter, '%') or " +
            "u.ma_surname like concat('%', :filter, '%') or u.name like concat('%', :filter, '%') or " +
            "u.numdoc like concat('%', :filter, '%'))")
    Optional<List<UsuarioEntity>> findEntities(String status, String filter, Pageable pageable);

    //Función para contar los usuarios
    @Query(value = "select count(u) from UsuarioEntity u " +
            "where u.status = :status " +
            "and (u.code like concat('%', :filter, '%') or u.pa_surname like concat('%', :filter, '%') or " +
            "u.ma_surname like concat('%', :filter, '%') or u.name like concat('%', :filter, '%') or " +
            "u.numdoc like concat('%', :filter, '%'))")
    Long findCountEntities(String status, String filter);

    //Funcipon para encontrar un usuario por su identificador
    Optional<UsuarioEntity> findByUniqueIdentifier(String uniqueIdentifier);

    //Función para encontrar un usuario por su numero de documento
    Optional<UsuarioEntity> findByNumdoc(String numdoc);

    //Función para encontrar un usuario por su telefono
    Optional<UsuarioEntity> findByTel(String tel);

    @Query(value = "select u from UsuarioEntity u " +
            "where u.numdoc = :numdoc and u.uniqueIdentifier <> :uniqueIdentifier ")
    Optional<UsuarioEntity> findByNumdoc(String numdoc, String uniqueIdentifier);

}



