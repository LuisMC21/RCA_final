package com.rca.RCA.service;

import com.rca.RCA.entity.AnioLectivoEntity;
import com.rca.RCA.entity.PeriodoEntity;
import com.rca.RCA.repository.AnioLectivoRepository;
import com.rca.RCA.repository.PeriodoRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.PeriodoDTO;
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
public class PeriodoService {

    @Autowired
    private PeriodoRepository periodoRepository;

    @Autowired
    private AnioLectivoRepository anioLectivoRepository;

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

    //Función para agregar periodo-START
    public ApiResponse<PeriodoDTO> add(PeriodoDTO periodoDTO){
        ApiResponse<PeriodoDTO> apiResponse = new ApiResponse<>();
        Optional<AnioLectivoEntity> optionalAnioLectivoEntity= this.anioLectivoRepository.findByUniqueIdentifier(periodoDTO.getAnio_lectivoDTO().getId());
        Optional<PeriodoEntity> optionalPeriodoEntity = this.periodoRepository.findByName(periodoDTO.getAnio_lectivoDTO().getId(),periodoDTO.getName(), ConstantsGeneric.CREATED_STATUS);
        if (optionalPeriodoEntity.isPresent() || optionalAnioLectivoEntity.isEmpty()) {
            log.warn("No se agregó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("PERIOD_EXISTS");
            apiResponse.setMessage("No se registró, el periodo existe");
            return apiResponse;
        }
        periodoDTO.setId(UUID.randomUUID().toString());
        periodoDTO.setCode(Code.generateCode(Code.PERIOD_CODE, this.periodoRepository.count() + 1, Code.PERIOD_LENGTH));
        periodoDTO.setAnio_lectivoDTO(optionalAnioLectivoEntity.get().getAnioLectivoDTO());
        periodoDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        periodoDTO.setCreateAt(LocalDateTime.now());

        //change DTO to entity
        PeriodoEntity periodoEntity =new PeriodoEntity();
        periodoEntity.setPeriodoDTO(periodoDTO);
        periodoEntity.setAnio_lectivoEntity(optionalAnioLectivoEntity.get());
        apiResponse.setData(this.periodoRepository.save(periodoEntity).getPeriodoDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para agregar periodo-END

    //Función para actualizar periodo-START
    public ApiResponse<PeriodoDTO> update(PeriodoDTO periodoDTO){
        ApiResponse<PeriodoDTO> apiResponse = new ApiResponse<>();
        Optional<PeriodoEntity> optionalPeriodoEntity=this.periodoRepository.findByName(periodoDTO.getAnio_lectivoDTO().getId(),periodoDTO.getName(), ConstantsGeneric.CREATED_STATUS);
        //Verifica que el nombre no exista
        if(optionalPeriodoEntity.isEmpty()) {
            optionalPeriodoEntity = this.periodoRepository.findByUniqueIdentifier(periodoDTO.getId());
            //Verifica que el id y el status sean válidos
            if (optionalPeriodoEntity.isPresent()&& optionalPeriodoEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
                periodoDTO.setUpdateAt(LocalDateTime.now());
                PeriodoEntity periodoEntity = optionalPeriodoEntity.get();
                //Set update data
                if (periodoDTO.getCode() != null) {
                    periodoEntity.setCode(periodoDTO.getCode());
                }
                if (periodoDTO.getName() != null) {
                    periodoEntity.setName(periodoDTO.getName());
                }
                if (periodoDTO.getDate_start() != null) {
                    periodoEntity.setDate_start(periodoDTO.getDate_start());
                }
                if (periodoDTO.getDate_end() != null) {
                    periodoEntity.setDate_end(periodoDTO.getDate_end());
                }
                periodoEntity.setUpdateAt(periodoDTO.getUpdateAt());
                //Update in database
                apiResponse.setSuccessful(true);
                apiResponse.setMessage("ok");
                apiResponse.setData(this.periodoRepository.save(periodoEntity).getPeriodoDTO());
                return apiResponse;
            } else{
                apiResponse.setMessage("No existe el periodo para poder actualizar");
                apiResponse.setCode("PERIOD_DOES_NOT_EXISTS");
            }
        } else{
            apiResponse.setMessage("No se puedo actualizar, periodo existente");
            apiResponse.setCode("PERIOD_EXISTS");
        }
        log.warn("No se actualizó el registro");
        apiResponse.setSuccessful(false);
        return apiResponse;
    }
    //Función para actualizar periodo-END

    //Función para cambiar estado a eliminado- START
    //id dto=uniqueIdentifier Entity
    public ApiResponse<PeriodoDTO> delete(String id){
        ApiResponse<PeriodoDTO> apiResponse = new ApiResponse<>();
        //Verifica que el id y el status sean válidos
        Optional<PeriodoEntity> optionalPeriodoEntity=this.periodoRepository.findByUniqueIdentifier(id);
        if(optionalPeriodoEntity.isPresent()){
            PeriodoEntity periodoEntity =optionalPeriodoEntity.get();
            periodoEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            periodoEntity.setDeleteAt(LocalDateTime.now());

            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.periodoRepository.save(periodoEntity).getPeriodoDTO());
        } else{
            log.warn("No se eliminó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("PERIOD_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe el periodo para poder eliminar");
        }
        return apiResponse;
    }
    //Función para cambiar estado a eliminado- END
}
