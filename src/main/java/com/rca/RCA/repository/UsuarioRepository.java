package com.rca.RCA.repository;


import com.rca.RCA.auth.entity.Rol;
import com.rca.RCA.entity.UsuarioEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer>{

    //Función para obtener un usaurio con filtro por codigo,nombre, apellidos
    @Query(value = "SELECT u.* " +
            "FROM user u " +
            "JOIN usuario_rol ur ON u.id = ur.usuario_id " +
            "JOIN role r ON r.id = ur.rol_id " +
            "WHERE r.rol_nombre = 'ROLE_ADMIN' " +
            "AND u.tx_status = :status " +
            "AND (u.name like concat ('%',:filter,'%') or u.pa_surname like concat ('%',:filter,'%') or u.ma_surname like concat ('%',:filter,'%') " +
            "or u.numdoc like concat ('%',:filter,'%') or u.tx_unique_identifier like concat ('%',:filter,'%'))", nativeQuery = true)
    Optional<List<UsuarioEntity>> findEntities(String status, String filter, Pageable pageable);

    //Función para contar los usuarios
    @Query(value = "SELECT count(*) " +
            "FROM user u " +
            "JOIN usuario_rol ur ON u.id = ur.usuario_id " +
            "JOIN role r ON r.id = ur.rol_id " +
            "WHERE r.rol_nombre = 'ROLE_ADMIN' " +
            "AND u.tx_status = :status " +
            "AND (u.name like concat ('%',:filter,'%') or u.pa_surname like concat ('%',:filter,'%') or u.ma_surname like concat ('%',:filter,'%') " +
            "or u.numdoc like concat ('%',:filter,'%') or u.tx_unique_identifier like concat ('%',:filter,'%'))", nativeQuery = true)
    Long findCountEntities(String status, String filter);

    @Query(value = "select count(*) from usuario u JOIN rol r where r.id = u.rol_id " +
            "and u.tx_status = :status and r.tx_status = :status " +
            "and r.tx_unique_identifier = :uniqueIdentifier", nativeQuery = true)
    Long findCountEntitiesRol(@Param("status") String status, @Param("uniqueIdentifier") String uniqueIdentifier);

    //Funcipon para encontrar un usuario por su identificador

    @Query(value = "SELECT u FROM UsuarioEntity u " +
            "WHERE u.uniqueIdentifier = :id " +
            "AND u.status = :status")
    Optional<UsuarioEntity> findByUniqueIdentifier(String id, String status);

    //Función para encontrar un usuario por su numero de documento

    //Función para encontrar un usuario por su telefono
    @Query(value = "select count(u)>0 from UsuarioEntity u " +
            "where u.tel = :tel and u.uniqueIdentifier <> :id " +
            "AND u.status = :status ")
    boolean existsByTel(String tel, String id, String status);


    @Query(value = "select count(u)>0 from UsuarioEntity u " +
            "where u.numdoc = :numdoc and u.uniqueIdentifier <> :uniqueIdentifier " +
            "AND u.status = :status ")
    boolean existsByNumdoc(String numdoc, String uniqueIdentifier, String status);

    //Función para eliminar imágenes asociadas al usuario
    @Transactional
    @Modifying
    @Query(value="update imagen i JOIN usuario u  SET i.tx_status = 'DELETED', i.tx_delete_at = :fecha " +
            "where i.user_id = u.id" +
            " and u.tx_unique_identifier = :uniqueIdentifier", nativeQuery = true)
    void deleteImagen(@Param("uniqueIdentifier") String uniqueIdentifier, @Param("fecha") LocalDateTime fecha);

    //Función para eliminar noticias asociadas al usuario
    @Transactional
    @Modifying
    @Query(value="update noticia n JOIN usuario u  SET n.tx_status = 'DELETED', n.tx_delete_at = :fecha " +
            "where n.user_id = u.id " +
            "and u.tx_unique_identifier = :uniqueIdentifier", nativeQuery = true)
    void deleteNoticia(@Param("uniqueIdentifier") String uniqueIdentifier, @Param("fecha") LocalDateTime fecha);

    @Query(value = "SELECT u FROM UsuarioEntity u " +
            "WHERE (u.nombreUsuario = :nombreUsuario OR u.email = :nombreUsuario) " +
            "AND status = :status ")
    Optional<UsuarioEntity> findByNombreUsuarioOrEmail(String nombreUsuario, String status);
    Optional<UsuarioEntity> findByTokenPassword(String tokenPassword);

    @Query(value = "select count(u)>0 from UsuarioEntity u " +
            "where u.nombreUsuario = :nombreUsuario and u.uniqueIdentifier <> :id " +
            "AND u.status = :status ")
    boolean existsByNombreUsuario(String nombreUsuario, String id, String status);
    @Query(value = "select count(u)>0 from UsuarioEntity u " +
            "where u.email = :email and u.uniqueIdentifier <> :id " +
            "AND u.status = :status ")
    boolean existsByEmail(String email, String id, String status);

    @Query(value = "SELECT u FROM UsuarioEntity u " +
            "JOIN u.roles r " +
            "WHERE (u.nombreUsuario = :username OR u.email = :username) " +
            "AND u.status = :status ")
    Optional<UsuarioEntity> idFindByUsername(String username,String status);

    @Query(value = "SELECT d.uniqueIdentifier FROM UsuarioEntity u " +
            "JOIN u.docenteEntity d " +
            "WHERE (u.nombreUsuario = :username OR u.email = :username) " +
            "AND u.status = :status ")
    Optional<String> idDocenteByUsername(String username,String status);

    @Query(value = "SELECT a.uniqueIdentifier FROM UsuarioEntity u " +
            "JOIN u.alumnoEntity a " +
            "WHERE (u.nombreUsuario = :username OR u.email = :username) " +
            "AND u.status = :status ")
    Optional<String> idAlumnoByUsername(String username,String status);
}



