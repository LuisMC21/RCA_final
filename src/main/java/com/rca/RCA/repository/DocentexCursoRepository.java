package com.rca.RCA.repository;

import com.rca.RCA.entity.DocentexCursoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocentexCursoRepository extends JpaRepository<DocentexCursoEntity, Integer> {

    //Función para contar las aulas existentes y activas de un grado, con filtro de código y nombre
     @Query(value = "SELECT count(x) from DocenteEntity d " +
            "JOIN d.docentexCursoEntities x " +
            "JOIN x.cursoEntity c " +
            "JOIN x.anio_lectivoEntity a " +
            "WHERE d=x.docenteEntity " +
            "AND d.status = :status " +
            "AND x.status = :status " +
            "AND c.status = :status " +
            "AND (d.code like concat('%', :filter, '%') or x.code like concat('%', :filter, '%') or a.name like concat('%', :filter, '%') " +
             "or c.name like concat('%', :filter, '%'))")
    Long findCountDocentexCurso(String status, String filter);

    //Función para listar las aulas existentes y activas de un grado, con filtro de código y nombre
    @Query(value = "SELECT x from DocenteEntity d " +
            "JOIN d.docentexCursoEntities x " +
            "JOIN x.cursoEntity c " +
            "JOIN x.anio_lectivoEntity a " +
            "WHERE d=x.docenteEntity " +
            "AND d.status = :status " +
            "AND x.status = :status " +
            "AND c.status = :status " +
            "AND (d.code like concat('%', :filter, '%') or x.code like concat('%', :filter, '%') or a.name like concat('%', :filter, '%')) " +
            "order by x.aulaEntity.gradoEntity.name, x.aulaEntity.seccionEntity.name")
    Optional<List<DocentexCursoEntity>> findDocentexCurso(String status, String filter, Pageable pageable);

    @Query(value = "SELECT x from DocenteEntity d " +
            "JOIN d.docentexCursoEntities x " +
            "JOIN x.aulaEntity au " +
            "JOIN x.cursoEntity c " +
            "JOIN x.anio_lectivoEntity a " +
            "JOIN au.matriculaEntities m " +
            "JOIN m.alumnoEntity al " +
            "WHERE d=x.docenteEntity " +
            "AND d.status = :status " +
            "AND x.status = :status " +
            "AND c.status = :status " +
            "AND a.uniqueIdentifier = :anio " +
            "AND al.uniqueIdentifier = :alumno " +
            "AND (d.code like concat('%', :filter, '%') or x.code like concat('%', :filter, '%') or a.name like concat('%', :filter, '%'))")
    Optional<List<DocentexCursoEntity>> findDocentexCurso(String status, String alumno, String anio, String filter, Pageable pageable);

    @Query(value = "SELECT count(x) from DocenteEntity d " +
            "JOIN d.docentexCursoEntities x " +
            "JOIN x.aulaEntity au " +
            "JOIN x.cursoEntity c " +
            "JOIN x.anio_lectivoEntity a " +
            "JOIN au.matriculaEntities m " +
            "JOIN m.alumnoEntity al " +
            "WHERE d=x.docenteEntity " +
            "AND d.status = :status " +
            "AND x.status = :status " +
            "AND c.status = :status " +
            "AND a.uniqueIdentifier = :anio " +
            "AND al.uniqueIdentifier = :alumno " +
            "AND (d.code like concat('%', :filter, '%') or x.code like concat('%', :filter, '%') or a.name like concat('%', :filter, '%'))")
    Long findCountDocentexCurso(String status, String alumno, String anio, String filter);

    //Función para obtener un aula con su Identificado Único
    Optional<DocentexCursoEntity> findByUniqueIdentifier(String uniqueIdentifier);
    @Query(value = "SELECT x from DocenteEntity d " +
            "JOIN d.docentexCursoEntities x " +
            "JOIN x.cursoEntity c " +
            "WHERE d=x.docenteEntity " +
            "AND d.id = :id_docente " +
            "AND x.status = :status " +
            "AND d.status= :status ")
    Optional<List<DocentexCursoEntity>> findByDocente(Integer id_docente, String status);

    @Query(value = "SELECT x from DocenteEntity d " +
            "JOIN d.docentexCursoEntities x " +
            "JOIN x.cursoEntity c " +
            "WHERE d=x.docenteEntity " +
            "AND c.id = :id_curso " +
            "AND x.status = :status " +
            "AND c.status= :status ")
    Optional<List<DocentexCursoEntity>> findByCurso(Integer id_curso, String status);
    @Query(value = "SELECT a from AulaEntity a " +
            "JOIN a.docentexCursoEntities x " +
            "WHERE a=x.aulaEntity " +
            "AND a.uniqueIdentifier = :id_aula " +
            "AND x.status = :status " +
            "AND a.status= :status ")
    Optional<List<DocentexCursoEntity>> findByAula(String id_aula, String status);
    @Query(value = "SELECT x from DocenteEntity d " +
            "JOIN d.docentexCursoEntities x " +
            "JOIN x.cursoEntity c " +
            "WHERE d=x.docenteEntity " +
            "AND c=x.cursoEntity " +
            "AND d.id = :id_docente " +
            "AND c.id = :id_curso " +
            "AND x.status = :status " +
            "AND d.status = :status " +
            "AND c.status= :status ")
    Optional<List<DocentexCursoEntity>> findByDocenteYCurso(Integer id_docente, Integer id_curso, String status);

    @Query(value = "SELECT count(dxc)>0 FROM DocentexCursoEntity dxc " +
            "JOIN dxc.cursoEntity c " +
            "JOIN dxc.aulaEntity a " +
            "JOIN dxc.docenteEntity d " +
            "JOIN dxc.anio_lectivoEntity anio " +
            "WHERE dxc.uniqueIdentifier != :id " +
            "AND dxc.status = :status " +
            "AND c.status = :status " +
            "AND a.status = :status " +
            "AND d.status = :status " +
            "AND anio.status = :status " +
            "AND c.uniqueIdentifier = :idC " +
            "AND a.uniqueIdentifier = :idA " +
            "AND anio.uniqueIdentifier = :id_anio ")
    boolean existsByDocenteCursoAula(String id, String idC, String idA, String id_anio, String status);

    @Query(value="select dc.* from docentexcurso dc JOIN aula a ON a.id = dc.aula_id JOIN curso c ON c.id = dc.curso_id " +
            "JOIN anio_lectivo al ON al.id = dc.anio_lectivo_id " +
            "Where dc.tx_status = :status and a.tx_status = :status and c.tx_status = :status and al.tx_status = :status " +
            "and c.tx_unique_identifier like  concat('%', :curso, '%') " +
            "and al.tx_unique_identifier like concat('%', :anio, '%') " +
            "and a.tx_unique_identifier like  concat('%', :aula, '%') ", nativeQuery = true)
    Optional<DocentexCursoEntity> findByAulaCurso(String status, String anio, String aula, String curso);

    @Query(value="Select dc.* from docentexcurso dc JOIN docente d ON dc.docente_id = d.id JOIN anio_lectivo al " +
            "ON al.id = dc.anio_lectivo_id " +
            "WHERE dc.tx_status = :status AND al.tx_status = :status AND d.tx_status = :status " +
            "AND d.tx_unique_identifier like concat('%',:docente,'%') " +
            "AND al.tx_unique_identifier LIKE concat('%',:anio,'%')", nativeQuery = true)
    Optional<List<DocentexCursoEntity>> findByDocenteAnio(String status, String docente, String anio, Pageable pageable);

    @Query(value="Select count(*) from docentexcurso dc JOIN docente d ON dc.docente_id = d.id JOIN anio_lectivo al " +
            "ON al.id = dc.anio_lectivo_id " +
            "WHERE dc.tx_status = :status AND al.tx_status = :status AND d.tx_status = :status " +
            "AND d.tx_unique_identifier like concat('%',:docente,'%') " +
            "AND al.tx_unique_identifier LIKE concat('%',:anio,'%')", nativeQuery = true)
    Long countFindByDocenteAnio(String status, String docente, String anio);

    @Query(value="Select dc.* from docentexcurso dc " +
            "JOIN docente d ON dc.docente_id = d.id " +
            "JOIN anio_lectivo al ON al.id = dc.anio_lectivo_id " +
            "JOIN aula au ON au.id = dc.aula_id " +
            "WHERE dc.tx_status = :status " +
            "AND al.tx_status = :status " +
            "AND au.tx_status = :status " +
            "AND d.tx_status = :status " +
            "AND d.tx_unique_identifier like concat('%',:docente,'%') " +
            "AND au.tx_unique_identifier like concat('%',:aula,'%') " +
            "AND al.tx_unique_identifier LIKE concat('%',:anio,'%')", nativeQuery = true)
    Optional<List<DocentexCursoEntity>> findByDocenteAulaAnio(String status, String docente, String aula, String anio, Pageable pageable);
    @Query(value="Select count(dc.*) from docentexcurso dc " +
            "JOIN docente d ON dc.docente_id = d.id " +
            "JOIN anio_lectivo al ON al.id = dc.anio_lectivo_id " +
            "JOIN aula au ON au.id = dc.aula_id " +
            "WHERE dc.tx_status = :status " +
            "AND al.tx_status = :status " +
            "AND au.tx_status = :status " +
            "AND d.tx_status = :status " +
            "AND d.tx_unique_identifier like concat('%',:docente,'%') " +
            "AND au.tx_unique_identifier like concat('%',:aula,'%') " +
            "AND al.tx_unique_identifier LIKE concat('%',:anio,'%')", nativeQuery = true)
    Long countByDocenteAulaAnio(String status, String docente, String aula, String anio);

}
