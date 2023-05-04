package com.rca.RCA.service;

import com.rca.RCA.entity.ApoderadoEntity;
import com.rca.RCA.entity.GradoEntity;
import com.rca.RCA.entity.UsuarioEntity;
import com.rca.RCA.repository.ApoderadoRepository;
import com.rca.RCA.repository.UsuarioRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.ApoderadoDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ApoderadoService {

    @Autowired
    private ApoderadoRepository apoderadoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public ApoderadoService(ApoderadoRepository apoderadoRepository, UsuarioRepository usuarioRepository){
        this.apoderadoRepository = apoderadoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ApiResponse<Pagination<ApoderadoDTO>> getList(String filter, int page, int size) {
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<ApoderadoDTO>> apiResponse = new ApiResponse<>();
        Pagination<ApoderadoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.apoderadoRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<ApoderadoEntity> ApoderadoEntities = this.apoderadoRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(ApoderadoEntities.stream().map(ApoderadoEntity::getApoderadoDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    public ApiResponse<ApoderadoDTO> one(String id) throws ResourceNotFoundException {
        ApoderadoEntity apoderadoEntity=this.apoderadoRepository.findByUniqueIdentifier(id).orElseThrow(()-> new ResourceNotFoundException("Apoderado no encontrado"));
        ApiResponse<ApoderadoDTO> apiResponse = new ApiResponse<>();
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(apoderadoEntity.getApoderadoDTO());
        return apiResponse;
    }

    //Agregar Apoderado
    public ApiResponse<ApoderadoDTO> add(ApoderadoDTO ApoderadoDTO) throws AttributeException, ResourceNotFoundException {
        ApiResponse<ApoderadoDTO> apiResponse = new ApiResponse<>();

        //Excepciones
        if(apoderadoRepository.existsByEmail(ConstantsGeneric.CREATED_STATUS, ApoderadoDTO.getEmail(), ApoderadoDTO.getId()))
            throw new AttributeException("El email ya existe");

        //Add data DTO
        ApoderadoDTO.setId(UUID.randomUUID().toString());
        ApoderadoDTO.setCode(Code.generateCode(Code.APO_CODE, this.usuarioRepository.count() + 1, Code.APO_LENGTH));
        ApoderadoDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        ApoderadoDTO.setCreateAt(LocalDateTime.now());

        //change dto to entity
        ApoderadoEntity ApoderadoEntity = new ApoderadoEntity();
        ApoderadoEntity.setApoderadoDTO(ApoderadoDTO);

        //Validar usuario
        UsuarioEntity optionalUsuarioEntity = this.usuarioRepository.findByUniqueIdentifier(ApoderadoDTO.getUsuarioDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Usuario no existe"));

        ApoderadoEntity.setUsuarioEntity(optionalUsuarioEntity);
        apiResponse.setData(this.apoderadoRepository.save(ApoderadoEntity).getApoderadoDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar Apoderado
    public ApiResponse<ApoderadoDTO> update(ApoderadoDTO ApoderadoDTO) throws ResourceNotFoundException, AttributeException {

        //Excepciones
        if(ApoderadoDTO.getId().isBlank())
            throw new ResourceNotFoundException("Periodo no existe");
        if(apoderadoRepository.existsByEmail(ConstantsGeneric.CREATED_STATUS, ApoderadoDTO.getEmail(), ApoderadoDTO.getId()))
            throw new AttributeException("Email ya existe");

        ApiResponse<ApoderadoDTO> apiResponse = new ApiResponse<>();

        ApoderadoEntity apoderadoEntity = this.apoderadoRepository.findByUniqueIdentifier(ApoderadoDTO.getId()).orElseThrow(()-> new ResourceNotFoundException("Apoderado no existe"));

        //change dto to entity
        ApoderadoEntity ApoderadoEntity = apoderadoEntity;
        ApoderadoEntity.setEmail(ApoderadoDTO.getEmail());
        ApoderadoEntity.setUpdateAt(LocalDateTime.now());

        //Validar usuario
        UsuarioEntity usuarioEntity = this.usuarioRepository.findByUniqueIdentifier(ApoderadoDTO.getUsuarioDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Usuario no existe"));
        ApoderadoEntity.setUsuarioEntity(usuarioEntity);

        apiResponse.setData(this.apoderadoRepository.save(ApoderadoEntity).getApoderadoDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }



    //Borrar Apoderado
    public ApiResponse<ApoderadoDTO> delete(String id) {
        ApiResponse<ApoderadoDTO> apiResponse = new ApiResponse<>();
        Optional<ApoderadoEntity> optionalApoderadoEntity = this.apoderadoRepository.findByUniqueIdentifier(id);
        if (optionalApoderadoEntity.isPresent()) {

            this.apoderadoRepository.eliminarUsuario(id, LocalDateTime.now());

            ApoderadoEntity ApoderadoEntity = optionalApoderadoEntity.get();
            ApoderadoEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            ApoderadoEntity.setDeleteAt(LocalDateTime.now());

            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.apoderadoRepository.save(ApoderadoEntity).getApoderadoDTO());
        } else {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("APODERADO_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe el rol para poder eliminar");;
        }

        return apiResponse;
    }
}
