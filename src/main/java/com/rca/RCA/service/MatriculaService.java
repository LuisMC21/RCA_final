package com.rca.RCA.service;

import com.rca.RCA.entity.AlumnoEntity;
import com.rca.RCA.entity.AnioLectivoEntity;
import com.rca.RCA.entity.AulaEntity;
import com.rca.RCA.entity.MatriculaEntity;
import com.rca.RCA.repository.AlumnoRepository;
import com.rca.RCA.repository.AnioLectivoRepository;
import com.rca.RCA.repository.AulaRepository;
import com.rca.RCA.repository.MatriculaRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.MatriculaDTO;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
public class MatriculaService {
    @Autowired
    private MatriculaRepository matriculaRepository;
    @Autowired
    private AlumnoRepository alumnoRepository;
    @Autowired
    private AulaRepository aulaRepository;
    @Autowired
    private AnioLectivoRepository anioLectivoRepository;

    //Función para listar aulas con paginación-START
    public ApiResponse<Pagination<MatriculaDTO>> getList(String filter, int page, int size){
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<MatriculaDTO>> apiResponse = new ApiResponse<>();
        Pagination<MatriculaDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.matriculaRepository.findCountMatricula(ConstantsGeneric.CREATED_STATUS, filter));
        if(pagination.getCountFilter()>0){
            Pageable pageable= PageRequest.of(page, size);
            List<MatriculaEntity> matriculaEntities=this.matriculaRepository.findMatricula(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            log.info(matriculaEntities.size());
            pagination.setList(matriculaEntities.stream().map(MatriculaEntity::getMatriculaDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para listar aulas-END

    //Función para agregar un aula- START
    public ApiResponse<MatriculaDTO> add(MatriculaDTO matriculaDTO){
        log.info("Aula Alumno AnioLectivo {} {} {}", matriculaDTO.getAulaDTO().getId(), matriculaDTO.getAlumnoDTO().getId(), matriculaDTO.getAnioLectivoDTO().getId());
        ApiResponse<MatriculaDTO> apiResponse = new ApiResponse<>();
        MatriculaEntity matriculaEntity = new MatriculaEntity();
        Optional<AulaEntity> optionalAulaEntity=this.aulaRepository.findByUniqueIdentifier(matriculaDTO.getAulaDTO().getId());
        Optional<AlumnoEntity> optionalAlumnoEntity=this.alumnoRepository.findByUniqueIdentifier(matriculaDTO.getAlumnoDTO().getId());
        Optional<AnioLectivoEntity> optionalAnioLectivoEntity=this.anioLectivoRepository.findByUniqueIdentifier(matriculaDTO.getAnioLectivoDTO().getId());
        if(optionalAulaEntity.isPresent() && optionalAlumnoEntity.isPresent() && optionalAnioLectivoEntity.isPresent()){
            if(this.matriculaRepository.findByAuAlAn(matriculaDTO.getAulaDTO().getId(), matriculaDTO.getAlumnoDTO().getId(), matriculaDTO.getAnioLectivoDTO().getId(), ConstantsGeneric.CREATED_STATUS).isEmpty()) {
                //Update in database
                matriculaEntity.setCode(Code.generateCode(Code.CLASSROOM_CODE, this.matriculaRepository.count() + 1, Code.CLASSROOM_LENGTH));
                matriculaEntity.setAulaEntity(optionalAulaEntity.get());
                matriculaEntity.setAlumnoEntity(optionalAlumnoEntity.get());
                matriculaEntity.setAnio_lectivoEntity(optionalAnioLectivoEntity.get());
                matriculaEntity.setUniqueIdentifier(UUID.randomUUID().toString());
                matriculaEntity.setStatus(ConstantsGeneric.CREATED_STATUS);
                matriculaEntity.setCreateAt(LocalDateTime.now());
                apiResponse.setSuccessful(true);
                apiResponse.setMessage("ok");
                apiResponse.setData(this.matriculaRepository.save(matriculaEntity).getMatriculaDTO());
                return apiResponse;
            }else{
                log.warn("No se completó el registro");
                apiResponse.setSuccessful(false);
                apiResponse.setCode("ENROLLMENT_EXISTS");
            }
        }else{
            log.warn("No se completó el registro");
            apiResponse.setSuccessful(false);
            if(optionalAulaEntity.isEmpty()) {
                apiResponse.setCode("CLASSROOM_DOES_NOT_EXISTS");
            }
            if(optionalAlumnoEntity.isEmpty()) {
                apiResponse.setCode("STUDENT_DOES_NOT_EXISTS");
            }
            if(optionalAnioLectivoEntity.isEmpty()) {
                apiResponse.setCode("SCHOOL_YEAR_DOES_NOT_EXISTS");
            }
        }
        apiResponse.setMessage("No se pudo registrar la matrícula");
        return apiResponse;
    }
    //Función para agregar un aula- END
/*
      //Función para actualizar un aula-START
    public ApiResponse<AulaDTO> update(AulaDTO aulaDTO){
        ApiResponse<AulaDTO> apiResponse = new ApiResponse<>();
        if(!aulaDTO.getId().isEmpty()) {
            Optional<AulaEntity> optionalAulaEntity = this.matriculaRepository.findByUniqueIdentifier(aulaDTO.getId());
            //Verifica que el id y el status sean válidos
            if (optionalAulaEntity.isPresent() && optionalAulaEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
                Optional<GradoEntity> optionalGradoEntity=Optional.empty();
                Optional<SeccionEntity> optionalSeccionEntity = Optional.empty();
                if (aulaDTO.getGradoDTO().getId() != null) {
                    optionalGradoEntity = this.gradoRepository.findByUniqueIdentifier(aulaDTO.getGradoDTO().getId());
                }
                if (aulaDTO.getSeccionDTO().getId() != null) {
                    optionalSeccionEntity = this.seccionRepository.findByUniqueIdentifier(aulaDTO.getSeccionDTO().getId());
                }
                //Set update data
                if (optionalGradoEntity.isPresent()) {
                    optionalAulaEntity.get().setGradoEntity(optionalGradoEntity.get());
                }
                if (optionalSeccionEntity.isPresent()) {
                    optionalAulaEntity.get().setSeccionEntity(optionalSeccionEntity.get());
                }
                optionalAulaEntity.get().setUpdateAt(aulaDTO.getUpdateAt());
                //Update in database
                apiResponse.setSuccessful(true);
                apiResponse.setMessage("ok");
                apiResponse.setData(this.matriculaRepository.save(optionalAulaEntity.get()).getAulaDTO());
                return apiResponse;
            } else {
                log.warn("No se actualizó el registro");
                apiResponse.setSuccessful(false);
                apiResponse.setMessage("No existe el aula para poder actualizar");
                apiResponse.setCode("CLASSROOM_DOES_NOT_EXISTS");
                return apiResponse;
            }
        }
        log.warn("No se actualizó el registro");
        apiResponse.setSuccessful(false);
        apiResponse.setMessage("No existe el aula para poder actualizar");
        apiResponse.setCode("CLASSROOM_DOES_NOT_EXISTS");
        return apiResponse;
    }
    //Función para actualizar un aula-END

    //Función para cambiar estado a eliminado- START
    //id dto=uniqueIdentifier Entity
    public ApiResponse<AulaDTO> delete(String id){
        ApiResponse<AulaDTO> apiResponse = new ApiResponse<>();
        //Verifica que el id y el status sean válidos
        Optional<AulaEntity> optionalAulaEntity=this.matriculaRepository.findByUniqueIdentifier(id);
        if(optionalAulaEntity.isPresent() && optionalAulaEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)){
            AulaEntity aulaEntity =optionalAulaEntity.get();
            aulaEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            aulaEntity.setDeleteAt(LocalDateTime.now());

            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.matriculaRepository.save(aulaEntity).getAulaDTO());
        } else{
            log.warn("No se eliminó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("CLASSROOM_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe el aula para poder eliminar");
        }
        return apiResponse;
    }
    //Función para cambiar estado a eliminado- END

 */
}
