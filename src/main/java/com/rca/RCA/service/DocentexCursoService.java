package com.rca.RCA.service;


import com.rca.RCA.entity.*;
import com.rca.RCA.repository.*;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.DocentexCursoDTO;
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
public class DocentexCursoService {
    @Autowired
    private DocentexCursoRepository docentexCursoRepository;
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private DocenteRepository docenteRepository;
    @Autowired
    private ClaseRepository claseRepository;
    @Autowired
    private ClaseService claseService;
    @Autowired
    private EvaluacionRepository evaluacionRepository;
    @Autowired
    private EvaluacionService evaluacionService;

    @Autowired
    private AnioLectivoRepository anioLectivoRepository;

    @Autowired
    private AulaRepository aulaRepository;
    //Función para listar los cursos asignados a los docente-START
    public ApiResponse<Pagination<DocentexCursoDTO>> getList(String filter, int page, int size){
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<DocentexCursoDTO>> apiResponse = new ApiResponse<>();
        Pagination<DocentexCursoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.docentexCursoRepository.findCountDocentexCurso(ConstantsGeneric.CREATED_STATUS, filter));
        if(pagination.getCountFilter()>0){
            Pageable pageable= PageRequest.of(page, size);
            List<DocentexCursoEntity> docentexCursoEntities=this.docentexCursoRepository.findDocentexCurso(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            log.info(docentexCursoEntities.size());
            pagination.setList(docentexCursoEntities.stream().map(DocentexCursoEntity::getDocentexCursoDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para listar los cursos asignados a los docente-END

    public ApiResponse<Pagination<DocentexCursoDTO>> getList(String filter,  String alumno, String anio, int page, int size){
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<DocentexCursoDTO>> apiResponse = new ApiResponse<>();
        Pagination<DocentexCursoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.docentexCursoRepository.findCountDocentexCurso(ConstantsGeneric.CREATED_STATUS, alumno, anio, filter));
        if(pagination.getCountFilter()>0){
            Pageable pageable= PageRequest.of(page, size);
            List<DocentexCursoEntity> docentexCursoEntities=this.docentexCursoRepository.findDocentexCurso(ConstantsGeneric.CREATED_STATUS, alumno, anio, filter, pageable).orElse(new ArrayList<>());
            log.info(docentexCursoEntities.size());
            pagination.setList(docentexCursoEntities.stream().map(DocentexCursoEntity::getDocentexCursoDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    public ApiResponse<DocentexCursoDTO> getListByAulaCurso(String anio, String aula, String curso) throws ResourceNotFoundException {
        ApiResponse<DocentexCursoDTO> apiResponse = new ApiResponse<>();
        DocentexCursoDTO docentexCursoDTO =this.docentexCursoRepository.findByAulaCurso(ConstantsGeneric.CREATED_STATUS, anio, aula, curso).orElseThrow(()-> new ResourceNotFoundException("Asignatura no encontrada")).getDocentexCursoDTO();

        apiResponse.setData(docentexCursoDTO);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    public ApiResponse<Pagination<DocentexCursoDTO>> getListByDocenteAnio(String filter,String docente, String anio, int page, int size){
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<DocentexCursoDTO>> apiResponse = new ApiResponse<>();
        Pagination<DocentexCursoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.docentexCursoRepository.countFindByDocenteAnio(ConstantsGeneric.CREATED_STATUS, docente, anio));
        if(pagination.getCountFilter()>0){
            Pageable pageable= PageRequest.of(page, size);
            List<DocentexCursoEntity> docentexCursoEntities=this.docentexCursoRepository.findByDocenteAnio(ConstantsGeneric.CREATED_STATUS, docente, anio, pageable).orElse(new ArrayList<>());
            log.info(docentexCursoEntities.size());
            pagination.setList(docentexCursoEntities.stream().map(DocentexCursoEntity::getDocentexCursoDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    public ApiResponse<Pagination<DocentexCursoDTO>> getListByDocenteAulaAnio(String filter,String docente, String aula, String anio, int page, int size){
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<DocentexCursoDTO>> apiResponse = new ApiResponse<>();
        Pagination<DocentexCursoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.docentexCursoRepository.countByDocenteAulaAnio(ConstantsGeneric.CREATED_STATUS, docente, aula, anio));
        if(pagination.getCountFilter()>0){
            Pageable pageable= PageRequest.of(page, size);
            List<DocentexCursoEntity> docentexCursoEntities=this.docentexCursoRepository.findByDocenteAulaAnio(ConstantsGeneric.CREATED_STATUS, docente, aula, anio, pageable).orElse(new ArrayList<>());
            log.info(docentexCursoEntities.size());
            pagination.setList(docentexCursoEntities.stream().map(DocentexCursoEntity::getDocentexCursoDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Función para obtener un curso asignado al docente-START
    public ApiResponse<DocentexCursoDTO> one(String id) throws ResourceNotFoundException {
        DocentexCursoEntity docentexCursoEntity=this.docentexCursoRepository.findByUniqueIdentifier(id).orElseThrow(()-> new ResourceNotFoundException("Asignatura no existe"));
        ApiResponse<DocentexCursoDTO> apiResponse = new ApiResponse<>();
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(docentexCursoEntity.getDocentexCursoDTO());
        return apiResponse;
    }
        //Función para obtener un curso asignado al docente-END

    //Función para agregar un curso asignado a un docente- START
    public ApiResponse<DocentexCursoDTO> add(DocentexCursoDTO docentexCursoDTO) throws ResourceNotFoundException, AttributeException {
        //Excepciones
        if(docentexCursoRepository.existsByDocenteCursoAula("", docentexCursoDTO.getCursoDTO().getId(), docentexCursoDTO.getAulaDTO().getId(), docentexCursoDTO.getAnioLectivoDTO().getId(), ConstantsGeneric.CREATED_STATUS ))
            throw new AttributeException("Asignatura ya existe");
        log.info("Docente Curso Grado {} {} {}", docentexCursoDTO.getDocenteDTO().getId(), docentexCursoDTO.getCursoDTO().getId(), docentexCursoDTO.getAulaDTO().getId());
        ApiResponse<DocentexCursoDTO> apiResponse = new ApiResponse<>();
        DocentexCursoEntity docentexCursoEntity = new DocentexCursoEntity();
        DocenteEntity docenteEntity=this.docenteRepository.findByUniqueIdentifier(docentexCursoDTO.getDocenteDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Docente no existe"));
        CursoEntity cursoEntity=this.cursoRepository.findByUniqueIdentifier(docentexCursoDTO.getCursoDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Curso no existe"));
        AulaEntity aulaEntity=this.aulaRepository.findByUniqueIdentifier(docentexCursoDTO.getAulaDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Aula no existe"));
        AnioLectivoEntity anioLectivoEntity=this.anioLectivoRepository.findByUniqueIdentifier(docentexCursoDTO.getAnioLectivoDTO().getId(),ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Año no existe"));
        //Update in database
        docentexCursoEntity.setCode(Code.generateCode(Code.CXD_CODE, this.docentexCursoRepository.count() + 1,Code.CXD_LENGTH));
        docentexCursoEntity.setDocenteEntity(docenteEntity);
        docentexCursoEntity.setCursoEntity(cursoEntity);
        docentexCursoEntity.setAulaEntity(aulaEntity);
        docentexCursoEntity.setAnio_lectivoEntity(anioLectivoEntity);
        docentexCursoEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        docentexCursoEntity.setStatus(ConstantsGeneric.CREATED_STATUS);
        docentexCursoEntity.setCreateAt(LocalDateTime.now());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.docentexCursoRepository.save(docentexCursoEntity).getDocentexCursoDTO());
        return apiResponse;
    }
    //Función para agregar un curso asignado a un docente- END

      //Función para actualizar un curso asignado a un docente-START
    public ApiResponse<DocentexCursoDTO> update(DocentexCursoDTO docentexCursoDTO) throws ResourceNotFoundException, AttributeException {
        if(docentexCursoDTO.getId().isBlank())
            throw new ResourceNotFoundException("Asignatura no existe");
        if(docentexCursoRepository.existsByDocenteCursoAula(docentexCursoDTO.getId(), docentexCursoDTO.getCursoDTO().getId(), docentexCursoDTO.getAulaDTO().getId(),docentexCursoDTO.getAnioLectivoDTO().getId() , ConstantsGeneric.CREATED_STATUS ))
            throw new AttributeException("Asignatura ya existe");


        DocentexCursoEntity docentexCursoEntity = this.docentexCursoRepository.findByUniqueIdentifier(docentexCursoDTO.getId()).orElseThrow(()-> new ResourceNotFoundException("DocentexCurso no existe"));

        ApiResponse<DocentexCursoDTO> apiResponse = new ApiResponse<>();
        //Verifica que el id y el status sean válidos
        docentexCursoEntity.setUpdateAt(docentexCursoDTO.getUpdateAt());
        DocenteEntity docenteEntity = this.docenteRepository.findByUniqueIdentifier(docentexCursoDTO.getDocenteDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()->new ResourceNotFoundException("Docente no existe"));
        CursoEntity cursoEntity = this.cursoRepository.findByUniqueIdentifier(docentexCursoDTO.getCursoDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Curso no existe"));
        AulaEntity aulaEntity = this.aulaRepository.findByUniqueIdentifier(docentexCursoDTO.getAulaDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Aula no existe"));
        AnioLectivoEntity anioLectivoEntity=this.anioLectivoRepository.findByUniqueIdentifier(docentexCursoDTO.getAnioLectivoDTO().getId(),ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Año no existe"));
        //Set update data
        docentexCursoEntity.setDocenteEntity(docenteEntity);
        docentexCursoEntity.setCursoEntity(cursoEntity);
        docentexCursoEntity.setAulaEntity(aulaEntity);
        docentexCursoEntity.setAnio_lectivoEntity(anioLectivoEntity);
        docentexCursoEntity.setUpdateAt(LocalDateTime.now());
        //Update in database
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.docentexCursoRepository.save(docentexCursoEntity).getDocentexCursoDTO());
        return apiResponse;

    }
    //Función para actualizar un curso asignado a un docente-END

    //Función para cambiar estado a eliminado- START
    //id dto=uniqueIdentifier Entity
    public ApiResponse<DocentexCursoDTO> delete(String id) throws ResourceNotFoundException {
        DocentexCursoEntity docentexCursoEntity=this.docentexCursoRepository.findByUniqueIdentifier(id).orElseThrow(()-> new ResourceNotFoundException("Asignatura no existe"));

        ApiResponse<DocentexCursoDTO> apiResponse = new ApiResponse<>();
        docentexCursoEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
        docentexCursoEntity.setDeleteAt(LocalDateTime.now());
        //eliminar lista de clases
        Optional<List<ClaseEntity>> optionalClaseEntities= this.claseRepository.findById_DxC(docentexCursoEntity.getUniqueIdentifier(), ConstantsGeneric.CREATED_STATUS);
        for (int i = 0; i < optionalClaseEntities.get().size(); i++) {
            optionalClaseEntities.get().get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
            optionalClaseEntities.get().get(i).setDeleteAt(docentexCursoEntity.getDeleteAt());
            this.claseService.delete(optionalClaseEntities.get().get(i).getCode());
        }
        //eliminar lista de evaluaciones
        Optional<List<EvaluacionEntity>> optionalEvaluacionEntities= this.evaluacionRepository.findById_DXC(docentexCursoEntity.getUniqueIdentifier(), ConstantsGeneric.CREATED_STATUS);
        for (int i = 0; i < optionalEvaluacionEntities.get().size(); i++) {
            optionalEvaluacionEntities.get().get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
            optionalEvaluacionEntities.get().get(i).setDeleteAt(docentexCursoEntity.getDeleteAt());
            this.claseService.delete(optionalEvaluacionEntities.get().get(i).getCode());
        }
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.docentexCursoRepository.save(docentexCursoEntity).getDocentexCursoDTO());

        return apiResponse;
    }
    //Función para cambiar estado a eliminado- END
}
