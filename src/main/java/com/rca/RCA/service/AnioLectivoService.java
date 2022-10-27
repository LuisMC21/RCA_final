package com.rca.RCA.service;

import com.rca.RCA.entity.AnioLectivoEntity;
import com.rca.RCA.entity.DocentexCursoEntity;
import com.rca.RCA.entity.PeriodoEntity;
import com.rca.RCA.repository.AnioLectivoRepository;
import com.rca.RCA.repository.PeriodoRepository;
import com.rca.RCA.type.AnioLectivoDTO;
import com.rca.RCA.type.ApiResponse;
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
public class AnioLectivoService {

    @Autowired
    private AnioLectivoRepository anioLectivoRepository;
    @Autowired
    private PeriodoRepository periodoRepository;
    @Autowired
    private PeriodoService periodoService;

    //Función para listar con paginación de seccion-START
    public ApiResponse<Pagination<AnioLectivoDTO>> getList(String filter, int page, int size){
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<AnioLectivoDTO>> apiResponse = new ApiResponse<>();
        Pagination<AnioLectivoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.anioLectivoRepository.findCountSeccion(ConstantsGeneric.CREATED_STATUS, filter));
        if(pagination.getCountFilter()>0){
            Pageable pageable= PageRequest.of(page, size);
            List<AnioLectivoEntity> seccionEntities=this.anioLectivoRepository.findAnioLectivo(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(seccionEntities.stream().map(AnioLectivoEntity::getAnioLectivoDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para listar con paginación de seccion-END

    public ApiResponse<AnioLectivoDTO> add(AnioLectivoDTO anioLectivoDTO){
        ApiResponse<AnioLectivoDTO> apiResponse = new ApiResponse<>();
        anioLectivoDTO.setId(UUID.randomUUID().toString());
        anioLectivoDTO.setCode(Code.generateCode(Code.SCHOOL_YEAR_CODE, this.anioLectivoRepository.count() + 1, Code.SCHOOL_YEAR_LENGTH));
        anioLectivoDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        anioLectivoDTO.setCreateAt(LocalDateTime.now());

        Optional<AnioLectivoEntity> optionalSeccionEntity = this.anioLectivoRepository.findByName(anioLectivoDTO.getName());
        if (optionalSeccionEntity.isPresent()) {
            log.warn("No se agregó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("SCHOOL_YEAR_EXISTS");
            apiResponse.setMessage("No se resgistró, el año lectivo existe");
            return apiResponse;
        }

        //change DTO to entity
        AnioLectivoEntity anioLectivoEntity =new AnioLectivoEntity();
        anioLectivoEntity.setAnioLectivoDTO(anioLectivoDTO);
        apiResponse.setData(this.anioLectivoRepository.save(anioLectivoEntity).getAnioLectivoDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    public ApiResponse<AnioLectivoDTO> update(AnioLectivoDTO anioLectivoDTO){
        ApiResponse<AnioLectivoDTO> apiResponse = new ApiResponse<>();
        Optional<AnioLectivoEntity> optionalAnioLectivoEntity=this.anioLectivoRepository.findByName(anioLectivoDTO.getName());
        //Verifica que el nombre no exista
        if(optionalAnioLectivoEntity.isEmpty()) {
            optionalAnioLectivoEntity = this.anioLectivoRepository.findByUniqueIdentifier(anioLectivoDTO.getId());
            //Verifica que el id y el status sean válidos
            if (optionalAnioLectivoEntity.isPresent()&& optionalAnioLectivoEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
                anioLectivoDTO.setUpdateAt(LocalDateTime.now());
                AnioLectivoEntity anioLectivoEntity = optionalAnioLectivoEntity.get();
                //Set update data
                if (anioLectivoDTO.getCode() != null) {
                    anioLectivoEntity.setCode(anioLectivoDTO.getCode());
                }
                if (anioLectivoDTO.getName() != null) {
                    anioLectivoEntity.setName(anioLectivoDTO.getName());
                }
                anioLectivoEntity.setUpdateAt(anioLectivoDTO.getUpdateAt());
                //Update in database
                apiResponse.setSuccessful(true);
                apiResponse.setMessage("ok");
                apiResponse.setData(this.anioLectivoRepository.save(anioLectivoEntity).getAnioLectivoDTO());
                return apiResponse;
            } else{
                apiResponse.setMessage("No existe el año lectivo para poder actualizar");
                apiResponse.setCode("SCHOOL_YEAR_DOES_NOT_EXISTS");
            }
        } else{
            apiResponse.setMessage("No se pudo actualizar, año lectivo existente");
            apiResponse.setCode("SCHOOL_YEAR_EXISTS");
        }
        log.warn("No se actualizó el registro");
        apiResponse.setSuccessful(false);
        return apiResponse;
    }

    //Función para cambiar estado a eliminado- START
    //id dto=uniqueIdentifier Entity
    public ApiResponse<AnioLectivoDTO> delete(String id){
        ApiResponse<AnioLectivoDTO> apiResponse = new ApiResponse<>();
        //Verifica que el id y el status sean válidos
        Optional<AnioLectivoEntity> optionalSeccionEntity=this.anioLectivoRepository.findByUniqueIdentifier(id);
        if(optionalSeccionEntity.isPresent()){
            AnioLectivoEntity anioLectivoEntity =optionalSeccionEntity.get();
            anioLectivoEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            anioLectivoEntity.setDeleteAt(LocalDateTime.now());
            Optional<List<PeriodoEntity>> optionalAnioLectivoEntities= this.periodoRepository.findById_AnioLectivo(anioLectivoEntity.getId(), ConstantsGeneric.CREATED_STATUS);
            for(int i=0; i<optionalAnioLectivoEntities.get().size(); i++){
                optionalAnioLectivoEntities.get().get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
                optionalAnioLectivoEntities.get().get(i).setDeleteAt(anioLectivoEntity.getDeleteAt());
                this.periodoService.delete(optionalAnioLectivoEntities.get().get(i).getCode());
            }
            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.anioLectivoRepository.save(anioLectivoEntity).getAnioLectivoDTO());
        } else{
            log.warn("No se eliminó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("SCHOOL_YEAR_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe el año lectivo para poder eliminar");
        }
        return apiResponse;
    }
    //Función para cambiar estado a eliminado- END
}
