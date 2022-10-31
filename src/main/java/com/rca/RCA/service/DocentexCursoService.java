package com.rca.RCA.service;

import com.rca.RCA.entity.*;
import com.rca.RCA.repository.*;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.DocentexCursoDTO;
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
public class DocentexCursoService {
    @Autowired
    private DocentexCursoRepository docentexCursoRepository;
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private DocenteRepository docenteRepository;
    @Autowired
    private ClaseRepository claseRepository;
    @Autowired
    private ClaseService claseService;
    @Autowired
    private EvaluacionRepository evaluacionRepository;
    @Autowired
    private EvaluacionService evaluacionService;

    //Función para listar los cursos asignados a los docente-START
    public ApiResponse<Pagination<DocentexCursoDTO>> getList(String filter, int page, int size){
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<DocentexCursoDTO>> apiResponse = new ApiResponse<>();
        Pagination<DocentexCursoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.docentexCursoRepository.findCountDocentexCurso(ConstantsGeneric.CREATED_STATUS, filter));
        if(pagination.getCountFilter()>0){
            Pageable pageable= PageRequest.of(page, size);
            List<DocentexCursoEntity> docentexCursoEntities=this.docentexCursoRepository.findDocentexCurso(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            log.info(docentexCursoEntities.size());
            pagination.setList(docentexCursoEntities.stream().map(DocentexCursoEntity::getDocentexCursoDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para listar los cursos asignados a los docente-END
    //Función para agregar un curso asignado a un docente- START
    public ApiResponse<DocentexCursoDTO> add(DocentexCursoDTO docentexCursoDTO){
        log.info("Docente Curso {} {}", docentexCursoDTO.getDocenteDTO().getId(), docentexCursoDTO.getCursoDTO().getId());
        ApiResponse<DocentexCursoDTO> apiResponse = new ApiResponse<>();
        DocentexCursoEntity docentexCursoEntity = new DocentexCursoEntity();
        Optional<DocenteEntity> optionalDocenteEntity=this.docenteRepository.findByUniqueIdentifier(docentexCursoDTO.getDocenteDTO().getId());
        Optional<CursoEntity> optionalCursoEntity=this.cursoRepository.findByUniqueIdentifier(docentexCursoDTO.getCursoDTO().getId());
        if(optionalDocenteEntity.isPresent() && optionalCursoEntity.isPresent()){
            //Update in database
            docentexCursoEntity.setCode(Code.generateCode(Code.CXD_CODE, this.docentexCursoRepository.count() + 1,Code.CXD_LENGTH));
            docentexCursoEntity.setDocenteEntity(optionalDocenteEntity.get());
            docentexCursoEntity.setCursoEntity(optionalCursoEntity.get());
            docentexCursoEntity.setUniqueIdentifier(UUID.randomUUID().toString());
            docentexCursoEntity.setCursoEntity(docentexCursoEntity.getCursoEntity());
            docentexCursoEntity.setDocenteEntity(docentexCursoEntity.getDocenteEntity());
            docentexCursoEntity.setStatus(ConstantsGeneric.CREATED_STATUS);
            docentexCursoEntity.setCreateAt(LocalDateTime.now());
            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.docentexCursoRepository.save(docentexCursoEntity).getDocentexCursoDTO());
            return apiResponse;
        }else{
            log.warn("No se completó el registro");
            apiResponse.setSuccessful(false);
            if(optionalDocenteEntity.isEmpty()) {
                apiResponse.setCode("TEACHER_DOES_NOT_EXISTS");
            }
            if(optionalCursoEntity.isEmpty()) {
                apiResponse.setCode("COURSE_DOES_NOT_EXISTS");
            }
            apiResponse.setMessage("No se pudo registrar el curso al docente");
        }
        return apiResponse;
    }
    //Función para agregar un curso asignado a un docente- END

      //Función para actualizar un curso asignado a un docente-START
    public ApiResponse<DocentexCursoDTO> update(DocentexCursoDTO aulaDTO){
        ApiResponse<DocentexCursoDTO> apiResponse = new ApiResponse<>();
        if(!aulaDTO.getId().isEmpty()) {
            Optional<DocentexCursoEntity> optionalDocentexCursoEntity = this.docentexCursoRepository.findByUniqueIdentifier(aulaDTO.getId());
            //Verifica que el id y el status sean válidos
            if (optionalDocentexCursoEntity.isPresent() && optionalDocentexCursoEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
                optionalDocentexCursoEntity.get().setUpdateAt(aulaDTO.getUpdateAt());
                Optional<DocenteEntity> optionalDocenteEntity=Optional.empty();
                Optional<CursoEntity> optionalCursoEntity = Optional.empty();
                if (aulaDTO.getDocenteDTO().getId() != null) {
                    optionalDocenteEntity = this.docenteRepository.findByUniqueIdentifier(aulaDTO.getDocenteDTO().getId());
                }
                if (aulaDTO.getCursoDTO().getId() != null) {
                    optionalCursoEntity = this.cursoRepository.findByUniqueIdentifier(aulaDTO.getCursoDTO().getId());
                }
                //Set update data
                optionalDocenteEntity.ifPresent(docenteEntity -> optionalDocentexCursoEntity.get().setDocenteEntity(docenteEntity));
                optionalCursoEntity.ifPresent(cursoEntity -> optionalDocentexCursoEntity.get().setCursoEntity(cursoEntity));

                optionalDocentexCursoEntity.get().setUpdateAt(LocalDateTime.now());
                //Update in database
                apiResponse.setSuccessful(true);
                apiResponse.setMessage("ok");
                apiResponse.setData(this.docentexCursoRepository.save(optionalDocentexCursoEntity.get()).getDocentexCursoDTO());
                return apiResponse;
            }
        }
        log.warn("No se actualizó el registro");
        apiResponse.setSuccessful(false);
        apiResponse.setMessage("No existe el curso asignado al docente para poder actualizar");
        apiResponse.setCode("TEACHER_PER_COURSE_DOES_NOT_EXISTS");
        return apiResponse;
    }
    //Función para actualizar un curso asignado a un docente-END

    //Función para cambiar estado a eliminado- START
    //id dto=uniqueIdentifier Entity
    public ApiResponse<DocentexCursoDTO> delete(String id){
        ApiResponse<DocentexCursoDTO> apiResponse = new ApiResponse<>();
        //Verifica que el id y el status sean válidos
        Optional<DocentexCursoEntity> optionalDocentexCursoEntity=this.docentexCursoRepository.findByUniqueIdentifier(id);
        if(optionalDocentexCursoEntity.isPresent() && optionalDocentexCursoEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)){
            DocentexCursoEntity docentexCursoEntity =optionalDocentexCursoEntity.get();
            docentexCursoEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            docentexCursoEntity.setDeleteAt(LocalDateTime.now());
            //eliminar lista de clases
            Optional<List<ClaseEntity>> optionalClaseEntities= this.claseRepository.findById_DxC(docentexCursoEntity.getUniqueIdentifier(), ConstantsGeneric.CREATED_STATUS);
            for (int i = 0; i < optionalClaseEntities.get().size(); i++) {
                optionalClaseEntities.get().get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
                optionalClaseEntities.get().get(i).setDeleteAt(docentexCursoEntity.getDeleteAt());
                this.claseService.delete(optionalClaseEntities.get().get(i).getCode());
            }
            //eliminar lista de evaluaciones
            Optional<List<EvaluacionEntity>> optionalEvaluacionEntities= this.evaluacionRepository.findById_DXC(docentexCursoEntity.getUniqueIdentifier(), ConstantsGeneric.CREATED_STATUS);
            for (int i = 0; i < optionalEvaluacionEntities.get().size(); i++) {
                optionalEvaluacionEntities.get().get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
                optionalEvaluacionEntities.get().get(i).setDeleteAt(docentexCursoEntity.getDeleteAt());
                this.claseService.delete(optionalEvaluacionEntities.get().get(i).getCode());
            }
            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.docentexCursoRepository.save(docentexCursoEntity).getDocentexCursoDTO());
        } else{
            log.warn("No se eliminó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("TEACHER_PER_COURSE_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe el curso asignado al docente para poder eliminar");
        }
        return apiResponse;
    }
    //Función para cambiar estado a eliminado- END
}
