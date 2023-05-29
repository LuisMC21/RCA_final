package com.rca.RCA.auth.repository;

import com.rca.RCA.auth.entity.Rol;
import com.rca.RCA.auth.enums.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    /*
    //Fución para listar los roles con filtro por código o nombre
    @Query(value = "select r from RolEntity r " +
            "where r.status = :status " +
            "and ( r.code like concat('%', :filter, '%') or r.name like concat('%', :filter, '%') ) " +
            "order by r.name")
    Optional<List<RolEntity>> findEntities(String status, String filter, Pageable pageable);

    //Funcion para contar el número de roles
    @Query(value = "select count(r) from RolEntity r " +
            "where r.status = :status " +
            "and ( r.code like concat('%', :filter, '%') or r.name like concat('%', :filter, '%') ) " +
            "order by r.name")
    Long findCountEntities(String status, String filter);

    //Obtener un rol por su identificador
    Optional<RolEntity> findByUniqueIdentifier(String uniqueIdentifier);

    //Obtener un rol según su nombre
    Optional<RolEntity> findByName(String name);

    //Obtener un rol por su nombre e identificador
    @Query(value = "select r from RolEntity r " +
            "where r.name = :name and r.uniqueIdentifier <> :uniqueIdentifier ")
    Optional<RolEntity> findByName(String name, String uniqueIdentifier);

    @Transactional
    @Modifying
    @Query(value="update usuario u JOIN rol r  SET u.tx_status = 'DELETED' where u.rol_id = r.id" +
            " and r.tx_unique_identifier = :uniqueIdentifier", nativeQuery = true)
    void deleteUsuarios(@Param("uniqueIdentifier") String uniqueIdentifier);
    */

    Optional<Rol> findByRolNombre(RolNombre rolNombre);

    Optional<Rol> findByUniqueIdentifier(String uniqueIdentifier);
}
