package com.rca.RCA.service;

import com.rca.RCA.entity.DocenteEntity;
import com.rca.RCA.repository.DocenteRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.DocenteDTO;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.UsuarioDTO;
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
public class DocenteService {

    @Autowired
    private DocenteRepository docenteRepository;

    //Función para listar secciones con paginación-START
    public ApiResponse<Pagination<DocenteDTO>> getList(String filter, int page, int size){
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<DocenteDTO>> apiResponse = new ApiResponse<>();
        Pagination<DocenteDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.docenteRepository.findCountSeccion(ConstantsGeneric.CREATED_STATUS, filter));
        if(pagination.getCountFilter()>0){
            Pageable pageable= PageRequest.of(page, size);
            List<DocenteEntity> docenteEntities=this.docenteRepository.findDocente(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(docenteEntities.stream().map(DocenteEntity::getDocenteDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para listar secciones con paginación-END

    //Función para agregar seccion-START
    public ApiResponse<DocenteDTO> add(UsuarioDTO usuarioDTO, DocenteDTO docenteDTO){
        System.out.println(usuarioDTO.getName());
        ApiResponse<DocenteDTO> apiResponse = new ApiResponse<>();
        docenteDTO.setId(UUID.randomUUID().toString());
        docenteDTO.setCode(Code.generateCode(Code.SECTION_CODE, this.docenteRepository.count() + 1, Code.SECTION_LENGTH));
        docenteDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        docenteDTO.setCreateAt(LocalDateTime.now());

    /*    Optional<SeccionEntity> optionalSeccionEntity = this.docenteRepository.findByName(docenteDTO.getName());
        if (optionalSeccionEntity.isPresent()) {
            log.warn("No se agregó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("SECTION_EXISTS");
            apiResponse.setMessage("No se resgistró, la sección existe");
            return apiResponse;
        }
     */

        //change DTO to entity
        DocenteEntity docenteEntity =new DocenteEntity();
        docenteEntity.setDocenteDTO(docenteDTO);
        apiResponse.setData(this.docenteRepository.save(docenteEntity).getDocenteDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para agregar seccion-END
/*
    //Función para actualizar seccion-START
    public ApiResponse<SeccionDTO> update(SeccionDTO seccionDTO){
        ApiResponse<SeccionDTO> apiResponse = new ApiResponse<>();
        Optional<SeccionEntity> optionalSeccionEntity=this.docenteRepository.findByName(seccionDTO.getName());
        //Verifica que el nombre no exista
        if(optionalSeccionEntity.isEmpty()) {
            optionalSeccionEntity = this.docenteRepository.findByUniqueIdentifier(seccionDTO.getId());
            //Verifica que el id y el status sean válidos
            if (optionalSeccionEntity.isPresent()&& optionalSeccionEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
                seccionDTO.setUpdateAt(LocalDateTime.now());
                SeccionEntity seccionEntity = optionalSeccionEntity.get();
                //Set update data
                if (seccionDTO.getCode() != null) {
                    seccionEntity.setCode(seccionDTO.getCode());
                }
                if (seccionDTO.getName() != null) {
                    seccionEntity.setName(seccionDTO.getName());
                }
                seccionEntity.setUpdateAt(seccionDTO.getUpdateAt());
                //Update in database
                apiResponse.setSuccessful(true);
                apiResponse.setMessage("ok");
                apiResponse.setData(this.docenteRepository.save(seccionEntity).getSeccionDTO());
                return apiResponse;
            } else{
                apiResponse.setMessage("No existe la sección para poder actualizar");
                apiResponse.setCode("SECTION_DOES_NOT_EXISTS");
            }
        } else{
            apiResponse.setMessage("No se puedo actualizar, sección existente");
            apiResponse.setCode("SECTION_EXISTS");
        }
        log.warn("No se actualizó el registro");
        apiResponse.setSuccessful(false);
        return apiResponse;
    }
    //Función para actualizar seccion-END


    //Función para cambiar estado a eliminado- START
    //id dto=uniqueIdentifier Entity
    public ApiResponse<SeccionDTO> delete(String id){
        ApiResponse<SeccionDTO> apiResponse = new ApiResponse<>();
        //Verifica que el id y el status sean válidos
        Optional<SeccionEntity> optionalSeccionEntity=this.docenteRepository.findByUniqueIdentifier(id);
        if(optionalSeccionEntity.isPresent()){
            SeccionEntity seccionEntity =optionalSeccionEntity.get();
            seccionEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            seccionEntity.setDeleteAt(LocalDateTime.now());

            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.docenteRepository.save(seccionEntity).getSeccionDTO());
        } else{
            log.warn("No se eliminó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("SECTION_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe la sección para poder eliminar");
        }
        return apiResponse;
    }
    //Función para cambiar estado a eliminado- END
 */
}
