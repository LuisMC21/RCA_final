package com.rca.RCA.service;

import com.rca.RCA.auth.entity.Rol;
//import com.rca.RCA.entity.RolEntity;
import com.rca.RCA.auth.enums.RolNombre;
import com.rca.RCA.entity.GradoEntity;
import com.rca.RCA.entity.UsuarioEntity;
import com.rca.RCA.repository.ImagenRepository;
import com.rca.RCA.repository.NoticiaRepository;
import com.rca.RCA.auth.repository.RolRepository;
import com.rca.RCA.repository.UsuarioRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.GradoDTO;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.UsuarioDTO;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private NoticiaRepository noticiaRepository;

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

    public ApiResponse<UsuarioDTO> one(String id) throws ResourceNotFoundException {
        UsuarioEntity usuarioEntity=this.usuarioRepository.findByUniqueIdentifier(id, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Usuario no encontrado"));
        ApiResponse<UsuarioDTO> apiResponse = new ApiResponse<>();
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(usuarioEntity.getUsuarioDTO());
        return apiResponse;
    }

    //Modificar usuario
    public ApiResponse<UsuarioDTO> update(UsuarioDTO usuarioDTO) throws ResourceNotFoundException, AttributeException {
        //validamos
        if(usuarioDTO.getId().isBlank())
            throw new ResourceNotFoundException("Usuario no encontrado");
        if(this.usuarioRepository.existsByNumdoc(usuarioDTO.getNumdoc(), usuarioDTO.getId(), ConstantsGeneric.CREATED_STATUS))
            new ResourceNotFoundException("Usuario ya existe");
        if(this.usuarioRepository.existsByTel(usuarioDTO.getTel(),  usuarioDTO.getId(), ConstantsGeneric.CREATED_STATUS))
            throw new AttributeException("Usuario con telefono existente");
        if(this.usuarioRepository.existsByEmail(usuarioDTO.getEmail(),  usuarioDTO.getId(), ConstantsGeneric.CREATED_STATUS))
            throw new AttributeException("Usuario con email existente");
        if(this.usuarioRepository.existsByNombreUsuario(usuarioDTO.getNombreUsuario(),  usuarioDTO.getId(), ConstantsGeneric.CREATED_STATUS))
            throw new AttributeException("Usuario con telefono existente");

        ApiResponse<UsuarioDTO> apiResponse = new ApiResponse<>();
        UsuarioEntity usuarioEntity = this.usuarioRepository.findByUniqueIdentifier(usuarioDTO.getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Usuario no existe"));
        System.out.println(usuarioEntity.getUsuarioDTO());
        //change dto to entity
        usuarioEntity.setName(usuarioDTO.getName());
        usuarioEntity.setPa_surname(usuarioDTO.getPa_surname());
        usuarioEntity.setMa_surname(usuarioDTO.getMa_surname());
        usuarioEntity.setBirthdate(usuarioDTO.getBirthdate());
        usuarioEntity.setType_doc(usuarioDTO.getType_doc());
        usuarioEntity.setNumdoc(usuarioDTO.getNumdoc());
        usuarioEntity.setGra_inst(usuarioDTO.getGra_inst());
        usuarioEntity.setEmail(usuarioDTO.getEmail());
        usuarioEntity.setTel(usuarioDTO.getTel());
        //set category
        if(usuarioDTO.getRol().equalsIgnoreCase("ADMINISTRADOR")){
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_ADMIN).get());
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_TEACHER).get());
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_STUDENT).get());
        }
        if(usuarioDTO.getRol().equalsIgnoreCase("TEACHER")){
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_TEACHER).get());
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_STUDENT).get());
        }
        if(usuarioDTO.getRol().equalsIgnoreCase("STUDENT")){
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_STUDENT).get());
        }
        apiResponse.setData(this.usuarioRepository.save(usuarioEntity).getUsuarioDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Borrar usuario
    public ApiResponse<UsuarioDTO> delete(String id) {
        ApiResponse<UsuarioDTO> apiResponse = new ApiResponse<>();
        Optional<UsuarioEntity> optionalUsuarioEntity = this.usuarioRepository.findByUniqueIdentifier(id, ConstantsGeneric.CREATED_STATUS);
        Long imagenes = this.imagenRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS,id);
        Long noticias = this.noticiaRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, id);

        if (optionalUsuarioEntity.isPresent()) {
            //Eliminar imágenes asociadas al usuario eliminado
            if (imagenes > 0){
                this.usuarioRepository.deleteImagen(id, LocalDateTime.now());
            }

            if (noticias > 0){
                this.usuarioRepository.deleteNoticia(id, LocalDateTime.now());
            }

            //Eliminar usuario
            UsuarioEntity UsuarioEntity = optionalUsuarioEntity.get();
            UsuarioEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            UsuarioEntity.setDeleteAt(LocalDateTime.now());

            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.usuarioRepository.save(UsuarioEntity).getUsuarioDTO());

        } else {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("USUARIO_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe el usuario para poder eliminar");
            return apiResponse;
        }

        return apiResponse;
    }


}




