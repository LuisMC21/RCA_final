package com.rca.RCA.service;

import com.rca.RCA.auth.entity.Rol;
import com.rca.RCA.auth.enums.RolNombre;
//import com.rca.RCA.entity.RolEntity;
import com.rca.RCA.auth.repository.RolRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;
    /*
    @Autowired
    private UsuarioRepository usuarioRepository;

    public ApiResponse<Pagination<RolDTO>> getList(String filter, int page, int size) {
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<RolDTO>> apiResponse = new ApiResponse<>();
        Pagination<RolDTO> pagination = new Pagination();
        pagination.setCountFilter(this.rolRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<RolEntity> RolEntities = this.rolRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(RolEntities.stream().map(RolEntity::getRolDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Agregar Rol
    public ApiResponse<RolDTO> add(RolDTO RolDTO) {
        ApiResponse<RolDTO> apiResponse = new ApiResponse<>();
        System.out.println(RolDTO.toString());
        RolDTO.setId(UUID.randomUUID().toString());
        RolDTO.setCode(Code.generateCode(Code.ROL_CODE, this.rolRepository.count() + 1, Code.ROL_LENGTH));
        RolDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        RolDTO.setCreateAt(LocalDateTime.now());
        //validamos
        Optional<RolEntity> optionalRolEntity = this.rolRepository.findByName(RolDTO.getName());
        if (optionalRolEntity.isPresent()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Rol_EXISTS");
            apiResponse.setMessage("No se registro, el rol existe");
            return apiResponse;
        }
        //change dto to entity
        RolEntity RolEntity = new RolEntity();
        RolEntity.setRolDTO(RolDTO);

        apiResponse.setData(this.rolRepository.save(RolEntity).getRolDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar Rol
    public ApiResponse<RolDTO> update(RolDTO RolDTO) {
        ApiResponse<RolDTO> apiResponse = new ApiResponse<>();
        System.out.println(RolDTO.toString());

        Optional<RolEntity> optionalRolEntity = this.rolRepository.findByUniqueIdentifier(RolDTO.getId());
        if (optionalRolEntity.isPresent()) {
            RolDTO.setUpdateAt(LocalDateTime.now());
            //validamos que no se repita
            Optional<RolEntity> optionalRolEntityValidation = this.rolRepository.findByName(RolDTO.getName(), RolDTO.getName());
            if (optionalRolEntityValidation.isPresent()) {
                apiResponse.setSuccessful(false);
                apiResponse.setCode("rol_NOT_EXISTS");
                apiResponse.setMessage("No se encontro el rol");
                return apiResponse;
            }
            RolEntity RolEntity = optionalRolEntity.get();
            //set update data
            if (RolDTO.getCode() != null) {
                RolEntity.setCode(RolDTO.getCode());
            }
            if (RolDTO.getName() != null) {
                RolEntity.setName(RolDTO.getName());
            }
            RolEntity.setUpdateAt(RolDTO.getUpdateAt());
            //update in database

            apiResponse.setData(this.rolRepository.save(RolEntity).getRolDTO());
            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
        } else {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("rol_NOT_EXISTS");
            apiResponse.setMessage("No se encontro el rol para actualizar");;
        }

        return apiResponse;
    }

    //Borrar Rol
    public ApiResponse<RolDTO> delete(String id) {
        ApiResponse<RolDTO> apiResponse = new ApiResponse<>();
        Optional<RolEntity> optionalRolEntity = this.rolRepository.findByUniqueIdentifier(id);
        Long usuarios = this.usuarioRepository.findCountEntitiesRol(ConstantsGeneric.CREATED_STATUS, id);
        if (optionalRolEntity.isPresent()) {

            if(usuarios > 0){
                apiResponse.setSuccessful(false);
                apiResponse.setCode("No se puede eliminar");
                apiResponse.setMessage("Existen usuarios asociados con este rol");
                return  apiResponse;
            }else{
                System.out.println(optionalRolEntity.get());
                RolEntity RolEntity = optionalRolEntity.get();
                RolEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
                RolEntity.setDeleteAt(LocalDateTime.now());

                apiResponse.setSuccessful(true);
                apiResponse.setMessage("ok");
                apiResponse.setData(this.rolRepository.save(RolEntity).getRolDTO());
            }

        } else {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ROL_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe el rol para poder eliminar");
        }

        return apiResponse;
    }
    */
    public Optional<Rol> getByRolNombre(RolNombre rolNombre){
        return rolRepository.findByRolNombre(rolNombre);
    }

    public void save(Rol rol){
        rolRepository.save(rol);
    }
}
