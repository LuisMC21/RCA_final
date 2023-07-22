package com.rca.RCA.service;

import com.rca.RCA.auth.entity.Rol;
import com.rca.RCA.auth.enums.RolNombre;
import com.rca.RCA.auth.service.LoginService;
import com.rca.RCA.entity.DocenteEntity;
import com.rca.RCA.entity.DocentexCursoEntity;
import com.rca.RCA.entity.UsuarioEntity;
import com.rca.RCA.repository.DocenteRepository;
import com.rca.RCA.repository.DocentexCursoRepository;
import com.rca.RCA.auth.repository.RolRepository;
import com.rca.RCA.repository.UsuarioRepository;
import com.rca.RCA.type.*;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
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
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LoginService loginService;
    @Autowired
    private DocentexCursoRepository docentexCursoRepository;
    @Autowired
    private DocentexCursoService docentexCursoService;

    //Función para listar docentes con paginación-START
    public ApiResponse<Pagination<DocenteDTO>> getList(String filter, int page, int size){
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<DocenteDTO>> apiResponse = new ApiResponse<>();
        Pagination<DocenteDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.docenteRepository.findCountDocente(ConstantsGeneric.CREATED_STATUS, filter));
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
    //Función para listar docentes con paginación-END

    public ApiResponse<DocenteDTO> one(String id) throws ResourceNotFoundException {
        ApiResponse<DocenteDTO> apiResponse = new ApiResponse<>();
        //Verifica que el id y el status sean válidos
        DocenteEntity docenteEntity = this.docenteRepository.findByUniqueIdentifier(id, ConstantsGeneric.CREATED_STATUS).orElseThrow(() -> new ResourceNotFoundException("Docente no existe"));
        apiResponse.setSuccessful(false);
        apiResponse.setData(docenteEntity.getDocenteDTO());
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para agregar docente-START
    @Transactional
    public ApiResponse<DocenteDTO> add(DocenteDTO docenteDTO) throws ResourceNotFoundException, AttributeException {
        ApiResponse<DocenteDTO> apiResponse = new ApiResponse<>();
        //Verifica que el rol sea docente
        if (!docenteDTO.getUsuarioDTO().getRol().equalsIgnoreCase("TEACHER"))
            throw new AttributeException("El rol es inválido");
        Rol optionalRolEntity= this.rolRepository.findByRolNombre(RolNombre.ROLE_TEACHER).orElseThrow(()-> new ResourceNotFoundException("Rol Inválido"));
        //add usuario
        //docenteDTO.getUsuarioDTO().setRolDTO(optionalRolEntity.get().getRolDTO());
        ApiResponse<UsuarioDTO> apiResponseU= this.loginService.add(docenteDTO.getUsuarioDTO());
        if (!apiResponseU.isSuccessful()) {
            log.warn("No se agregó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("DOCENTE_EXISTS");
            apiResponse.setMessage(apiResponseU.getMessage());
            return apiResponse;
        }
        //add data docente DTO
        docenteDTO.setId(UUID.randomUUID().toString());
        docenteDTO.setCode(Code.generateCode(Code.TEACHER_CODE, this.docenteRepository.count() + 1, Code.TEACHER_LENGTH));
        docenteDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        docenteDTO.setCreateAt(apiResponseU.getData().getCreateAt());
        //change DTO to entity
        DocenteEntity docenteEntity =new DocenteEntity();
        docenteEntity.setDocenteDTO(docenteDTO);
        //add usuario to docente
        docenteEntity.setUsuarioEntity(this.usuarioRepository.findByUniqueIdentifier(docenteDTO.getUsuarioDTO().getId(), ConstantsGeneric.CREATED_STATUS).get());
        //save docente
        docenteDTO = this.docenteRepository.save(docenteEntity).getDocenteDTO();
        docenteDTO.getUsuarioDTO().setPassword("CIFRADA");
        apiResponse.setData(docenteDTO);

        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para agregar docente-END

    //Función para actualizar docente-START
    @Transactional
    public ApiResponse<DocenteDTO> update(DocenteDTO docenteDTO) throws ResourceNotFoundException, AttributeException {
        if(docenteDTO.getId().isBlank())
            throw new ResourceNotFoundException("Docente no encontrado");

        UsuarioDTO usuarioDTO = this.docenteRepository.findUserByDocente(docenteDTO.getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Usuario no existe")).getUsuarioDTO();
        usuarioDTO.setRol(docenteDTO.getUsuarioDTO().getRol());

        ApiResponse<UsuarioDTO> apiResponseU = this.usuarioService.update(docenteDTO.getUsuarioDTO());
        ApiResponse<DocenteDTO> apiResponse = new ApiResponse<>();
        DocenteEntity docenteEntity = this.docenteRepository.findByUniqueIdentifier(docenteDTO.getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Docente no existe"));
        //Set update time
        docenteEntity.setUpdateAt(LocalDateTime.now());
        docenteEntity.getUsuarioEntity().setUpdateAt(docenteEntity.getUpdateAt());
        //Set update data
        docenteEntity.setExperience(docenteDTO.getExperience());
        docenteEntity.setDose(docenteDTO.getDose());
        docenteEntity.setSpecialty(docenteDTO.getSpecialty());
        docenteEntity.getUsuarioEntity().setPa_surname(docenteDTO.getUsuarioDTO().getPa_surname());
        docenteEntity.getUsuarioEntity().setMa_surname(docenteDTO.getUsuarioDTO().getMa_surname());
        docenteEntity.getUsuarioEntity().setName(docenteDTO.getUsuarioDTO().getName());
        docenteEntity.getUsuarioEntity().setGra_inst(docenteDTO.getUsuarioDTO().getGra_inst());
        docenteEntity.getUsuarioEntity().setTel(docenteDTO.getUsuarioDTO().getTel());
        docenteEntity.getUsuarioEntity().setEmail(docenteDTO.getUsuarioDTO().getEmail());
        if(docenteDTO.getUsuarioDTO().getPassword() != null)
            docenteEntity.getUsuarioEntity().setPassword(docenteDTO.getUsuarioDTO().getPassword());
        //Update in database to usuario
        if (apiResponseU.isSuccessful()) {
            //Update in database to docente
            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            docenteDTO = this.docenteRepository.save(docenteEntity).getDocenteDTO();
            docenteDTO.getUsuarioDTO().setPassword("CIFRADA");
            apiResponse.setData(docenteDTO);
            return apiResponse;
        } else {
            apiResponse.setSuccessful(false);
            apiResponse.setMessage(apiResponseU.getMessage());
            return apiResponse;
        }
    }
    //Función para actualizar docente-END
    
    //Función para cambiar estado a eliminado- START
    //id dto=uniqueIdentifier Entity
    @Transactional
    public ApiResponse<DocenteDTO> delete(String id) throws ResourceNotFoundException {
        ApiResponse<DocenteDTO> apiResponse = new ApiResponse<>();
        //Verifica que el id y el status sean válidos
        DocenteEntity docenteEntity=this.docenteRepository.findByUniqueIdentifier(id, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Docente no existe"));
        UsuarioEntity usuarioEntity= usuarioRepository.findByUniqueIdentifier(docenteEntity.getUsuarioEntity().getUniqueIdentifier(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Usuario no existe"));
        ApiResponse<UsuarioDTO> apiResponseU = usuarioService.delete(usuarioEntity.getUniqueIdentifier());
        if(!apiResponseU.isSuccessful()){
            apiResponse.setSuccessful(false);
            apiResponse.setMessage(apiResponseU.getMessage());
            return apiResponse;
        }
        Optional<List<DocentexCursoEntity>> optionalDocentexCursoEntities= this.docentexCursoRepository.findByDocente(docenteEntity.getId(), ConstantsGeneric.CREATED_STATUS);
        for (int i = 0; i < optionalDocentexCursoEntities.get().size(); i++) {
            optionalDocentexCursoEntities.get().get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
            optionalDocentexCursoEntities.get().get(i).setDeleteAt(docenteEntity.getDeleteAt());
            this.docentexCursoService.delete(optionalDocentexCursoEntities.get().get(i).getCode());
        }
        docenteEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
        docenteEntity.setDeleteAt(LocalDateTime.now());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        docenteEntity = this.docenteRepository.save(docenteEntity);
        docenteEntity.getDocenteDTO().getUsuarioDTO().setPassword("CIFRADA");
        apiResponse.setData(docenteEntity.getDocenteDTO());
        return apiResponse;
    }
    //Función para cambiar estado a eliminado- END
}
