package com.rca.RCA.service;

import com.rca.RCA.entity.AulaEntity;
import com.rca.RCA.entity.SeccionEntity;
import com.rca.RCA.repository.AulaRepository;
import com.rca.RCA.repository.SeccionRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.SeccionDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Log4j2
@Service
public class SeccionService {

    @Autowired
    private SeccionRepository seccionRepository;
    @Autowired
    private AulaRepository aulaRepository;
    @Autowired
    private AulaService aulaService;

    //Función para listar secciones con paginación-START
    public ApiResponse<Pagination<SeccionDTO>> getList(String filter, int page, int size){
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<SeccionDTO>> apiResponse = new ApiResponse<>();
        Pagination<SeccionDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.seccionRepository.findCountSeccion(ConstantsGeneric.CREATED_STATUS, filter));
        if(pagination.getCountFilter()>0){
            Pageable pageable= PageRequest.of(page, size);
            List<SeccionEntity> seccionEntities=this.seccionRepository.findSeccion(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(seccionEntities.stream().map(SeccionEntity::getSeccionDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para listar secciones con paginación-END
    public ApiResponse<SeccionDTO> one(String id) throws ResourceNotFoundException {
        SeccionEntity seccionEntity = this.seccionRepository.findByUniqueIdentifier(id, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Sección no existe"));
        ApiResponse<SeccionDTO> apiResponse = new ApiResponse<>();
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(seccionEntity.getSeccionDTO());
        return apiResponse;
    }
        //Función para agregar seccion-START
    public ApiResponse<SeccionDTO> add(SeccionDTO seccionDTO) throws AttributeException {
        //Excepciones
        if (seccionRepository.existsByName(ConstantsGeneric.CREATED_STATUS, seccionDTO.getName(), ""))
            throw new AttributeException("Sección ya existe");
        ApiResponse<SeccionDTO> apiResponse = new ApiResponse<>();
        seccionDTO.setId(UUID.randomUUID().toString());
        seccionDTO.setCode(Code.generateCode(Code.SECTION_CODE, this.seccionRepository.count() + 1, Code.SECTION_LENGTH));
        seccionDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        seccionDTO.setCreateAt(LocalDateTime.now());

        //change DTO to entity
        SeccionEntity seccionEntity =new SeccionEntity();
        seccionEntity.setSeccionDTO(seccionDTO);
        apiResponse.setData(this.seccionRepository.save(seccionEntity).getSeccionDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para agregar seccion-END

    //Función para actualizar seccion-START
    public ApiResponse<SeccionDTO> update(SeccionDTO seccionDTO) throws ResourceNotFoundException, AttributeException {
        //Excepciones
        if(seccionDTO.getId().isBlank())
            throw new ResourceNotFoundException("Sección no existe para poder actualizar");
        if(seccionRepository.existsByName(ConstantsGeneric.CREATED_STATUS, seccionDTO.getName(), seccionDTO.getId()))
            throw new AttributeException("Sección existente");

        ApiResponse<SeccionDTO> apiResponse = new ApiResponse<>();
        seccionDTO.setUpdateAt(LocalDateTime.now());
        //Set update data
        SeccionEntity seccionEntity = this.seccionRepository.findByUniqueIdentifier(seccionDTO.getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Sección no existe"));
        seccionEntity.setCode(seccionDTO.getCode());
        seccionEntity.setName(seccionDTO.getName());
        seccionEntity.setUpdateAt(seccionDTO.getUpdateAt());
        //Update in database
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.seccionRepository.save(seccionEntity).getSeccionDTO());
        return apiResponse;
    }
    //Función para actualizar seccion-END


    //Función para cambiar estado a eliminado- START
    //id dto=uniqueIdentifier Entity
    public ApiResponse<SeccionDTO> delete(String id) throws ResourceNotFoundException, AttributeException {
        SeccionEntity seccionEntity=this.seccionRepository.findByUniqueIdentifier(id, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Sección no existe"));

        ApiResponse<SeccionDTO> apiResponse = new ApiResponse<>();
        //Verifica que el id y el status sean válidos
        seccionEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
        seccionEntity.setDeleteAt(LocalDateTime.now());
        List<AulaEntity> aulaEntities= this.aulaRepository.findById_Seccion(seccionEntity.getId(), ConstantsGeneric.CREATED_STATUS).orElse(new ArrayList<>());
        if(aulaEntities.size()>0 )
            throw new AttributeException("La sección tiene aula asignada, elimine primero el aula");
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.seccionRepository.save(seccionEntity).getSeccionDTO());
        return apiResponse;
    }
    //Función para cambiar estado a eliminado- END
}
