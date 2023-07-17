package com.rca.RCA.service;

import com.rca.RCA.entity.CursoEntity;
import com.rca.RCA.entity.DocentexCursoEntity;
import com.rca.RCA.repository.CursoRepository;
import com.rca.RCA.repository.DocentexCursoRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.CursoDTO;
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
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private DocentexCursoRepository docentexCursoRepository;

    @Autowired
    private DocentexCursoService docentexCursoService;
    //Función para listar cursos con paginación-START
    public ApiResponse<Pagination<CursoDTO>> getList(String filter, int page, int size){
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<CursoDTO>> apiResponse = new ApiResponse<>();
        Pagination<CursoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.cursoRepository.findCountCurso(ConstantsGeneric.CREATED_STATUS, filter));
        if(pagination.getCountFilter()>0){
            Pageable pageable= PageRequest.of(page, size);
            List<CursoEntity> cursoEntities=this.cursoRepository.findCurso(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(cursoEntities.stream().map(CursoEntity::getCursoDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para listar cursos con paginación-END

    //Listar con año y aula
    public ApiResponse<List<CursoDTO>> getListByAulaAnio(String aula, String anio){
        ApiResponse<List<CursoDTO>> apiResponse = new ApiResponse<>();
            List<CursoEntity> cursoEntities=this.cursoRepository.findCursoByAulaAnio(ConstantsGeneric.CREATED_STATUS, aula, anio).orElse(new ArrayList<>());
            List<CursoDTO> cursoDTOS = cursoEntities.stream().map(CursoEntity::getCursoDTO).collect(Collectors.toList());
        apiResponse.setData(cursoDTOS);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    public ApiResponse<CursoDTO> one(String id) throws ResourceNotFoundException {
        CursoEntity cursoEntity = this.cursoRepository.findByUniqueIdentifier(id, ConstantsGeneric.CREATED_STATUS).orElseThrow(() -> new ResourceNotFoundException("Curso no existe"));
        ApiResponse<CursoDTO> apiResponse = new ApiResponse<>();
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(cursoEntity.getCursoDTO());
        return apiResponse;
    }
    //Función para agregar un curso con paginación-START
    public ApiResponse<CursoDTO> add(CursoDTO cursoDTO) throws AttributeException {
        //Excepciones
        if(cursoRepository.existsByName(cursoDTO.getName(),"", ConstantsGeneric.CREATED_STATUS))
            throw new AttributeException("Curso ya existe");
        ApiResponse<CursoDTO> apiResponse = new ApiResponse<>();
        cursoDTO.setId(UUID.randomUUID().toString());
        cursoDTO.setCode(Code.generateCode(Code.COURSE_CODE, this.cursoRepository.count() + 1, Code.COURSE_LENGTH));
        cursoDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        cursoDTO.setCreateAt(LocalDateTime.now());

        //change DTO to entity
        CursoEntity cursoEntity =new CursoEntity();
        cursoEntity.setCursoDTO(cursoDTO);
        apiResponse.setData(this.cursoRepository.save(cursoEntity).getCursoDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para agregar un curso con paginación-END

    //Función para actualizar un curso-START
    public ApiResponse<CursoDTO> update(CursoDTO cursoDTO) throws ResourceNotFoundException, AttributeException {
        //Excepciones
        if(cursoDTO.getId().isBlank())
            throw new ResourceNotFoundException("Curso no existe");
        if(cursoRepository.existsByName(cursoDTO.getName(), cursoDTO.getId(), ConstantsGeneric.CREATED_STATUS))
            throw new AttributeException("Curso ya existe");

        ApiResponse<CursoDTO> apiResponse = new ApiResponse<>();
        CursoEntity cursoEntity = this.cursoRepository.findByUniqueIdentifier(cursoDTO.getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Curso no existe"));
        //Verifica que el id y el status sean válidos
        cursoDTO.setUpdateAt(LocalDateTime.now());
        //Set update data
        cursoEntity.setName(cursoDTO.getName());
        cursoEntity.setUpdateAt(cursoDTO.getUpdateAt());
        //Update in database
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.cursoRepository.save(cursoEntity).getCursoDTO());
        return apiResponse;
    }
    //Función para actualizar un curso-END

    //Función para cambiar estado a eliminado- START
    //id dto=uniqueIdentifier Entity
    public ApiResponse<CursoDTO> delete(String id) throws ResourceNotFoundException {
        CursoEntity cursoEntity=this.cursoRepository.findByUniqueIdentifier(id, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Curso no existe"));
        ApiResponse<CursoDTO> apiResponse = new ApiResponse<>();
        cursoEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
        cursoEntity.setDeleteAt(LocalDateTime.now());
        Optional<List<DocentexCursoEntity>> optionalDocentexCursoEntities= this.docentexCursoRepository.findByCurso(cursoEntity.getId(), ConstantsGeneric.CREATED_STATUS);
        for(int i=0; i<optionalDocentexCursoEntities.get().size(); i++){
            this.docentexCursoService.delete(optionalDocentexCursoEntities.get().get(i).getCode());
        }
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.cursoRepository.save(cursoEntity).getCursoDTO());
        return apiResponse;
    }
    //Función para cambiar estado a eliminado- END
}
