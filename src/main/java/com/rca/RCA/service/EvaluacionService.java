package com.rca.RCA.service;

import com.rca.RCA.entity.*;
import com.rca.RCA.repository.*;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.EvaluacionDTO;
import com.rca.RCA.type.Pagination;
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
public class EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;
    private AlumnoRepository alumnoRepository;
    private DocentexCursoRepository docentexCursoRepository;
    private PeriodoRepository periodoRepository;

    public EvaluacionService(EvaluacionRepository evaluacionRepository, AlumnoRepository alumnoRepository,
                             DocentexCursoRepository docentexCursoRepository, PeriodoRepository periodoRepository){
        this.evaluacionRepository = evaluacionRepository;
        this.alumnoRepository = alumnoRepository;
        this.docentexCursoRepository = docentexCursoRepository;
        this.periodoRepository = periodoRepository;
    }

    //Obtener Evaluaciones
    public ApiResponse<Pagination<EvaluacionDTO>> getList(String filter, int page, int size) {
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<EvaluacionDTO>> apiResponse = new ApiResponse<>();
        Pagination<EvaluacionDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.evaluacionRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<EvaluacionEntity> EvaluacionEntities = this.evaluacionRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(EvaluacionEntities.stream().map(EvaluacionEntity::getEvaluacionDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Agreagar Evaluacion
    public ApiResponse<EvaluacionDTO> add(EvaluacionDTO EvaluacionDTO) {
        ApiResponse<EvaluacionDTO> apiResponse = new ApiResponse<>();
        System.out.println(EvaluacionDTO.toString());
        EvaluacionDTO.setId(UUID.randomUUID().toString());
        EvaluacionDTO.setCode(Code.generateCode(Code.EVA_CODE, this.evaluacionRepository.count() + 1, Code.EVA_LENGTH));
        EvaluacionDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        EvaluacionDTO.setCreateAt(LocalDateTime.now());
        System.out.println(EvaluacionDTO.toString());

        //validamos
        Optional<EvaluacionEntity> optionalEvaluacionEntity = this.evaluacionRepository.findByUniqueIdentifier(EvaluacionDTO.getId());
        if (optionalEvaluacionEntity.isPresent()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Evaluacion_EXISTS");
            apiResponse.setMessage("No se registró, la Evaluacion existe");
            return apiResponse;
        }
        //change dto to entity
        EvaluacionEntity EvaluacionEntity = new EvaluacionEntity();
        EvaluacionEntity.setEvaluacionDTO(EvaluacionDTO);

        //set Alumno
        Optional<AlumnoEntity> optionalAlumnoEntity = this.alumnoRepository.findByUniqueIdentifier(EvaluacionDTO.getAlumnoDTO().getId());
        if (optionalAlumnoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ALUMNO_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el alumno asociado a la evaluacion no existe");
            return apiResponse;
        }

        //set Periodo
        Optional<PeriodoEntity> optionalPeriodoEntity = this.periodoRepository.findByUniqueIdentifier(EvaluacionDTO.getPeriodoDTO().getId());
        if (optionalAlumnoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("PERIODO_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el periodo asociado a la evaluacion no existe");
            return apiResponse;
        }

        //set docentexCurso
        Optional<DocentexCursoEntity> optionalDocentexCursoEntity = this.docentexCursoRepository.findByUniqueIdentifier(EvaluacionDTO.getDocentexCursoDTO().getId());
        if (optionalDocentexCursoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("docentexCurso_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el docentexCurso asociado a la evaluacion no existe");
            return apiResponse;
        }

        EvaluacionEntity.setAlumnoEntity(optionalAlumnoEntity.get());
        EvaluacionEntity.setPeriodoEntity(optionalPeriodoEntity.get());
        EvaluacionEntity.setDocentexCursoEntity(optionalDocentexCursoEntity.get());
        apiResponse.setData(this.evaluacionRepository.save(EvaluacionEntity).getEvaluacionDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar Evaluacion
    public ApiResponse<EvaluacionDTO> update(EvaluacionDTO EvaluacionDTO) {
        ApiResponse<EvaluacionDTO> apiResponse = new ApiResponse<>();
        System.out.println(EvaluacionDTO.toString());

        Optional<EvaluacionEntity> optionalEvaluacionEntity = this.evaluacionRepository.findByUniqueIdentifier(EvaluacionDTO.getId());
        if (optionalEvaluacionEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("evaluacion_NOT_EXISTS");
            apiResponse.setMessage("No se encontro la evaluacion");
            return apiResponse;
        }

        //change dto to entity
        EvaluacionEntity EvaluacionEntity = optionalEvaluacionEntity.get();
        EvaluacionEntity.setNote(EvaluacionDTO.getNote());
        EvaluacionEntity.setDate(EvaluacionDTO.getDate());

        //set Alumno
        Optional<AlumnoEntity> optionalAlumnoEntity = this.alumnoRepository.findByUniqueIdentifier(EvaluacionDTO.getAlumnoDTO().getId());
        if (optionalAlumnoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ALUMNO_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el alumno asociado a la evaluacion no existe");
            return apiResponse;
        }

        //set Periodo
        Optional<PeriodoEntity> optionalPeriodoEntity = this.periodoRepository.findByUniqueIdentifier(EvaluacionDTO.getPeriodoDTO().getId());
        if (optionalAlumnoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("PERIODO_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el periodo asociado a la evaluacion no existe");
            return apiResponse;
        }

        //set docentexCurso
        Optional<DocentexCursoEntity> optionalDocentexCursoEntity = this.docentexCursoRepository.findByUniqueIdentifier(EvaluacionDTO.getDocentexCursoDTO().getId());
        if (optionalDocentexCursoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("docentexCurso_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el docentexCurso asociado a la evaluacion no existe");
            return apiResponse;
        }

        EvaluacionEntity.setAlumnoEntity(optionalAlumnoEntity.get());
        EvaluacionEntity.setPeriodoEntity(optionalPeriodoEntity.get());
        EvaluacionEntity.setDocentexCursoEntity(optionalDocentexCursoEntity.get());
        apiResponse.setData(this.evaluacionRepository.save(EvaluacionEntity).getEvaluacionDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");

        return apiResponse;
    }

    //Borrar Evaluacion
    public ApiResponse<EvaluacionDTO> delete(String id) {
        ApiResponse<EvaluacionDTO> apiResponse = new ApiResponse<>();
        Optional<EvaluacionEntity> optionalEvaluacionEntity = this.evaluacionRepository.findByUniqueIdentifier(id);
        if (optionalEvaluacionEntity.isPresent()) {
            EvaluacionEntity EvaluacionEntity = optionalEvaluacionEntity.get();
            EvaluacionEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            EvaluacionEntity.setDeleteAt(LocalDateTime.now());

            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.evaluacionRepository.save(EvaluacionEntity).getEvaluacionDTO());
        } else {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ROL_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe la evaluacion para poder eliminar");
        }

        return apiResponse;
    }
}
