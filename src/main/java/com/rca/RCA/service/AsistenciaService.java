package com.rca.RCA.service;

import com.rca.RCA.entity.*;
import com.rca.RCA.entity.AsistenciaEntity;
import com.rca.RCA.repository.AlumnoRepository;
import com.rca.RCA.repository.AsistenciaRepository;
import com.rca.RCA.repository.ClaseRepository;
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
    @Autowired
    private AlumnoRepository alumnoRepository;
    @Autowired
    private ClaseRepository claseRepository;

    public AsistenciaService(AsistenciaRepository asistenciaRepository, AlumnoRepository alumnoRepository, ClaseRepository claseRepository){
        this.asistenciaRepository = asistenciaRepository;
        this.alumnoRepository = alumnoRepository;
        this.claseRepository = claseRepository;
    }

    //Listar asistencia
    public ApiResponse<Pagination<AsistenciaDTO>> getList(String filter, int page, int size) {
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<AsistenciaDTO>> apiResponse = new ApiResponse<>();
        Pagination<AsistenciaDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.asistenciaRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<AsistenciaEntity> AsistenciaEntities = this.asistenciaRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(AsistenciaEntities.stream().map(AsistenciaEntity::getAsistenciaDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Agregar Asistencia
    public ApiResponse<AsistenciaDTO> add(AsistenciaDTO AsistenciaDTO) {
        ApiResponse<AsistenciaDTO> apiResponse = new ApiResponse<>();
        System.out.println(AsistenciaDTO.toString());
        AsistenciaDTO.setId(UUID.randomUUID().toString());
        AsistenciaDTO.setCode(Code.generateCode(Code.ASIS_CODE, this.asistenciaRepository.count() + 1, Code.ASIS_LENGTH));
        AsistenciaDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        AsistenciaDTO.setCreateAt(LocalDateTime.now());
        System.out.println(AsistenciaDTO.toString());

        //change dto to entity
        AsistenciaEntity AsistenciaEntity = new AsistenciaEntity();
        AsistenciaEntity.setAsistenciaDTO(AsistenciaDTO);

        //set usuario
        Optional<AlumnoEntity> optionalAlumnoEntity = this.alumnoRepository.findByUniqueIdentifier(AsistenciaDTO.getAlumnoDTO().getId());
        if (optionalAlumnoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("alumno_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el alumno asociado a la Asistencia no existe");
            return apiResponse;
        }

        //set clase
        Optional<ClaseEntity> optionalClaseEntity = this.claseRepository.findByUniqueIdentifier(AsistenciaDTO.getClaseDTO().getId());
        if (optionalClaseEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("CLASE_NOT_EXISTS");
            apiResponse.setMessage("No se registró, la clase asociada a la Asistencia no existe");
            return apiResponse;
        }

        AsistenciaEntity.setAlumnoEntity(optionalAlumnoEntity.get());
        AsistenciaEntity.setClaseEntity(optionalClaseEntity.get());
        apiResponse.setData(this.asistenciaRepository.save(AsistenciaEntity).getAsistenciaDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar Asistencia
    public ApiResponse<AsistenciaDTO> update(AsistenciaDTO AsistenciaDTO) {
        ApiResponse<AsistenciaDTO> apiResponse = new ApiResponse<>();
        System.out.println(AsistenciaDTO.toString());

        Optional<AsistenciaEntity> optionalAsistenciaEntity = this.asistenciaRepository.findByUniqueIdentifier(AsistenciaDTO.getId());
        if (optionalAsistenciaEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Asistencia_NOT_EXISTS");
            apiResponse.setMessage("No se encontro la Asistencia");
            return apiResponse;
        }

        //change dto to entity
        AsistenciaEntity AsistenciaEntity = optionalAsistenciaEntity.get();
        AsistenciaEntity.setState(AsistenciaDTO.getState());

        //set alumno
        Optional<AlumnoEntity> optionalAlumnoEntity = this.alumnoRepository.findByUniqueIdentifier(AsistenciaDTO.getAlumnoDTO().getId());
        if (optionalAlumnoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Alumno_NOT_EXISTS");
            apiResponse.setMessage("No se registro, el alumno asociada a la Asistencia no existe");
            return apiResponse;
        }

        //set Clase
        Optional<ClaseEntity> optionalClaseEntity = this.claseRepository.findByUniqueIdentifier(AsistenciaDTO.getClaseDTO().getId());
        if (optionalClaseEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("CLASE_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el apoderado asociado al Asistencia no existe");
            return apiResponse;
        }

        AsistenciaEntity.setAlumnoEntity(optionalAlumnoEntity.get());
        AsistenciaEntity.setClaseEntity(optionalClaseEntity.get());
        apiResponse.setData(this.asistenciaRepository.save(AsistenciaEntity).getAsistenciaDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Borrar asistencia
    public ApiResponse<AsistenciaDTO> delete(String id) {

        ApiResponse<AsistenciaDTO> apiResponse = new ApiResponse<>();
        Optional<AsistenciaEntity> optionalAsistenciaEntity = this.asistenciaRepository.findByUniqueIdentifier(id);
        if (optionalAsistenciaEntity.isPresent()) {
            AsistenciaEntity AsistenciaEntity = optionalAsistenciaEntity.get();
            AsistenciaEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            AsistenciaEntity.setDeleteAt(LocalDateTime.now());

            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.asistenciaRepository.save(AsistenciaEntity).getAsistenciaDTO());
        } else {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ROL_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existela asistencia para poder eliminar");
        }

        return  apiResponse;
    }
}
