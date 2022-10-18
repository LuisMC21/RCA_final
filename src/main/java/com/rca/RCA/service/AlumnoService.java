package com.rca.RCA.service;

import com.rca.RCA.entity.AlumnoEntity;
import com.rca.RCA.repository.AlumnoRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.AlumnoDTO;
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
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    public Pagination<AlumnoDTO> getList(String filter, int page, int size) {

        Pagination<AlumnoDTO> pagination = new Pagination();
        pagination.setCountFilter(this.alumnoRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<AlumnoEntity> AlumnoEntities = this.alumnoRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(AlumnoEntities.stream().map(AlumnoEntity::getAlumnoDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        return pagination;
    }

    //Agregar Alumno
    public ApiResponse<AlumnoDTO> add(AlumnoDTO AlumnoDTO) {
        ApiResponse<AlumnoDTO> apiResponse = new ApiResponse<>();
        System.out.println(AlumnoDTO.toString());
        AlumnoDTO.setId(UUID.randomUUID().toString());
        AlumnoDTO.setCode(Code.generateCode(Code.ALU_CODE, this.alumnoRepository.count() + 1, Code.ALU_LENGTH));
        AlumnoDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        AlumnoDTO.setCreateAt(LocalDateTime.now());
        /*validamos
        Optional<AlumnoEntity> optionalAlumnoEntity = this.alumnoRepository.findByCode(AlumnoDTO.getCode());
        if (optionalAlumnoEntity.isPresent()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Alumno_EXISTS");
            apiResponse.setMessage("No se registro, el Alumno existe");
            return apiResponse;
        }*/
        //change dto to entity
        AlumnoEntity AlumnoEntity = new AlumnoEntity();
        AlumnoEntity.setAlumnoDTO(AlumnoDTO);

        apiResponse.setData(this.alumnoRepository.save(AlumnoEntity).getAlumnoDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar Alumno
    public void update(AlumnoDTO AlumnoDTO) {
        Optional<AlumnoEntity> optionalAlumnoEntity = this.alumnoRepository.findByUniqueIdentifier(AlumnoDTO.getId());
        if (optionalAlumnoEntity.isPresent()) {
            AlumnoDTO.setUpdateAt(LocalDateTime.now());
            /*validamos que no se repita
            Optional<AlumnoEntity> optionalAlumnoEntityValidation = this.alumnoRepository.findByCode(AlumnoDTO.getCode(), AlumnoDTO.getId());
            if (optionalAlumnoEntityValidation.isPresent()) {
                System.out.println("No se actulizo, el alumno existe");
                return;
            }*/
            AlumnoEntity AlumnoEntity = optionalAlumnoEntity.get();
            //set update data
            if (AlumnoDTO.getCode() != null) {
                AlumnoEntity.setCode(AlumnoDTO.getCode());
            }

            AlumnoEntity.setUpdateAt(AlumnoDTO.getUpdateAt());
            //update in database
            this.alumnoRepository.save(AlumnoEntity);
        } else {
            System.out.println("No existe la categoria para poder actualizar");
        }
    }

    //Borrar Alumno
    public void delete(String id) {
        Optional<AlumnoEntity> optionalAlumnoEntity = this.alumnoRepository.findByUniqueIdentifier(id);
        if (optionalAlumnoEntity.isPresent()) {
            AlumnoEntity AlumnoEntity = optionalAlumnoEntity.get();
            AlumnoEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            AlumnoEntity.setDeleteAt(LocalDateTime.now());
            this.alumnoRepository.save(AlumnoEntity);
        } else {
            System.out.println("No existe el Alumno para poder eliminar");
        }
    }


}
