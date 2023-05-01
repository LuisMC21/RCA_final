package com.rca.RCA.repository;

import com.rca.RCA.entity.ImagenEntity;
import com.rca.RCA.entity.UsuarioEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ImagenRepository extends JpaRepository<ImagenEntity, Integer> {

    //Función para obtener una imagen con filtros por datos de un usuario (codigo, apellidos, nombre)
    @Query(value = "select i from ImagenEntity i join i.usuarioEntity u " +
            "where u = i.usuarioEntity and u.status=:status and i.status=:status " +
            "and (u.code like concat('%', :filter, '%') or u.uniqueIdentifier like concat('%', :filter, '%'))")
    Optional<List<ImagenEntity>> findEntities(String status, String filter, Pageable pageable);

    //Función para contar las imágenes
    @Query(value = "select count(i) from ImagenEntity i join i.usuarioEntity u " +
            "where u = i.usuarioEntity and u.status=:status and i.status=:status " +
            "and (u.code like concat('%', :filter, '%') or u.uniqueIdentifier like concat('%', :filter, '%'))")
    Long findCountEntities(String status, String filter);


    //Función para obtener una imagen según su nombre
    Optional<ImagenEntity> findByName(String name);

    //Función para obtener una imagen según su identificador
    Optional<ImagenEntity> findByUniqueIdentifier(String uniqueIdentifier);

    @Query(value = "select i from ImagenEntity i " +
            "where i.name = :name and i.uniqueIdentifier <> :uniqueIdentifier ")
    Optional<ImagenEntity> findByName(String name, String uniqueIdentifier);

    //Función para obtener una imagen según su identificador


    //Función para verificar si exixte una imagen con el mismo nombre
    @Query(value = "SELECT count(i)>0 FROM ImagenEntity i " +
            "WHERE i.uniqueIdentifier != :id " +
            "AND i.status = :status " +
            "AND i.name = :name")
    boolean existsByName(String id, String status, String name);
}
