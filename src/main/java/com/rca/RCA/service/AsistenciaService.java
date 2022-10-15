package com.rca.RCA.service;

import com.rca.RCA.entity.AsistenciaEntity;
import com.rca.RCA.repository.AsistenciaRepository;
import com.rca.RCA.type.*;
import com.rca.RCA.type.AsistenciaDTO;
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
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    public Pagination<AsistenciaDTO> getList(String filter, int page, int size) {

        Pagination<AsistenciaDTO> pagination = new Pagination();
        pagination.setCountFilter(this.asistenciaRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<AsistenciaEntity> AsistenciaEntities = this.asistenciaRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(AsistenciaEntities.stream().map(AsistenciaEntity::getAsistenciaDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        return pagination;
    }

    //Agregar asistencia
    public ApiResponse<AsistenciaDTO> add(AsistenciaDTO AsistenciaDTO) {
        ApiResponse<AsistenciaDTO> apiResponse = new ApiResponse<>();
        System.out.println(AsistenciaDTO.toString());
        AsistenciaDTO.setId(UUID.randomUUID().toString());
        AsistenciaDTO.setCode(Code.generateCode(Code.ASIS_CODE, this.asistenciaRepository.count() + 1, Code.ASIS_LENGTH));
        AsistenciaDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        AsistenciaDTO.setCreateAt(LocalDateTime.now());
        //validamos

        AsistenciaEntity AsistenciaEntity = new AsistenciaEntity();
        AsistenciaEntity.setAsistenciaDTO(AsistenciaDTO);

        apiResponse.setData(this.asistenciaRepository.save(AsistenciaEntity).getAsistenciaDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar asistencia
    public void update(AsistenciaDTO AsistenciaDTO) {
        Optional<AsistenciaEntity> optionalAsistenciaEntity = this.asistenciaRepository.findByUniqueIdentifier(AsistenciaDTO.getId());
        if (optionalAsistenciaEntity.isPresent()) {
            AsistenciaDTO.setUpdateAt(LocalDateTime.now());
            //validamos que no se repita

            AsistenciaEntity AsistenciaEntity = optionalAsistenciaEntity.get();
            //set update data
            if (AsistenciaDTO.getCode() != null) {
                AsistenciaEntity.setCode(AsistenciaDTO.getCode());
            }

            AsistenciaEntity.setUpdateAt(AsistenciaDTO.getUpdateAt());
            //update in database
            this.asistenciaRepository.save(AsistenciaEntity);
        } else {
            System.out.println("No existe la asistencia para poder actualizar");
        }
    }

    //Borrar asistencia
    public void delete(String id) {
        Optional<AsistenciaEntity> optionalAsistenciaEntity = this.asistenciaRepository.findByUniqueIdentifier(id);
        if (optionalAsistenciaEntity.isPresent()) {
            AsistenciaEntity AsistenciaEntity = optionalAsistenciaEntity.get();
            AsistenciaEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            AsistenciaEntity.setDeleteAt(LocalDateTime.now());
            this.asistenciaRepository.save(AsistenciaEntity);
        } else {
            System.out.println("No existe la Asistencia para poder eliminar");
        }
    }
}
