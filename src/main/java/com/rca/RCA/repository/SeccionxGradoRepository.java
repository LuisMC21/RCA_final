package com.rca.RCA.repository;

import com.rca.RCA.entity.SeccionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface SeccionxGradoRepository extends JpaRepository<SeccionEntity, Integer> {
    //Función para contar las secciones existentes y activas de un grado, con filtro de código y nombre
/*    @Query(value = "SELECT count(s) FROM GradoEntity g JOIN g.seccionxGradoEntities x " +
            "WHERE g.uniqueIdentifier = :id " +
            "and s.status = :status " +
            "and (s.code like concat('%', :filter, '%') or s.name like concat('%', :filter, '%'))" +
            "order by s.name")
    Long findCountSeccionxGrado(String id, String status, String filter);

    //Función para listar las secciones existentes y activas de un grado, con filtro de código y nombre
    @Query(value = "SELECT s FROM GradoEntity g JOIN g.seccionxgrado s " +
            "WHERE g.uniqueIdentifier = :id " +
            "and s.status = :status " +
            "and (s.code like concat('%', :filter, '%') or s.name like concat('%', :filter, '%'))" +
            "order by s.name")
    Optional<List<SeccionEntity>> findSeccionxGrado(String id, String status, String filter, Pageable pageable);
*/

    @Query(value = "select count(seccion.id) from grado inner join seccionxgrado ON grado.id=seccionxgrado.grado_id INNER JOIN seccion ON seccionxgrado.seccion_id=seccion.id;", nativeQuery = true)
    Long findCountSeccionxGrado(String id, String status, String filter);
    @Query(value = "select seccion.* from grado inner join seccionxgrado ON grado.id=seccionxgrado.grado_id INNER JOIN seccion ON seccionxgrado.seccion_id=seccion.id;", nativeQuery = true)
    Optional<List<SeccionEntity>> findSeccionxGrado(String id, String status, String filter, Pageable pageable);

}
