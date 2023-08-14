package com.rca.RCA.service;

import com.rca.RCA.entity.AnioLectivoEntity;
import com.rca.RCA.entity.ClaseEntity;
import com.rca.RCA.entity.EvaluacionEntity;
import com.rca.RCA.entity.PeriodoEntity;
import com.rca.RCA.repository.AnioLectivoRepository;
import com.rca.RCA.repository.ClaseRepository;
import com.rca.RCA.repository.EvaluacionRepository;
import com.rca.RCA.repository.PeriodoRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.PeriodoDTO;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
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
public class PeriodoService {

    @Autowired
    private PeriodoRepository periodoRepository;
    @Autowired
    private AnioLectivoRepository anioLectivoRepository;
    @Autowired
    private ClaseRepository claseRepository;
    @Autowired
    private ClaseService claseService;
    @Autowired
    private EvaluacionRepository evaluacionRepository;
    @Autowired
    private EvaluacionService evaluacionService;

    //Función para listar periodos con paginación-START
    public ApiResponse<Pagination<PeriodoDTO>> getList(String filter, int page, int size){
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<PeriodoDTO>> apiResponse = new ApiResponse<>();
        Pagination<PeriodoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.periodoRepository.findCountPeriodo(ConstantsGeneric.CREATED_STATUS, filter));
        if(pagination.getCountFilter()>0){
            Pageable pageable= PageRequest.of(page, size);
            List<PeriodoEntity> periodoEntities=this.periodoRepository.findPeriodo(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(periodoEntities.stream().map(PeriodoEntity::getPeriodoDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para listar periodos con paginación-END

    //Función para obtener un periodo con id-START
    public ApiResponse<PeriodoDTO> one(String id) throws ResourceNotFoundException {
        PeriodoEntity periodoEntity=this.periodoRepository.findByUniqueIdentifier(id, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Periodo no existe"));
        ApiResponse<PeriodoDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(periodoEntity.getPeriodoDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para obtener un periodo con id-END

    //Función para agregar periodo-START
    public ApiResponse<PeriodoDTO> add(PeriodoDTO periodoDTO) throws ResourceNotFoundException, AttributeException {
        //Excepciones
        if(periodoRepository.existsByName("", periodoDTO.getAnio_lectivoDTO().getId(), periodoDTO.getName(), ConstantsGeneric.CREATED_STATUS))
            throw new AttributeException("El periodo ya existe");
        ApiResponse<PeriodoDTO> apiResponse = new ApiResponse<>();
        AnioLectivoEntity anioLectivoEntity= this.anioLectivoRepository.findByUniqueIdentifier(periodoDTO.getAnio_lectivoDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(() -> new ResourceNotFoundException("Año lectivo no existe"));

        PeriodoEntity periodoEntity = new PeriodoEntity();
        periodoDTO.setId(UUID.randomUUID().toString());
        periodoDTO.setCode(Code.generateCode(Code.PERIOD_CODE, this.periodoRepository.count() + 1, Code.PERIOD_LENGTH));
        periodoDTO.setAnio_lectivoDTO(anioLectivoEntity.getAnioLectivoDTO());
        periodoDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        periodoDTO.setCreateAt(LocalDateTime.now());
        //change DTO to entity
        periodoEntity.setPeriodoDTO(periodoDTO);
        periodoEntity.setAnio_lectivoEntity(anioLectivoEntity);
        apiResponse.setData(this.periodoRepository.save(periodoEntity).getPeriodoDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para agregar periodo-END

    //Función para actualizar periodo-START
    public ApiResponse<PeriodoDTO> update(PeriodoDTO periodoDTO) throws ResourceNotFoundException, AttributeException {
        //Excepciones
        if(periodoDTO.getId().isBlank())
            throw new ResourceNotFoundException("Periodo no existe");
        if(periodoRepository.existsByName(periodoDTO.getId(), periodoDTO.getAnio_lectivoDTO().getId(), periodoDTO.getName(), ConstantsGeneric.CREATED_STATUS))
            throw new AttributeException("Periodo ya existe");

        ApiResponse<PeriodoDTO> apiResponse = new ApiResponse<>();
        PeriodoEntity periodoEntity = this.periodoRepository.findByUniqueIdentifier(periodoDTO.getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Periodo no existe"));

        AnioLectivoEntity anioLectivoEntity= this.anioLectivoRepository.findByUniqueIdentifier(periodoDTO.getAnio_lectivoDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(() -> new ResourceNotFoundException("Año lectivo no existe"));

        periodoDTO.setUpdateAt(LocalDateTime.now());
        //Set update data
        periodoEntity.setName(periodoDTO.getName());
        periodoEntity.setDate_start(periodoDTO.getDate_start());
        periodoEntity.setDate_end(periodoDTO.getDate_end());
        periodoEntity.setUpdateAt(periodoDTO.getUpdateAt());
        periodoEntity.setAnio_lectivoEntity(anioLectivoEntity);
        //Update in database
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.periodoRepository.save(periodoEntity).getPeriodoDTO());
        return apiResponse;
    }
    //Función para actualizar periodo-END

    //Función para cambiar estado a eliminado- START
    //id dto=uniqueIdentifier Entity
    @Transactional
    public ApiResponse<PeriodoDTO> delete(String id) throws ResourceNotFoundException {
        PeriodoEntity periodoEntity=this.periodoRepository.findByUniqueIdentifier(id, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Periodo no existe"));
        ApiResponse<PeriodoDTO> apiResponse = new ApiResponse<>();
        //Verifica que el id y el status sean válidos
        periodoEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
        periodoEntity.setDeleteAt(LocalDateTime.now());
        //eliminar lista de clases
        Optional<List<ClaseEntity>> optionalClaseEntities= this.claseRepository.findById_Periodo(periodoEntity.getUniqueIdentifier(), ConstantsGeneric.CREATED_STATUS);
        for(int i=0; i<optionalClaseEntities.get().size(); i++){
            optionalClaseEntities.get().get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
            optionalClaseEntities.get().get(i).setDeleteAt(periodoEntity.getDeleteAt());
            this.claseService.delete(optionalClaseEntities.get().get(i).getCode());
        }
        //eliminar lista de evaluaciones
        Optional<List<EvaluacionEntity>> optionalEvaluacionEntities= this.evaluacionRepository.findById_Periodo(periodoEntity.getUniqueIdentifier(), ConstantsGeneric.CREATED_STATUS);
        for(int i=0; i<optionalEvaluacionEntities.get().size(); i++){
            optionalEvaluacionEntities.get().get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
            optionalEvaluacionEntities.get().get(i).setDeleteAt(periodoEntity.getDeleteAt());
            this.claseService.delete(optionalEvaluacionEntities.get().get(i).getCode());
        }
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.periodoRepository.save(periodoEntity).getPeriodoDTO());
        return apiResponse;
    }
    //Función para cambiar estado a eliminado- END
}
