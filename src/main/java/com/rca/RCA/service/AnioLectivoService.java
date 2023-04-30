package com.rca.RCA.service;

import com.rca.RCA.entity.AnioLectivoEntity;
import com.rca.RCA.entity.MatriculaEntity;
import com.rca.RCA.entity.PeriodoEntity;
import com.rca.RCA.repository.AnioLectivoRepository;
import com.rca.RCA.repository.MatriculaRepository;
import com.rca.RCA.repository.PeriodoRepository;
import com.rca.RCA.type.AnioLectivoDTO;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
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
    @Autowired
    private MatriculaRepository matriculaRepository;
    @Autowired
    private MatriculaService matriculaService;

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
    //Función para obtener un año lectivo con id -START
    public ApiResponse<AnioLectivoDTO> one(String id) throws ResourceNotFoundException {
        AnioLectivoEntity anioLectivoEntity =this.anioLectivoRepository.findByUniqueIdentifier(id, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Año lectivo no existe"));
        ApiResponse<AnioLectivoDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(anioLectivoEntity.getAnioLectivoDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para obtener un año lectivo con id -END

    public ApiResponse<AnioLectivoDTO> add(AnioLectivoDTO anioLectivoDTO) throws ResourceNotFoundException, AttributeException {
        //Excepciones
        if(anioLectivoRepository.existsByName(anioLectivoDTO.getName(), ConstantsGeneric.CREATED_STATUS, ""))
            throw new AttributeException("Año lectivo ya existe");
        ApiResponse<AnioLectivoDTO> apiResponse = new ApiResponse<>();
        anioLectivoDTO.setId(UUID.randomUUID().toString());
        anioLectivoDTO.setCode(Code.generateCode(Code.SCHOOL_YEAR_CODE, this.anioLectivoRepository.count() + 1, Code.SCHOOL_YEAR_LENGTH));
        anioLectivoDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        anioLectivoDTO.setCreateAt(LocalDateTime.now());

        //change DTO to entity
        AnioLectivoEntity anioLectivoEntity = new AnioLectivoEntity();
        anioLectivoEntity.setAnioLectivoDTO(anioLectivoDTO);
        apiResponse.setData(this.anioLectivoRepository.save(anioLectivoEntity).getAnioLectivoDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    public ApiResponse<AnioLectivoDTO> update(AnioLectivoDTO anioLectivoDTO) throws ResourceNotFoundException, AttributeException {
        //Excepciones
        if(anioLectivoDTO.getId().isBlank())
            throw new ResourceNotFoundException("Año lectivo no existe");
        if(anioLectivoRepository.existsByName(anioLectivoDTO.getName(), ConstantsGeneric.CREATED_STATUS, anioLectivoDTO.getId()))
            throw new AttributeException("Año lectivo ya existe");
        ApiResponse<AnioLectivoDTO> apiResponse = new ApiResponse<>();
        AnioLectivoEntity anioLectivoEntity=this.anioLectivoRepository.findByUniqueIdentifier(anioLectivoDTO.getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Año lectivo no existe"));
        anioLectivoDTO.setUpdateAt(LocalDateTime.now());
        //Set update data
        anioLectivoEntity.setCode(anioLectivoDTO.getCode());
        anioLectivoEntity.setName(anioLectivoDTO.getName());
        anioLectivoEntity.setUpdateAt(anioLectivoDTO.getUpdateAt());
        //Update in database
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.anioLectivoRepository.save(anioLectivoEntity).getAnioLectivoDTO());
        return apiResponse;
    }

    //Función para cambiar estado a eliminado- START
    //id dto=uniqueIdentifier Entity
    public ApiResponse<AnioLectivoDTO> delete(String id) throws ResourceNotFoundException {
        //Verifica que el id y el status sean válidos
        AnioLectivoEntity anioLectivoEntity =this.anioLectivoRepository.findByUniqueIdentifier(id, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Año lectivo no existe"));

        ApiResponse<AnioLectivoDTO> apiResponse = new ApiResponse<>();
        anioLectivoEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
        anioLectivoEntity.setDeleteAt(LocalDateTime.now());
        //Elimina los periodos del año lectivo
        Optional<List<PeriodoEntity>> optionalAnioLectivoEntities= this.periodoRepository.findById_AnioLectivo(anioLectivoEntity.getUniqueIdentifier(), ConstantsGeneric.CREATED_STATUS);
        for(int i=0; i<optionalAnioLectivoEntities.get().size(); i++){
            optionalAnioLectivoEntities.get().get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
            optionalAnioLectivoEntities.get().get(i).setDeleteAt(anioLectivoEntity.getDeleteAt());
            this.periodoService.delete(optionalAnioLectivoEntities.get().get(i).getCode());
        }
        //Elimina las matriculas del año lectivo
        Optional<List<MatriculaEntity>> optionalMatriculaEntities= this.matriculaRepository.findByAnioLectivo(anioLectivoEntity.getUniqueIdentifier(), ConstantsGeneric.CREATED_STATUS);
        for(int i=0; i<optionalMatriculaEntities.get().size(); i++){
            optionalMatriculaEntities.get().get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
            optionalMatriculaEntities.get().get(i).setDeleteAt(anioLectivoEntity.getDeleteAt());
            this.matriculaService.delete(optionalMatriculaEntities.get().get(i).getCode());
        }
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.anioLectivoRepository.save(anioLectivoEntity).getAnioLectivoDTO());
        return apiResponse;
    }
    //Función para cambiar estado a eliminado- END
}
