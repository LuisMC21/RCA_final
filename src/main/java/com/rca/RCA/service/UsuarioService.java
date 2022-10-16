package com.rca.RCA.service;

import com.rca.RCA.entity.UsuarioEntity;
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

    public Pagination<UsuarioDTO> getList(String filter, int page, int size) {

        Pagination<UsuarioDTO> pagination = new Pagination();
        pagination.setCountFilter(this.usuarioRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<UsuarioEntity> UsuarioEntities = this.usuarioRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(UsuarioEntities.stream().map(UsuarioEntity::getUsuarioDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        return pagination;
    }

    //Agregar usuario
    public ApiResponse<UsuarioDTO> add(UsuarioDTO UsuarioDTO) {
        ApiResponse<UsuarioDTO> apiResponse = new ApiResponse<>();
        System.out.println(UsuarioDTO.toString());
        UsuarioDTO.setId(UUID.randomUUID().toString());
        UsuarioDTO.setCode(Code.generateCode(Code.USUARIO_CODE, this.usuarioRepository.count() + 1, Code.USUARIO_LENGTH));
        UsuarioDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        UsuarioDTO.setCreateAt(LocalDateTime.now());
        //validamos
        Optional<UsuarioEntity> optionalUsuarioEntity = this.usuarioRepository.findByNumdoc(UsuarioDTO.getNumdoc());
        if (optionalUsuarioEntity.isPresent()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Usuario_EXISTS");
            apiResponse.setMessage("No se registro, el usuario existe");
            return apiResponse;
        }
        //change dto to entity
        UsuarioEntity UsuarioEntity = new UsuarioEntity();
        UsuarioEntity.setUsuarioDTO(UsuarioDTO);

        apiResponse.setData(this.usuarioRepository.save(UsuarioEntity).getUsuarioDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar usuario
    public void update(UsuarioDTO UsuarioDTO) {
        Optional<UsuarioEntity> optionalUsuarioEntity = this.usuarioRepository.findByUniqueIdentifier(UsuarioDTO.getId());
        if (optionalUsuarioEntity.isPresent()) {
            UsuarioDTO.setUpdateAt(LocalDateTime.now());
            //validamos que no se repita
            Optional<UsuarioEntity> optionalUsuarioEntityValidation = this.usuarioRepository.findByNumdoc(UsuarioDTO.getNumdoc(), UsuarioDTO.getId());
            if (optionalUsuarioEntityValidation.isPresent()) {
                System.out.println("No se actulizo, la categoria existe");
                return;
            }
            UsuarioEntity UsuarioEntity = optionalUsuarioEntity.get();
            //set update data
            if (UsuarioDTO.getCode() != null) {
                UsuarioEntity.setCode(UsuarioDTO.getCode());
            }
            if (UsuarioDTO.getName() != null) {
                UsuarioEntity.setName(UsuarioDTO.getName());
            }
            UsuarioEntity.setUpdateAt(UsuarioDTO.getUpdateAt());
            //update in database
            this.usuarioRepository.save(UsuarioEntity);
        } else {
            System.out.println("No existe la categoria para poder actualizar");
        }
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




