package com.rca.RCA.service;

import com.rca.RCA.entity.DocenteEntity;
import com.rca.RCA.entity.DocentexCursoEntity;
import com.rca.RCA.entity.RolEntity;
import com.rca.RCA.entity.UsuarioEntity;
import com.rca.RCA.repository.DocenteRepository;
import com.rca.RCA.repository.DocentexCursoRepository;
import com.rca.RCA.repository.RolRepository;
import com.rca.RCA.repository.UsuarioRepository;
import com.rca.RCA.type.*;
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
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private UsuarioService usuarioService;
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

    //Función para agregar docente-START
    public ApiResponse<DocenteDTO> add(DocenteDTO docenteDTO){
        ApiResponse<DocenteDTO> apiResponse = new ApiResponse<>();
        //Verifica que el rol sea docente
        Optional<RolEntity> optionalRolEntity=this.rolRepository.findByName("Docente");
        if (optionalRolEntity.isEmpty() || !optionalRolEntity.get().getName().equalsIgnoreCase("DOCENTE")) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ROLE_NOT_SUPPORTED");
            apiResponse.setMessage("No se resgistró, el rol docente no existe");
            return apiResponse;
        }
        //add usuario
        docenteDTO.getUsuarioDTO().setRolDTO(optionalRolEntity.get().getRolDTO());
        ApiResponse<UsuarioDTO> apiResponseU= this.usuarioService.add(docenteDTO.getUsuarioDTO());
        if (!apiResponseU.isSuccessful()) {
            log.warn("No se agregó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("DOCENTE_EXISTS");
            apiResponse.setMessage("No se resgistró, el docente existe");
            return apiResponse;
        }
        //add data docente DTO
        docenteDTO.setId(UUID.randomUUID().toString());
        docenteDTO.setCode(Code.generateCode(Code.TEACHER_CODE, this.docenteRepository.count() + 1, Code.TEACHER_LENGTH));
        docenteDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        docenteDTO.setCreateAt(LocalDateTime.now());
        //change DTO to entity
        DocenteEntity docenteEntity =new DocenteEntity();
        docenteEntity.setDocenteDTO(docenteDTO);
        //add usuario to docente
        docenteEntity.setUsuarioEntity(this.usuarioRepository.findByUniqueIdentifier(docenteDTO.getUsuarioDTO().getId()).get());
        //save docente
        apiResponse.setData(this.docenteRepository.save(docenteEntity).getDocenteDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para agregar docente-END

    //Función para actualizar docente-START
    public ApiResponse<DocenteDTO> update(DocenteDTO docenteDTO){
        ApiResponse<DocenteDTO> apiResponse = new ApiResponse<>();
        //Verifica que el id recibido no sea nulo
        if(!docenteDTO.getId().isEmpty()) {
            Optional<DocenteEntity> optionalDocenteEntity = this.docenteRepository.findByUniqueIdentifier(docenteDTO.getId());
            //Verifica que el docente exista y sea válido
            if (optionalDocenteEntity.isPresent() && optionalDocenteEntity.get().getStatus().equalsIgnoreCase(ConstantsGeneric.CREATED_STATUS) && optionalDocenteEntity.get().getUsuarioEntity().getStatus().equalsIgnoreCase(ConstantsGeneric.CREATED_STATUS)) {
                //Set update time
                optionalDocenteEntity.get().setUpdateAt(LocalDateTime.now());
                optionalDocenteEntity.get().getUsuarioEntity().setUpdateAt(optionalDocenteEntity.get().getUpdateAt());
                //Set update data
                if (docenteDTO.getExperience() != null) {
                    optionalDocenteEntity.get().setExperience(docenteDTO.getExperience());
                }
                if (docenteDTO.getDose() != null) {
                    optionalDocenteEntity.get().setDose(docenteDTO.getDose());
                }
                if (docenteDTO.getSpecialty() != null) {
                    optionalDocenteEntity.get().setSpecialty(docenteDTO.getSpecialty());
                }
                if (docenteDTO.getUsuarioDTO().getPa_surname() != null) {
                    optionalDocenteEntity.get().getUsuarioEntity().setPa_surname(docenteDTO.getUsuarioDTO().getPa_surname());
                }
                if (docenteDTO.getUsuarioDTO().getMa_surname() != null) {
                    optionalDocenteEntity.get().getUsuarioEntity().setMa_surname(docenteDTO.getUsuarioDTO().getMa_surname());
                }
                if (docenteDTO.getUsuarioDTO().getName() != null) {
                    optionalDocenteEntity.get().getUsuarioEntity().setName(docenteDTO.getUsuarioDTO().getName());
                }
                if (docenteDTO.getUsuarioDTO().getGra_inst() != null) {
                    optionalDocenteEntity.get().getUsuarioEntity().setGra_inst(docenteDTO.getUsuarioDTO().getGra_inst());
                }
                if (docenteDTO.getUsuarioDTO().getEmail_inst() != null) {
                    optionalDocenteEntity.get().getUsuarioEntity().setEmail_inst(docenteDTO.getUsuarioDTO().getEmail_inst());
                }
                //Update in database to usuario
                ApiResponse<UsuarioDTO> apiResponseU = this.usuarioService.update(optionalDocenteEntity.get().getUsuarioEntity().getUsuarioDTO());
                if (apiResponseU.isSuccessful()) {
                    //Update in database to docente
                    apiResponse.setSuccessful(true);
                    apiResponse.setMessage("ok");
                    apiResponse.setData(this.docenteRepository.save(optionalDocenteEntity.get()).getDocenteDTO());
                    return apiResponse;
                }
            } else {
                log.warn("No se actualizó el registro");
                apiResponse.setMessage("No se puedo actualizar, docente no existente");
                apiResponse.setCode("TEACHER_DOES_NOT_EXISTS");
                return apiResponse;
            }
        }
        log.warn("No se actualizó el registro");
        apiResponse.setMessage("No se puedo actualizar, docente no existente");
        apiResponse.setCode("TEACHER_DOES_NOT_EXISTS");
        apiResponse.setSuccessful(false);
        return apiResponse;
    }
    //Función para actualizar docente-END


    //Función para cambiar estado a eliminado- START
    //id dto=uniqueIdentifier Entity
    public ApiResponse<DocenteDTO> delete(String id){
        ApiResponse<DocenteDTO> apiResponse = new ApiResponse<>();
        //Verifica que el id y el status sean válidos
        Optional<DocenteEntity> optionalDocenteEntity=this.docenteRepository.findByUniqueIdentifier(id);

        if(optionalDocenteEntity.isPresent() && optionalDocenteEntity.get().getStatus().equalsIgnoreCase(ConstantsGeneric.CREATED_STATUS) && optionalDocenteEntity.get().getUsuarioEntity().getStatus().equalsIgnoreCase(ConstantsGeneric.CREATED_STATUS)){
            optionalDocenteEntity.get().getUsuarioEntity().setStatus(ConstantsGeneric.DELETED_STATUS);
            optionalDocenteEntity.get().getUsuarioEntity().setDeleteAt(LocalDateTime.now());
            DocenteEntity docenteEntity =optionalDocenteEntity.get();
            docenteEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            docenteEntity.setDeleteAt(LocalDateTime.now());
            Optional<List<DocentexCursoEntity>> optionalDocentexCursoEntities= this.docentexCursoRepository.findByDocente(docenteEntity.getId(), ConstantsGeneric.CREATED_STATUS);
            for(int i=0; i<optionalDocentexCursoEntities.get().size(); i++){
                optionalDocentexCursoEntities.get().get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
                optionalDocentexCursoEntities.get().get(i).setDeleteAt(docenteEntity.getDeleteAt());
                this.docentexCursoService.delete(optionalDocentexCursoEntities.get().get(i).getCode());
            }
            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.docenteRepository.save(docenteEntity).getDocenteDTO());
        } else{
            log.warn("No se eliminó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("SECTION_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe el docente para poder eliminar");
        }
        return apiResponse;
    }
    //Función para cambiar estado a eliminado- END
}
