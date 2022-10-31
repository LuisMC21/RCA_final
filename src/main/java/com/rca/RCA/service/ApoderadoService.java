package com.rca.RCA.service;

import com.rca.RCA.entity.ApoderadoEntity;
import com.rca.RCA.entity.UsuarioEntity;
import com.rca.RCA.repository.ApoderadoRepository;
import com.rca.RCA.repository.UsuarioRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.ApoderadoDTO;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.ApoderadoDTO;
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

    //Agregar Apoderado
    public ApiResponse<ApoderadoDTO> add(ApoderadoDTO ApoderadoDTO) {
        ApiResponse<ApoderadoDTO> apiResponse = new ApiResponse<>();
        System.out.println(ApoderadoDTO.toString());
        ApoderadoDTO.setId(UUID.randomUUID().toString());
        ApoderadoDTO.setCode(Code.generateCode(Code.APO_CODE, this.usuarioRepository.count() + 1, Code.APO_LENGTH));
        ApoderadoDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        ApoderadoDTO.setCreateAt(LocalDateTime.now());
        System.out.println(ApoderadoDTO.toString());
        //validamos
        Optional<ApoderadoEntity> optionalApoderadoEntity = this.apoderadoRepository.findByEmail(ApoderadoDTO.getEmail());
        if (optionalApoderadoEntity.isPresent()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Apoderado_EXISTS");
            apiResponse.setMessage("No se registró, el apoderado existe");
            return apiResponse;
        }
        //change dto to entity
        ApoderadoEntity ApoderadoEntity = new ApoderadoEntity();
        ApoderadoEntity.setApoderadoDTO(ApoderadoDTO);

        //set usaurio
        Optional<UsuarioEntity> optionalUsuarioEntity = this.usuarioRepository.findByUniqueIdentifier(ApoderadoDTO.getUsuarioDTO().getId());
        if (optionalUsuarioEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ROL_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el usaurio asociado a la Apoderado no existe");
            return apiResponse;
        }

        ApoderadoEntity.setUsuarioEntity(optionalUsuarioEntity.get());
        apiResponse.setData(this.apoderadoRepository.save(ApoderadoEntity).getApoderadoDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar Apoderado
    public ApiResponse<ApoderadoDTO> update(ApoderadoDTO ApoderadoDTO) {
        ApiResponse<ApoderadoDTO> apiResponse = new ApiResponse<>();
        System.out.println(ApoderadoDTO.toString());

        Optional<ApoderadoEntity> optionalApoderadoEntity = this.apoderadoRepository.findByUniqueIdentifier(ApoderadoDTO.getId());
        if (optionalApoderadoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Apoderado_NOT_EXISTS");
            apiResponse.setMessage("No se encontro la Apoderado");
            return apiResponse;
        }

        //validamos
        Optional<ApoderadoEntity> optionalApoderadoEntityValidation = this.apoderadoRepository.findByEmail(ApoderadoDTO.getEmail(), ApoderadoDTO.getId());
        if (optionalApoderadoEntityValidation.isPresent()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Apoderado_EXISTS");
            apiResponse.setMessage("No se actualizó, la Apoderado existe");
            return apiResponse;
        }

        //change dto to entity
        ApoderadoEntity ApoderadoEntity = optionalApoderadoEntity.get();
        ApoderadoEntity.setEmail(ApoderadoDTO.getEmail());

        //set rol
        Optional<UsuarioEntity> optionalUsuarioEntity = this.usuarioRepository.findByUniqueIdentifier(ApoderadoDTO.getUsuarioDTO().getId());
        if (optionalUsuarioEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("USUARIO_NOT_EXISTS");
            apiResponse.setMessage("No se registro, el usuario asociada a la Apoderado no existe");
            return apiResponse;
        }
        ApoderadoEntity.setUsuarioEntity(optionalUsuarioEntity.get());
        apiResponse.setData(this.apoderadoRepository.save(ApoderadoEntity).getApoderadoDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Borrar Apoderado
    public void delete(String id) {
        Optional<ApoderadoEntity> optionalApoderadoEntity = this.apoderadoRepository.findByUniqueIdentifier(id);
        if (optionalApoderadoEntity.isPresent()) {
            ApoderadoEntity ApoderadoEntity = optionalApoderadoEntity.get();
            ApoderadoEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            ApoderadoEntity.setDeleteAt(LocalDateTime.now());
            this.apoderadoRepository.save(ApoderadoEntity);
        } else {
            System.out.println("No existe el Apoderado para poder eliminar");
        }
    }
}
