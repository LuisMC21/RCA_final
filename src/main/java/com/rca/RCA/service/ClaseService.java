package com.rca.RCA.service;


import com.rca.RCA.entity.*;
import com.rca.RCA.repository.*;
import com.rca.RCA.type.*;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private AulaRepository aulaRepository;
    @Autowired
    private DocentexCursoRepository docentexCursoRepository;
    @Autowired
    private PeriodoRepository periodoRepository;

    @Autowired
    private AsistenciaRepository asistenciaRepository;
    @Autowired
    private AsistenciaService asistenciaService;

    public ClaseService(ClaseRepository claseRepository, AulaRepository aulaRepository,
                        DocentexCursoRepository docentexCursoRepository, PeriodoRepository periodoRepository,
                        AsistenciaRepository asistenciaRepository){

        this.claseRepository = claseRepository;
        this.docentexCursoRepository = docentexCursoRepository;
        this.periodoRepository = periodoRepository;
        this.asistenciaRepository = asistenciaRepository;

    }

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

    public ApiResponse<Pagination<ClaseDTO>> getList(String filter, int page, int size, String periodo, String aula, String curso) {
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<ClaseDTO>> apiResponse = new ApiResponse<>();
        Pagination<ClaseDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.claseRepository.findCountEntities(filter,ConstantsGeneric.CREATED_STATUS, periodo, aula, curso));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<ClaseEntity> ClaseEntities = this.claseRepository.findEntities(filter,ConstantsGeneric.CREATED_STATUS, periodo, aula, curso, pageable).orElse(new ArrayList<>());
            pagination.setList(ClaseEntities.stream().map(ClaseEntity::getClaseDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    public ApiResponse<ClaseDTO> one(String id) throws ResourceNotFoundException {
        ClaseEntity claseEntity=this.claseRepository.findByUniqueIdentifier(id).orElseThrow(()-> new ResourceNotFoundException("Clase no encontrada"));
        ApiResponse<ClaseDTO> apiResponse = new ApiResponse<>();
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(claseEntity.getClaseDTO());
        return apiResponse;
    }

    //Agregar Clase
    @Transactional(rollbackFor = {ResourceNotFoundException.class, AttributeException.class, Exception.class})
    public ApiResponse<ClaseDTO> add(ClaseDTO claseDTO) throws ResourceNotFoundException {
        ApiResponse<ClaseDTO> apiResponse = new ApiResponse<>();

        claseDTO.setId(UUID.randomUUID().toString());
        claseDTO.setCode(Code.generateCode(Code.CLASS_CODE, this.claseRepository.count() + 1, Code.CLASS_LENGTH));
        claseDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        claseDTO.setCreateAt(LocalDateTime.now());

        //change dto to entity
        ClaseEntity claseEntity = new ClaseEntity();
        claseEntity.setClaseDTO(claseDTO);

        //set Periodo
        PeriodoEntity PeriodoEntity = this.periodoRepository.findByUniqueIdentifier(claseDTO.getPeriodoDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Periodo no encontrado"));

        //Set docentexcurso
        DocentexCursoEntity docentexCursoEntity = this.docentexCursoRepository.findByUniqueIdentifier(claseDTO.getDocentexCursoDTO().getId()).orElseThrow(()-> new ResourceNotFoundException("Docentexcurso no encontrado"));

        claseEntity.setPeriodoEntity(PeriodoEntity);
        claseEntity.setDocentexCursoEntity(docentexCursoEntity);

        ClaseDTO claseDTO1 = this.claseRepository.save(claseEntity).getClaseDTO();
        //Generar asistencias de la clase
        List<AlumnoDTO> alumnoDTOS = this.claseRepository.findAlumnosxClase(ConstantsGeneric.CREATED_STATUS, docentexCursoEntity.getAnio_lectivoEntity().getUniqueIdentifier(), docentexCursoEntity.getAulaEntity().getUniqueIdentifier(), docentexCursoEntity.getCursoEntity().getUniqueIdentifier()).orElse(new ArrayList<>()).stream().map(AlumnoEntity::getAlumnoDTO).collect(Collectors.toList());

        for(AlumnoDTO alumnoDTO2 : alumnoDTOS ){
            AsistenciaDTO asistenciaDTO = new AsistenciaDTO();
            asistenciaDTO.setClaseDTO(claseDTO1);
            asistenciaDTO.setAlumnoDTO(alumnoDTO2);
            asistenciaDTO.setState("PRESENTE");
            this.asistenciaService.add(asistenciaDTO);
        }
        apiResponse.setData(claseDTO1);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar Clase
    public ApiResponse<ClaseDTO> update(ClaseDTO ClaseDTO) throws ResourceNotFoundException {

        ApiResponse<ClaseDTO> apiResponse = new ApiResponse<>();
        System.out.println(ClaseDTO.toString());

        ClaseEntity claseEntity = this.claseRepository.findByUniqueIdentifier(ClaseDTO.getId()).orElseThrow(()-> new ResourceNotFoundException("Clase no encontrada"));

        //change dto to entity
        ClaseEntity ClaseEntity = claseEntity;
        ClaseEntity.setName(ClaseDTO.getName());
        ClaseEntity.setDate(ClaseDTO.getDate());
        ClaseEntity.setUpdateAt(LocalDateTime.now());

        //set Periodo
        PeriodoEntity PeriodoEntity = this.periodoRepository.findByUniqueIdentifier(ClaseDTO.getPeriodoDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Periodo no encontrado"));

        //Set docentexcurso
        DocentexCursoEntity docentexCursoEntity = this.docentexCursoRepository.findByUniqueIdentifier(ClaseDTO.getDocentexCursoDTO().getId()).orElseThrow(()-> new ResourceNotFoundException("Docentexcurso no encontrado"));


        ClaseEntity.setPeriodoEntity(PeriodoEntity);
        ClaseEntity.setDocentexCursoEntity(docentexCursoEntity);
        apiResponse.setData(this.claseRepository.save(ClaseEntity).getClaseDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");

        return  apiResponse;
    }

    //Borrar Clase
    public ApiResponse<ClaseDTO> delete(String id) {
        ApiResponse<ClaseDTO> apiResponse = new ApiResponse<>();
        Optional<ClaseEntity> optionalClaseEntity = this.claseRepository.findByUniqueIdentifier(id);
        Long asistencias = this.asistenciaRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, id);
        if (optionalClaseEntity.isPresent()) {

            if (asistencias > 0){
                this.claseRepository.eliminarAsistenias(id, LocalDateTime.now());
            }

            ClaseEntity ClaseEntity = optionalClaseEntity.get();
            ClaseEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            ClaseEntity.setDeleteAt(LocalDateTime.now());

            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.claseRepository.save(ClaseEntity).getClaseDTO());
        } else {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("CLASE_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe el clase para poder eliminar");
        }

        return apiResponse;
    }
}
