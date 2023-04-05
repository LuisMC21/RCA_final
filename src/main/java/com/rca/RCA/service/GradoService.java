package com.rca.RCA.service;

import com.rca.RCA.entity.AulaEntity;
import com.rca.RCA.entity.GradoEntity;
import com.rca.RCA.repository.AulaRepository;
import com.rca.RCA.repository.GradoRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.GradoDTO;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
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
    @Autowired
    private AulaRepository aulaRepository;
    @Autowired
    private AulaService aulaService;

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
        //comprobar que el grado no exista
        Optional<GradoEntity> optionalGradoEntity = this.gradoRepository.findByName(gradoDTO.getName());
        if (optionalGradoEntity.isPresent() && optionalGradoEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
            log.warn("No se completó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("GRADE_EXISTS");
            apiResponse.setMessage("No se resgistró, el grado existe");
            return apiResponse;
        }
        //agregar datos de auditoria
        gradoDTO.setId(UUID.randomUUID().toString());
        gradoDTO.setCode(Code.generateCode(Code.GRADE_CODE, this.gradoRepository.count() + 1, Code.GRADE_LENGTH));
        gradoDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        gradoDTO.setCreateAt(LocalDateTime.now());
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
        Optional<GradoEntity> optionalGradoEntity=this.gradoRepository.findByName(gradoDTO.getName());
        //Verifica que el nombre del grado no exista
        if(optionalGradoEntity.isEmpty() || optionalGradoEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
            optionalGradoEntity = this.gradoRepository.findByUniqueIdentifier(gradoDTO.getId(), ConstantsGeneric.CREATED_STATUS);
            //Verifica que el id y el status seas válidos
            if (optionalGradoEntity.isPresent() && optionalGradoEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
                gradoDTO.setUpdateAt(LocalDateTime.now());
                GradoEntity gradoEntity = optionalGradoEntity.get();
                //Set update data
                if (gradoDTO.getCode() != null) {
                    gradoEntity.setCode(gradoDTO.getCode());
                }
                if (gradoDTO.getName() != null) {
                    gradoEntity.setName(gradoDTO.getName());
                }
                gradoEntity.setUpdateAt(gradoDTO.getUpdateAt());
                //Update in database
                apiResponse.setSuccessful(true);
                apiResponse.setMessage("ok");
                apiResponse.setData(this.gradoRepository.save(gradoEntity).getGradoDTO());
                return apiResponse;
            } else {
                apiResponse.setMessage("No existe el grado para poder actualizar");
                apiResponse.setCode("GRADE_DOES_NOT_EXISTS");
            }
        } else{
            apiResponse.setMessage("No se pudo actualizar, grado existente");
            apiResponse.setCode("GRADE_EXISTS");
        }
        log.warn("No se actualizó el dato");
        apiResponse.setSuccessful(false);
        return apiResponse;
    }
    //Función para actualizar un grado- END

    //Función para cambiar estado a eliminado de un grado - START
    //id dto=uniqueIdentifier Entity
    public ApiResponse<GradoDTO> delete(String id) throws ResourceNotFoundException, AttributeException {
        ApiResponse<GradoDTO> apiResponse = new ApiResponse<>();
        GradoEntity gradoEntity=this.gradoRepository.findByUniqueIdentifier(id, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Grado no encontrado"));
        gradoEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
        gradoEntity.setDeleteAt(LocalDateTime.now());
        //Eliminar lista de aulas del grado
        List<AulaEntity> aulaEntities= this.aulaRepository.findById_Grado(gradoEntity.getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new AttributeException("Grado: No encontrado"));
        for (int i = 0; i < aulaEntities.size(); i++) {
            aulaEntities.get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
            aulaEntities.get(i).setDeleteAt(gradoEntity.getDeleteAt());
            this.aulaService.delete(aulaEntities.get(i).getCode());
        }
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.gradoRepository.save(gradoEntity).getGradoDTO());

        return apiResponse;
    }
    //Función para cambiar estado a elimiado de un grado con respuesta de grado DTO - END
}
