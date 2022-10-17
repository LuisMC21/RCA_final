package com.rca.RCA.service;

import com.rca.RCA.entity.ApoderadoEntity;
import com.rca.RCA.repository.ApoderadoRepository;
import com.rca.RCA.type.ApiResponse;
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

    public Pagination<ApoderadoDTO> getList(String filter, int page, int size) {

        Pagination<ApoderadoDTO> pagination = new Pagination();
        pagination.setCountFilter(this.apoderadoRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<ApoderadoEntity> ApoderadoEntities = this.apoderadoRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(ApoderadoEntities.stream().map(ApoderadoEntity::getApoderadoDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        return pagination;
    }

    //Agregar Apoderado
    public ApiResponse<ApoderadoDTO> add(ApoderadoDTO ApoderadoDTO) {
        ApiResponse<ApoderadoDTO> apiResponse = new ApiResponse<>();
        System.out.println(ApoderadoDTO.toString());
        ApoderadoDTO.setId(UUID.randomUUID().toString());
        ApoderadoDTO.setCode(Code.generateCode(Code.APO_CODE, this.apoderadoRepository.count() + 1, Code.APO_LENGTH));
        ApoderadoDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        ApoderadoDTO.setCreateAt(LocalDateTime.now());
        //validamos
        Optional<ApoderadoEntity> optionalApoderadoEntity = this.apoderadoRepository.findByEmail(ApoderadoDTO.getEmail());
        if (optionalApoderadoEntity.isPresent()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Apoderado_EXISTS");
            apiResponse.setMessage("No se registro, el Apoderado existe");
            return apiResponse;
        }
        //change dto to entity
        ApoderadoEntity ApoderadoEntity = new ApoderadoEntity();
        ApoderadoEntity.setApoderadoDTO(ApoderadoDTO);

        apiResponse.setData(this.apoderadoRepository.save(ApoderadoEntity).getApoderadoDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar Apoderado
    public void update(ApoderadoDTO ApoderadoDTO) {
        Optional<ApoderadoEntity> optionalApoderadoEntity = this.apoderadoRepository.findByUniqueIdentifier(ApoderadoDTO.getId());
        if (optionalApoderadoEntity.isPresent()) {
            ApoderadoDTO.setUpdateAt(LocalDateTime.now());
            //validamos que no se repita
            Optional<ApoderadoEntity> optionalApoderadoEntityValidation = this.apoderadoRepository.findByEmail(ApoderadoDTO.getEmail(), ApoderadoDTO.getId());
            if (optionalApoderadoEntityValidation.isPresent()) {
                System.out.println("No se actulizo, la categoria existe");
                return;
            }
            ApoderadoEntity ApoderadoEntity = optionalApoderadoEntity.get();
            //set update data
            if (ApoderadoDTO.getCode() != null) {
                ApoderadoEntity.setCode(ApoderadoDTO.getCode());
            }
            if (ApoderadoDTO.getEmail() != null) {
                ApoderadoEntity.setEmail(ApoderadoDTO.getEmail());
            }
            ApoderadoEntity.setUpdateAt(ApoderadoDTO.getUpdateAt());
            //update in database
            this.apoderadoRepository.save(ApoderadoEntity);
        } else {
            System.out.println("No existe el apoderado para poder actualizar");
        }
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
