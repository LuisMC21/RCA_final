package com.rca.RCA.repository;

import com.rca.RCA.entity.NoticiaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoticiaRepository extends JpaRepository<NoticiaEntity, Integer> {

    //Función para obtener una noticia con filtros por datos de un usuario (codigo, uniqueIdentifier)
    @Query(value = "select n from NoticiaEntity n join n.usuarioEntity u " +
            "where u = n.usuarioEntity and u.status=:status and n.status=:status " +
            "and (u.code like concat('%', :filter, '%') or u.uniqueIdentifier like concat('%', :filter, '%'))")
    Optional<List<NoticiaEntity>> findEntities(String status, String filter, Pageable pageable);

    //Función para contar las noticias
    @Query(value = "select count(n) from NoticiaEntity n join n.usuarioEntity u " +
            "where u = n.usuarioEntity and u.status=:status " +
            "and (u.code like concat('%', :filter, '%') or u.uniqueIdentifier like concat('%', :filter, '%'))")
    Long findCountEntities(String status, String filter);

    //Función para obtener una noticia según su identificador
    Optional<NoticiaEntity> findByUniqueIdentifier(String uniqueIdentifier);

    //Función para obtener una noticia por su título
    Optional<NoticiaEntity> findByTitle(String title);

    @Query(value = "select n from NoticiaEntity n " +
            "where n.title = :title and n.uniqueIdentifier <> :uniqueIdentifier ")
    Optional<NoticiaEntity> findByTitle(String title, String uniqueIdentifier);

    //Función para verificar si exixte una noticia con el mismo nombre
    @Query(value = "SELECT count(n)>0 FROM NoticiaEntity n " +
            "WHERE n.status = :status " +
            "AND  n.title = :title " +
            "AND n.uniqueIdentifier != :id")
    boolean existsByTitle(String id, String status, String title);

}
