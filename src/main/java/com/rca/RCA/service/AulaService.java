package com.rca.RCA.service;

import com.rca.RCA.entity.GradoEntity;
import com.rca.RCA.entity.SeccionEntity;
import com.rca.RCA.entity.AulaEntity;
import com.rca.RCA.repository.GradoRepository;
import com.rca.RCA.repository.SeccionRepository;
import com.rca.RCA.repository.AulaRepository;
import com.rca.RCA.type.*;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class AulaService {
    @Autowired
    private AulaRepository aulaRepository;
    @Autowired
    private SeccionRepository seccionRepository;
    @Autowired
    private GradoRepository gradoRepository;

    //Función para listar con paginación de secciones por el grado indicado-START
    public ApiResponse<Pagination<SeccionDTO>> getList(String id, String filter, int page, int size){
        log.info("id filter page size {} {} {}", id, filter, page, size);
        ApiResponse<Pagination<SeccionDTO>> apiResponse = new ApiResponse<>();
        Pagination<SeccionDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.aulaRepository.findCountAula(id, ConstantsGeneric.CREATED_STATUS, filter));
        System.out.println(pagination.getCountFilter());
        if(pagination.getCountFilter()>0){
            Pageable pageable= PageRequest.of(page, size);
            List<SeccionEntity> seccionEntities=this.aulaRepository.findAula(id, ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            log.info(seccionEntities.size());
            pagination.setList(seccionEntities.stream().map(SeccionEntity::getSeccionDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para listar con paginación de secciones por el grado indicado-END
    //Función para agregar una sección a un grado- START
    public ApiResponse<AulaDTO> add(Map ids){
        log.info("idGrado idSeccion {} {}", ids.get("idGrado"), ids.get("idSeccion"));
        ApiResponse<AulaDTO> apiResponse = new ApiResponse<>();
        AulaDTO aulaDTO = new AulaDTO();
        AulaEntity aulaEntity = new AulaEntity();

        Optional<GradoEntity> optionalGradoEntity=this.gradoRepository.findByUniqueIdentifier(ids.get("idGrado").toString());
        Optional<SeccionEntity> optionalSeccionEntity=this.seccionRepository.findByUniqueIdentifier(ids.get("idSeccion").toString());

        if(optionalGradoEntity.isPresent() && optionalSeccionEntity.isPresent()){
                //Update in database
            aulaEntity.setCode(Code.generateCode(Code.CLASSROOM_CODE, this.aulaRepository.count() + 1,Code.CLASSROOM_LENGTH));
            aulaEntity.setGradoEntity(optionalGradoEntity.get());
            aulaEntity.setSeccionEntity(optionalSeccionEntity.get());
            aulaEntity.setUniqueIdentifier(UUID.randomUUID().toString());
            aulaDTO.setSeccionDTO(aulaEntity.getSeccionEntity().getSeccionDTO());
            aulaDTO.setGradoDTO(aulaEntity.getGradoEntity().getGradoDTO());
            aulaDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
            aulaDTO.setCreateAt(LocalDateTime.now());
            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.aulaRepository.save(aulaEntity).getSeccionxGradoDTO());
            return apiResponse;
        }else{
            log.warn("No se completó el registro");
            apiResponse.setSuccessful(false);
            if(!optionalGradoEntity.isPresent()) {
                apiResponse.setCode("GRADE_DOES_NOT_EXISTS");
            }
            if(!optionalSeccionEntity.isPresent()) {
                apiResponse.setCode("SECTION_DOES_NOT_EXISTS");
            }
            apiResponse.setMessage("No se puedo registrar la sección en el grado");
        }
        return apiResponse;
    }
    //Función para agregar una sección a un grado- END
}
