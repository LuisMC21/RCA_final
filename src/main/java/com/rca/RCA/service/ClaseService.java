package com.rca.RCA.service;

import com.rca.RCA.entity.*;
import com.rca.RCA.repository.AulaRepository;
import com.rca.RCA.repository.ClaseRepository;
import com.rca.RCA.repository.DocentexCursoRepository;
import com.rca.RCA.repository.PeriodoRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.ClaseDTO;
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
public class ClaseService {

    @Autowired
    private ClaseRepository claseRepository;

    private AulaRepository aulaRepository;

    private DocentexCursoRepository docentexCursoRepository;

    private PeriodoRepository periodoRepository;

    //Listar clases
    public ApiResponse<Pagination<ClaseDTO>> getList(String filter, int page, int size) {
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<ClaseDTO>> apiResponse = new ApiResponse<>();
        Pagination<ClaseDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.claseRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<ClaseEntity> ClaseEntities = this.claseRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(ClaseEntities.stream().map(ClaseEntity::getClaseDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Agregar Clase
    public ApiResponse<ClaseDTO> add(ClaseDTO ClaseDTO) {
        ApiResponse<ClaseDTO> apiResponse = new ApiResponse<>();
        System.out.println(ClaseDTO.toString());
        ClaseDTO.setId(UUID.randomUUID().toString());
        ClaseDTO.setCode(Code.generateCode(Code.CLASS_CODE, this.claseRepository.count() + 1, Code.CLASS_LENGTH));
        ClaseDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        ClaseDTO.setCreateAt(LocalDateTime.now());

        //change dto to entity
        ClaseEntity ClaseEntity = new ClaseEntity();
        ClaseEntity.setClaseDTO(ClaseDTO);

        //set Periodo
        Optional<PeriodoEntity> optionalPeriodoEntity = this.periodoRepository.findByUniqueIdentifier(ClaseDTO.getPeriodoDTO().getId());
        if (optionalPeriodoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("PERIODO_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el periodo asociado a la clase no existe");
            return apiResponse;
        }

        //set aula
        Optional<AulaEntity> optionalAulaEntity = this.aulaRepository.findByUniqueIdentifier(ClaseDTO.getAulaDTO().getId());
        if (optionalAulaEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Aula_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el aula asociada a la clase no existe");
            return apiResponse;
        }

        //Set docentexcurso
        Optional<DocentexCursoEntity> optionalDocentexCursoEntity = this.docentexCursoRepository.findByUniqueIdentifier(ClaseDTO.getDocentexCursoDTO().getId());
        if (optionalDocentexCursoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("DocentexCurso_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el docentexCurso asociada a la clase no existe");
            return apiResponse;
        }

        ClaseEntity.setAulaEntity(optionalAulaEntity.get());
        ClaseEntity.setPeriodoEntity(optionalPeriodoEntity.get());
        ClaseEntity.setDocentexCursoEntity(optionalDocentexCursoEntity.get());
        apiResponse.setData(this.claseRepository.save(ClaseEntity).getClaseDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar Clase
    public ApiResponse<ClaseDTO> update(ClaseDTO ClaseDTO) {

        ApiResponse<ClaseDTO> apiResponse = new ApiResponse<>();
        System.out.println(ClaseDTO.toString());

        Optional<ClaseEntity> optionalClaseEntity = this.claseRepository.findByUniqueIdentifier(ClaseDTO.getId());
        if (optionalClaseEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("clase_NOT_EXISTS");
            apiResponse.setMessage("No se encontro el Usuario");
            return apiResponse;
        }

        //change dto to entity
        ClaseEntity ClaseEntity = optionalClaseEntity.get();
        ClaseEntity.setDate(ClaseDTO.getDate());
        ClaseEntity.setUpdateAt(LocalDateTime.now());

        //Set Periodo
        Optional<PeriodoEntity> optionalPeriodoEntity = this.periodoRepository.findByUniqueIdentifier(ClaseDTO.getPeriodoDTO().getId());
        if (optionalPeriodoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("PERIODO_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el periodo asociado a la clase no existe");
            return apiResponse;
        }

        //set aula
        Optional<AulaEntity> optionalAulaEntity = this.aulaRepository.findByUniqueIdentifier(ClaseDTO.getAulaDTO().getId());
        if (optionalAulaEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Aula_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el aula asociada a la clase no existe");
            return apiResponse;
        }

        //Set docentexcurso
        Optional<DocentexCursoEntity> optionalDocentexCursoEntity = this.docentexCursoRepository.findByUniqueIdentifier(ClaseDTO.getDocentexCursoDTO().getId());
        if (optionalDocentexCursoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("DocentexCurso_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el docentexCurso asociada a la clase no existe");
            return apiResponse;
        }

        ClaseEntity.setPeriodoEntity(optionalPeriodoEntity.get());
        ClaseEntity.setAulaEntity(optionalAulaEntity.get());
        ClaseEntity.setDocentexCursoEntity(optionalDocentexCursoEntity.get());
        apiResponse.setData(this.claseRepository.save(ClaseEntity).getClaseDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");

        return  apiResponse;
    }

    //Borrar Clase
    public void delete(String id) {
        Optional<ClaseEntity> optionalClaseEntity = this.claseRepository.findByUniqueIdentifier(id);
        if (optionalClaseEntity.isPresent()) {
            ClaseEntity ClaseEntity = optionalClaseEntity.get();
            ClaseEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            ClaseEntity.setDeleteAt(LocalDateTime.now());
            this.claseRepository.save(ClaseEntity);
        } else {
            System.out.println("No existe el Clase para poder eliminar");
        }
    }
}
