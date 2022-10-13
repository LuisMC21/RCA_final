package com.rca.RCA.service;

import com.rca.RCA.entity.GradoEntity;
import com.rca.RCA.repository.GradoRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.GradoDTO;
import com.rca.RCA.type.Pagination;
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
public class GradoService {

    @Autowired
    private GradoRepository gradoRepository;

    //Función para listar grados con filtro(código o nombre)-START
    public ApiResponse<Pagination<GradoDTO>> getList(String filter, int page, int size){
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<GradoDTO>> apiResponse = new ApiResponse<>();
        Pagination<GradoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.gradoRepository.findCountGrados(ConstantsGeneric.CREATED_STATUS, filter));
        if(pagination.getCountFilter()>0){
            Pageable pageable= PageRequest.of(page, size);
            List<GradoEntity> gradoEntities=this.gradoRepository.findGrados(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(gradoEntities.stream().map(GradoEntity::getGradoDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para listar grados con filtro(código o nombre)-END

    //Función para agregar un grado - START
    public ApiResponse<GradoDTO> add(GradoDTO gradoDTO){
        ApiResponse<GradoDTO> apiResponse = new ApiResponse<>();
        gradoDTO.setId(UUID.randomUUID().toString());
        gradoDTO.setCode(Code.generateCode(Code.GRADE_CODE, this.gradoRepository.count() + 1, Code.GRADE_LENGTH));
        gradoDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        gradoDTO.setCreateAt(LocalDateTime.now());

        Optional<GradoEntity> optionalGradoEntity = this.gradoRepository.findByName(gradoDTO.getName());
        if (optionalGradoEntity.isPresent()) {
            log.warn("No se completo el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("GRADE_EXISTS");
            apiResponse.setMessage("No se resgistró, el grado existe");
            return apiResponse;
        }

        //change DTO to entity
        GradoEntity gradoEntity =new GradoEntity();
        gradoEntity.setGradoDTO(gradoDTO);
        apiResponse.setData(this.gradoRepository.save(gradoEntity).getGradoDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para agregar un grado - END

    //Función para actualizar un grado- START
    public ApiResponse<GradoDTO> update(GradoDTO gradoDTO){
        ApiResponse<GradoDTO> apiResponse = new ApiResponse<>();

        Optional<GradoEntity> optionalGradoEntity=this.gradoRepository.findByUniqueIdentifier(gradoDTO.getId());
        if(optionalGradoEntity.isPresent()){
            gradoDTO.setUpdateAt(LocalDateTime.now());
            GradoEntity gradoEntity =optionalGradoEntity.get();
            //Set update data
            if(gradoDTO.getCode()!=null) {
                gradoEntity.setCode(gradoDTO.getCode());
            }
            if(gradoDTO.getName()!=null) {
                gradoEntity.setName(gradoDTO.getName());
            }
            gradoEntity.setUpdateAt(gradoDTO.getUpdateAt());
            //Update in database
            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.gradoRepository.save(gradoEntity).getGradoDTO());
            return apiResponse;
        }else{
            apiResponse.setSuccessful(false);
            apiResponse.setCode("GRADE_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe el grado para poder actualizar");
        }
        return apiResponse;
    }
    //Función para actualizar un grado- END

    //Función para cambiar estado a eliminado de un grado - START
    //id dto=uniqueIdentifier Entity
    public ApiResponse<GradoDTO> delete(String id){
        ApiResponse<GradoDTO> apiResponse = new ApiResponse<>();
        Optional<GradoEntity> optionalGradoEntity=this.gradoRepository.findByUniqueIdentifier(id);
        if(optionalGradoEntity.isPresent()){
            GradoEntity gradoEntity =optionalGradoEntity.get();
            gradoEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            gradoEntity.setDeleteAt(LocalDateTime.now());

            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.gradoRepository.save(gradoEntity).getGradoDTO());
        } else{
            apiResponse.setSuccessful(false);
            apiResponse.setCode("GRADE_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe el grado para poder eliminar");
        }
        return apiResponse;
    }
    //Función para cambiar estado a elimiado de un grado con respuesta de grado DTO- END
}
