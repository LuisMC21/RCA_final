package com.rca.RCA.service;

import com.rca.RCA.entity.SeccionEntity;
import com.rca.RCA.repository.SeccionxGradoRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.SeccionDTO;
import com.rca.RCA.util.ConstantsGeneric;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class SeccionxGradoService {
    @Autowired
    private SeccionxGradoRepository seccionxGradoRepository;

    //Función para listar con paginación de secciones por el grado indicado-START
    public ApiResponse<Pagination<SeccionDTO>> getListSxG(String id, String filter, int page, int size){
        log.info("id filter page size {} {} {}", id, filter, page, size);
        ApiResponse<Pagination<SeccionDTO>> apiResponse = new ApiResponse<>();
        Pagination<SeccionDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.seccionxGradoRepository.findCountSeccionxGrado(id, ConstantsGeneric.CREATED_STATUS, filter));
        System.out.println(pagination.getCountFilter());
        if(pagination.getCountFilter()>0){
            Pageable pageable= PageRequest.of(page, size);
            List<SeccionEntity> seccionEntities=this.seccionxGradoRepository.findSeccionxGrado(id, ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(seccionEntities.stream().map(SeccionEntity::getSeccionDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para listar con paginación de secciones por el grado indicado-END
}
