package com.rca.RCA.service;

import com.rca.RCA.entity.CursoEntity;
import com.rca.RCA.repository.CursoRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.CursoDTO;
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
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    //Función para listar cursos con paginación-START
    public ApiResponse<Pagination<CursoDTO>> getList(String filter, int page, int size){
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<CursoDTO>> apiResponse = new ApiResponse<>();
        Pagination<CursoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.cursoRepository.findCountCurso(ConstantsGeneric.CREATED_STATUS, filter));
        if(pagination.getCountFilter()>0){
            Pageable pageable= PageRequest.of(page, size);
            List<CursoEntity> cursoEntities=this.cursoRepository.findCurso(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(cursoEntities.stream().map(CursoEntity::getCursoDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para listar cursos con paginación-END

    //Función para agregar un curso con paginación-START
    public ApiResponse<CursoDTO> add(CursoDTO cursoDTO){
        ApiResponse<CursoDTO> apiResponse = new ApiResponse<>();
        cursoDTO.setId(UUID.randomUUID().toString());
        cursoDTO.setCode(Code.generateCode(Code.COURSE_CODE, this.cursoRepository.count() + 1, Code.COURSE_LENGTH));
        cursoDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        cursoDTO.setCreateAt(LocalDateTime.now());

        Optional<CursoEntity> optionalCursoEntity = this.cursoRepository.findByName(cursoDTO.getName());
        if (optionalCursoEntity.isPresent()) {
            log.warn("No se agregó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("COURSE_EXISTS");
            apiResponse.setMessage("No se registró, el curso existe");
            return apiResponse;
        }

        //change DTO to entity
        CursoEntity cursoEntity =new CursoEntity();
        cursoEntity.setCursoDTO(cursoDTO);
        apiResponse.setData(this.cursoRepository.save(cursoEntity).getCursoDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para agregar un curso con paginación-END

    //Función para actualizar un curso-START
    public ApiResponse<CursoDTO> update(CursoDTO cursoDTO){
        ApiResponse<CursoDTO> apiResponse = new ApiResponse<>();
        Optional<CursoEntity> optionalCursoEntity=this.cursoRepository.findByName(cursoDTO.getName());
        //Verifica que el nombre no exista
        if(optionalCursoEntity.isEmpty()) {
            optionalCursoEntity = this.cursoRepository.findByUniqueIdentifier(cursoDTO.getId());
            //Verifica que el id y el status sean válidos
            if (optionalCursoEntity.isPresent()&& optionalCursoEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
                cursoDTO.setUpdateAt(LocalDateTime.now());
                CursoEntity cursoEntity = optionalCursoEntity.get();
                //Set update data
                if (cursoDTO.getCode() != null) {
                    cursoEntity.setCode(cursoDTO.getCode());
                }
                if (cursoDTO.getName() != null) {
                    cursoEntity.setName(cursoDTO.getName());
                }
                cursoEntity.setUpdateAt(cursoDTO.getUpdateAt());
                //Update in database
                apiResponse.setSuccessful(true);
                apiResponse.setMessage("ok");
                apiResponse.setData(this.cursoRepository.save(cursoEntity).getCursoDTO());
                return apiResponse;
            } else{
                apiResponse.setMessage("No existe el curso para poder actualizar");
                apiResponse.setCode("COURSE_DOES_NOT_EXISTS");
            }
        } else{
            apiResponse.setMessage("No se puedo actualizar, curso existente");
            apiResponse.setCode("COURSE_EXISTS");
        }
        log.warn("No se actualizó el registro");
        apiResponse.setSuccessful(false);
        return apiResponse;
    }
    //Función para actualizar un curso-END

    //Función para cambiar estado a eliminado- START
    //id dto=uniqueIdentifier Entity
    public ApiResponse<CursoDTO> delete(String id){
        ApiResponse<CursoDTO> apiResponse = new ApiResponse<>();
        //Verifica que el id y el status sean válidos
        Optional<CursoEntity> optionalCursoEntity=this.cursoRepository.findByUniqueIdentifier(id);
        if(optionalCursoEntity.isPresent()){
            CursoEntity cursoEntity =optionalCursoEntity.get();
            cursoEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            cursoEntity.setDeleteAt(LocalDateTime.now());

            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.cursoRepository.save(cursoEntity).getCursoDTO());
        } else{
            log.warn("No se eliminó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("COURSE_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe el curso para poder eliminar");
        }
        return apiResponse;
    }
    //Función para cambiar estado a eliminado- END
}
