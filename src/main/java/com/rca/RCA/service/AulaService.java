package com.rca.RCA.service;

import com.rca.RCA.entity.*;
import com.rca.RCA.repository.*;
import com.rca.RCA.type.*;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class AulaService {
    @Autowired
    private AulaRepository aulaRepository;
    @Autowired
    private SeccionRepository seccionRepository;
    @Autowired
    private GradoRepository gradoRepository;
    @Autowired
    private ClaseRepository claseRepository;
    @Autowired
    private ClaseService claseService;
    @Autowired
    private MatriculaRepository matriculaRepository;
    @Autowired
    private MatriculaService matriculaService;

    //Función para listar aulas con paginación-START
    public ApiResponse<Pagination<AulaDTO>> getList(String filter, int page, int size){
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<AulaDTO>> apiResponse = new ApiResponse<>();
        Pagination<AulaDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.aulaRepository.findCountAula(ConstantsGeneric.CREATED_STATUS, filter));
        if(pagination.getCountFilter()>0){
            Pageable pageable= PageRequest.of(page, size);
            List<AulaEntity> aulaEntities=this.aulaRepository.findAula(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            log.info(aulaEntities.size());
            pagination.setList(aulaEntities.stream().map(AulaEntity::getAulaDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para listar aulas-END
    //Función para agregar un aula- START
    public ApiResponse<AulaDTO> add(AulaDTO aulaDTO){
        log.info("Grado Seccion {} {}", aulaDTO.getGradoDTO().getId(), aulaDTO.getSeccionDTO().getId());
        ApiResponse<AulaDTO> apiResponse = new ApiResponse<>();
        AulaEntity aulaEntity = new AulaEntity();
        Optional<GradoEntity> optionalGradoEntity=this.gradoRepository.findByUniqueIdentifier(aulaDTO.getGradoDTO().getId());
        Optional<SeccionEntity> optionalSeccionEntity=this.seccionRepository.findByUniqueIdentifier(aulaDTO.getSeccionDTO().getId());
        if(optionalGradoEntity.isPresent() &&
                optionalSeccionEntity.isPresent()
                && this.aulaRepository.findByGradoYSeccion(optionalGradoEntity.get().getId(),optionalSeccionEntity.get().getId(), ConstantsGeneric.CREATED_STATUS).isEmpty()
                && optionalGradoEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)
                && optionalSeccionEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)){
                //Update in database
                aulaEntity.setCode(Code.generateCode(Code.CLASSROOM_CODE, this.aulaRepository.count() + 1, Code.CLASSROOM_LENGTH));
                aulaEntity.setGradoEntity(optionalGradoEntity.get());
                aulaEntity.setSeccionEntity(optionalSeccionEntity.get());
                aulaEntity.setUniqueIdentifier(UUID.randomUUID().toString());
                aulaEntity.setStatus(ConstantsGeneric.CREATED_STATUS);
                aulaEntity.setCreateAt(LocalDateTime.now());
                apiResponse.setSuccessful(true);
                apiResponse.setMessage("ok");
                apiResponse.setData(this.aulaRepository.save(aulaEntity).getAulaDTO());
                return apiResponse;

        }else{
            log.warn("No se completó el registro");

            if(optionalGradoEntity.isEmpty() || optionalGradoEntity.get().getStatus().equals(ConstantsGeneric.DELETED_STATUS)) {
                apiResponse.setCode("GRADE_DOES_NOT_EXISTS");
            }
            if(optionalSeccionEntity.isEmpty() || optionalSeccionEntity.get().getStatus().equals(ConstantsGeneric.DELETED_STATUS)) {
                apiResponse.setCode("SECTION_DOES_NOT_EXISTS");
            }
            if(optionalGradoEntity.isPresent() && optionalSeccionEntity.isPresent()){
                if(this.aulaRepository.findByGradoYSeccion(optionalGradoEntity.get().getId(),optionalSeccionEntity.get().getId(), ConstantsGeneric.CREATED_STATUS).isPresent()){
                    apiResponse.setCode("CLASSROOM_EXISTS");
                }
            }

            apiResponse.setMessage("No se pudo registrar la sección en el grado");
        }
        return apiResponse;
    }
    //Función para agregar un aula- END

    //Función para actualizar un aula-START
    public ApiResponse<AulaDTO> update(AulaDTO aulaDTO){
        ApiResponse<AulaDTO> apiResponse = new ApiResponse<>();
        if(!aulaDTO.getId().isEmpty()) {
            Optional<AulaEntity> optionalAulaEntity = this.aulaRepository.findByUniqueIdentifier(aulaDTO.getId());
            //Verifica que el id y el status sean válidos
            if (optionalAulaEntity.isPresent()
                    && optionalAulaEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
                Optional<GradoEntity> optionalGradoEntity=Optional.empty();
                Optional<SeccionEntity> optionalSeccionEntity = Optional.empty();
                if (aulaDTO.getGradoDTO().getId() != null) {
                    optionalGradoEntity = this.gradoRepository.findByUniqueIdentifier(aulaDTO.getGradoDTO().getId());
                }
                if (aulaDTO.getSeccionDTO().getId() != null) {
                    optionalSeccionEntity = this.seccionRepository.findByUniqueIdentifier(aulaDTO.getSeccionDTO().getId());
                }
                //Set update data
                if (optionalGradoEntity.isPresent() && optionalGradoEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
                    optionalAulaEntity.get().setGradoEntity(optionalGradoEntity.get());
                } else{
                    log.warn("No se actualizó el registro");
                    apiResponse.setSuccessful(false);
                    apiResponse.setMessage("Grado no existente, no se puede actualizar");
                    apiResponse.setCode("GRADE_DOES_NOT_EXISTS");
                    return apiResponse;
                }
                if (optionalSeccionEntity.isPresent() && optionalSeccionEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
                    optionalAulaEntity.get().setSeccionEntity(optionalSeccionEntity.get());
                } else{
                    log.warn("No se actualizó el registro");
                    apiResponse.setSuccessful(false);
                    apiResponse.setMessage("Sección no existente, no se puede actualizar");
                    apiResponse.setCode("SECTION_DOES_NOT_EXISTS");
                    return apiResponse;
                }
                if(this.aulaRepository.findByGradoYSeccion(optionalAulaEntity.get().getGradoEntity().getId(), optionalAulaEntity.get().getSeccionEntity().getId(),ConstantsGeneric.CREATED_STATUS).isPresent()
                        && this.aulaRepository.findByGradoYSeccion(optionalAulaEntity.get().getGradoEntity().getId(), optionalAulaEntity.get().getSeccionEntity().getId(), ConstantsGeneric.CREATED_STATUS).get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)){
                    log.warn("No se actualizó el registro");
                    apiResponse.setSuccessful(false);
                    apiResponse.setMessage("Aula ya existente, no se puede actualizar");
                    apiResponse.setCode("CLASSROOM_EXISTS");
                    return apiResponse;
                }
                optionalAulaEntity.get().setUpdateAt(LocalDateTime.now());
                //Update in database
                apiResponse.setSuccessful(true);
                apiResponse.setMessage("ok");
                apiResponse.setData(this.aulaRepository.save(optionalAulaEntity.get()).getAulaDTO());
            } else {
                log.warn("No se actualizó el registro");
                apiResponse.setSuccessful(false);
                apiResponse.setMessage("No existe el aula para poder actualizar");
                apiResponse.setCode("CLASSROOM_DOES_NOT_EXISTS");
            }
            return apiResponse;
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
        Optional<AulaEntity> optionalAulaEntity=this.aulaRepository.findByUniqueIdentifier(id);
        if(optionalAulaEntity.isPresent() && optionalAulaEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)){
            AulaEntity aulaEntity =optionalAulaEntity.get();
            aulaEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            aulaEntity.setDeleteAt(LocalDateTime.now());
            //Eliminar lista de clases del aula
            Optional<List<ClaseEntity>> optionalClaseEntities= this.claseRepository.findByAula(aulaEntity.getUniqueIdentifier(), ConstantsGeneric.CREATED_STATUS);
            for(int i=0; i<optionalClaseEntities.get().size(); i++){
                optionalClaseEntities.get().get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
                optionalClaseEntities.get().get(i).setDeleteAt(aulaEntity.getDeleteAt());
                this.claseService.delete(optionalClaseEntities.get().get(i).getCode());
            }
            //Eliminar lista de matriculas del aula
            Optional<List<MatriculaEntity>> optionalMatriculaEntities= this.matriculaRepository.findByAula(aulaEntity.getUniqueIdentifier(), ConstantsGeneric.CREATED_STATUS);
            for(int i=0; i<optionalMatriculaEntities.get().size(); i++){
                optionalMatriculaEntities.get().get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
                optionalMatriculaEntities.get().get(i).setDeleteAt(aulaEntity.getDeleteAt());
                this.matriculaService.delete(optionalMatriculaEntities.get().get(i).getCode());
            }

            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.aulaRepository.save(aulaEntity).getAulaDTO());
        } else{
            log.warn("No se eliminó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("CLASSROOM_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe el aula para poder eliminar");
        }
        return apiResponse;
    }
    //Función para cambiar estado a eliminado- END
}
