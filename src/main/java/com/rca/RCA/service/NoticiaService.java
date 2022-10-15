package com.rca.RCA.service;

import com.rca.RCA.entity.NoticiaEntity;
import com.rca.RCA.repository.NoticiaRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.NoticiaDTO;
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
public class NoticiaService {

    @Autowired
    private NoticiaRepository noticiaRepository;

    public Pagination<NoticiaDTO> getList(String filter, int page, int size) {

        Pagination<NoticiaDTO> pagination = new Pagination();
        pagination.setCountFilter(this.noticiaRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<NoticiaEntity> NoticiaEntities = this.noticiaRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(NoticiaEntities.stream().map(NoticiaEntity::getNoticiaDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        return pagination;
    }

    //Agregar Noticia
    public ApiResponse<NoticiaDTO> add(NoticiaDTO NoticiaDTO) {
        ApiResponse<NoticiaDTO> apiResponse = new ApiResponse<>();
        System.out.println(NoticiaDTO.toString());
        NoticiaDTO.setId(UUID.randomUUID().toString());
        NoticiaDTO.setCode(Code.generateCode(Code.NEWS_CODE, this.noticiaRepository.count() + 1, Code.NEWS_LENGTH));
        NoticiaDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        NoticiaDTO.setCreateAt(LocalDateTime.now());
        //validamos
        Optional<NoticiaEntity> optionalNoticiaEntity = this.noticiaRepository.findByTitle(NoticiaDTO.getTitle());
        if (optionalNoticiaEntity.isPresent()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Noticia_EXISTS");
            apiResponse.setMessage("No se registro, el Noticia existe");
            return apiResponse;
        }
        //change dto to entity
        NoticiaEntity NoticiaEntity = new NoticiaEntity();
        NoticiaEntity.setNoticiaDTO(NoticiaDTO);

        apiResponse.setData(this.noticiaRepository.save(NoticiaEntity).getNoticiaDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar Noticia
    public void update(NoticiaDTO NoticiaDTO) {
        Optional<NoticiaEntity> optionalNoticiaEntity = this.noticiaRepository.findByUniqueIdentifier(NoticiaDTO.getId());
        if (optionalNoticiaEntity.isPresent()) {
            NoticiaDTO.setUpdateAt(LocalDateTime.now());
            //validamos que no se repita
            Optional<NoticiaEntity> optionalNoticiaEntityValidation = this.noticiaRepository.findByTitle(NoticiaDTO.getTitle(), NoticiaDTO.getId());
            if (optionalNoticiaEntityValidation.isPresent()) {
                System.out.println("No se actulizo, la categoria existe");
                return;
            }
            NoticiaEntity NoticiaEntity = optionalNoticiaEntity.get();
            //set update data
            if (NoticiaDTO.getCode() != null) {
                NoticiaEntity.setCode(NoticiaDTO.getCode());
            }
            if (NoticiaDTO.getTitle() != null) {
                NoticiaEntity.setTitle(NoticiaDTO.getTitle());
            }
            NoticiaEntity.setUpdateAt(NoticiaDTO.getUpdateAt());
            //update in database
            this.noticiaRepository.save(NoticiaEntity);
        } else {
            System.out.println("No existe la noticia para poder actualizar");
        }
    }

    //Borrar Noticia
    public void delete(String id) {
        Optional<NoticiaEntity> optionalNoticiaEntity = this.noticiaRepository.findByUniqueIdentifier(id);
        if (optionalNoticiaEntity.isPresent()) {
            NoticiaEntity NoticiaEntity = optionalNoticiaEntity.get();
            NoticiaEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            NoticiaEntity.setDeleteAt(LocalDateTime.now());
            this.noticiaRepository.save(NoticiaEntity);
        } else {
            System.out.println("No existe el Noticia para poder eliminar");
        }
    }
}
