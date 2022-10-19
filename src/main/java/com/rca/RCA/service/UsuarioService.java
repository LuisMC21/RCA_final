package com.rca.RCA.service;

import com.rca.RCA.entity.RolEntity;
import com.rca.RCA.entity.UsuarioEntity;
import com.rca.RCA.repository.RolRepository;
import com.rca.RCA.repository.UsuarioRepository;
import com.rca.RCA.type.ApiResponse;
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
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    private RolRepository rolRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository){
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    public ApiResponse<Pagination<UsuarioDTO>> getList(String filter, int page, int size) {
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<UsuarioDTO>> apiResponse = new ApiResponse<>();
        Pagination<UsuarioDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.usuarioRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<UsuarioEntity> usuarioEntities = this.usuarioRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(usuarioEntities.stream().map(UsuarioEntity::getUsuarioDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Agregar usuario
    public ApiResponse<UsuarioDTO> add(UsuarioDTO UsuarioDTO) {
        ApiResponse<UsuarioDTO> apiResponse = new ApiResponse<>();
        System.out.println(UsuarioDTO.toString());
        UsuarioDTO.setId(UUID.randomUUID().toString());
        UsuarioDTO.setCode(Code.generateCode(Code.USUARIO_CODE, this.usuarioRepository.count() + 1, Code.USUARIO_LENGTH));
        UsuarioDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        UsuarioDTO.setCreateAt(LocalDateTime.now());
        System.out.println(UsuarioDTO.toString());
        //validamos
        Optional<UsuarioEntity> optionalUsuarioEntity = this.usuarioRepository.findByNumdoc(UsuarioDTO.getNumdoc());
        if (optionalUsuarioEntity.isPresent()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Usuario_EXISTS");
            apiResponse.setMessage("No se registró, el usuario existe");
            return apiResponse;
        }
        //change dto to entity
        UsuarioEntity UsuarioEntity = new UsuarioEntity();
        UsuarioEntity.setUsuarioDTO(UsuarioDTO);

        //set rol
        Optional<RolEntity> optionalRolEntity = this.rolRepository.findByUniqueIdentifier(UsuarioDTO.getRolDTO().getId());
        if (optionalRolEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ROL_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el rol asociado al usuario no existe");
            return apiResponse;
        }

        UsuarioEntity.setRolEntity(optionalRolEntity.get());
        apiResponse.setData(this.usuarioRepository.save(UsuarioEntity).getUsuarioDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar usuario
    public ApiResponse<UsuarioDTO> update(UsuarioDTO UsuarioDTO) {
        ApiResponse<UsuarioDTO> apiResponse = new ApiResponse<>();
        System.out.println(UsuarioDTO.toString());

        Optional<UsuarioEntity> optionalUsuarioEntity = this.usuarioRepository.findByUniqueIdentifier(UsuarioDTO.getId());
        if (optionalUsuarioEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Usuario_NOT_EXISTS");
            apiResponse.setMessage("No se encontro el Usuario");
            return apiResponse;
        }

        //validamos
        Optional<UsuarioEntity> optionalUsuarioEntityValidation = this.usuarioRepository.findByNumdoc(UsuarioDTO.getNumdoc(), UsuarioDTO.getId());
        if (optionalUsuarioEntityValidation.isPresent()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Usuario_EXISTS");
            apiResponse.setMessage("No se actualizó, el Usuario existe");
            return apiResponse;
        }

        //change dto to entity
        UsuarioEntity UsuarioEntity = optionalUsuarioEntity.get();
        UsuarioEntity.setName(UsuarioDTO.getName());
        UsuarioEntity.setPa_surname(UsuarioDTO.getPa_surname());
        UsuarioEntity.setMa_surname(UsuarioDTO.getMa_surname());
        UsuarioEntity.setType_doc(UsuarioDTO.getType_doc());
        UsuarioEntity.setNumdoc(UsuarioDTO.getNumdoc());
        UsuarioEntity.setGra_inst(UsuarioDTO.getGra_inst());
        UsuarioEntity.setEmail_inst(UsuarioDTO.getEmail_inst());
        UsuarioEntity.setTel(UsuarioDTO.getTel());
        //set category
        Optional<RolEntity> optionalRolEntity = this.rolRepository.findByUniqueIdentifier(UsuarioDTO.getRolDTO().getId());
        if (optionalRolEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("CATEGORY_NOT_EXISTS");
            apiResponse.setMessage("No se registro, la categoria asociada al Usuarioo no existe");
            return apiResponse;
        }
        UsuarioEntity.setRolEntity(optionalRolEntity.get());
        apiResponse.setData(this.usuarioRepository.save(UsuarioEntity).getUsuarioDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Borrar usuario
    public void delete(String id) {
        Optional<UsuarioEntity> optionalUsuarioEntity = this.usuarioRepository.findByUniqueIdentifier(id);
        if (optionalUsuarioEntity.isPresent()) {
            UsuarioEntity UsuarioEntity = optionalUsuarioEntity.get();
            UsuarioEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            UsuarioEntity.setDeleteAt(LocalDateTime.now());
            this.usuarioRepository.save(UsuarioEntity);
        } else {
            System.out.println("No existe el usuario para poder eliminar");
        }
    }


}




