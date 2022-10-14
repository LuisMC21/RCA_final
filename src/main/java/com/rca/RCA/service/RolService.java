package com.rca.RCA.service;

import com.rca.RCA.entity.RolEntity;
import com.rca.RCA.repository.RolRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.RolDTO;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    //Listar los roles

    //Agregar Rol
    public ApiResponse<RolDTO> add(RolDTO RolDTO){
        ApiResponse<RolDTO> apiResponse = new ApiResponse<>();
        RolDTO.setId(UUID.randomUUID().toString());
        RolDTO.setCode(Code.generateCode(Code.ROL_CODE, this.rolRepository.count() + 1, Code.ROL_LENGTH));
        RolDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        RolDTO.setCreateAt(LocalDateTime.now());

        Optional<RolEntity> optionalRolEntity = this.rolRepository.findByName(RolDTO.getName());
        if (optionalRolEntity.isPresent()) {
            log.warn("No se completó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ROL_EXISTS");
            apiResponse.setMessage("No se resgistró, el Rol existe");
            return apiResponse;
        }
        //change DTO to entity
        RolEntity RolEntity =new RolEntity();
        RolEntity.setRolDTO(RolDTO);
        apiResponse.setData(this.rolRepository.save(RolEntity).getRolDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar rol
    public ApiResponse<RolDTO> update(RolDTO RolDTO){
        ApiResponse<RolDTO> apiResponse = new ApiResponse<>();

        Optional<RolEntity> optionalRolEntity=this.rolRepository.findByUniqueIdentifier(RolDTO.getId());
        if(optionalRolEntity.isPresent()){
            RolDTO.setUpdateAt(LocalDateTime.now());
            RolEntity RolEntity =optionalRolEntity.get();
            //Set update data
            if(RolDTO.getCode()!=null) {
                RolEntity.setCode(RolDTO.getCode());
            }
            if(RolDTO.getName()!=null) {
                RolEntity.setName(RolDTO.getName());
            }
            RolEntity.setUpdateAt(RolDTO.getUpdateAt());
            //Update in database
            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.rolRepository.save(RolEntity).getRolDTO());
            return apiResponse;
        }else{
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ROL_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe el Rol para poder actualizar");
        }
        return apiResponse;
    }

    //Eliminar rol
    public ApiResponse<RolDTO> delete(String id){
        ApiResponse<RolDTO> apiResponse = new ApiResponse<>();
        Optional<RolEntity> optionalRolEntity=this.rolRepository.findByUniqueIdentifier(id);
        if(optionalRolEntity.isPresent()){
            RolEntity RolEntity =optionalRolEntity.get();
            RolEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            RolEntity.setDeleteAt(LocalDateTime.now());

            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.rolRepository.save(RolEntity).getRolDTO());
        } else{
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ROL_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe el Rol para poder eliminar");
        }
        return apiResponse;
    }
}
