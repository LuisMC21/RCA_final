package com.rca.RCA.service;

import com.rca.RCA.entity.ImagenEntity;
import com.rca.RCA.repository.ImagenRepository;
import com.rca.RCA.type.*;
import com.rca.RCA.type.ImagenDTO;
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
public class ImagenService {

    @Autowired
    private ImagenRepository imagenRepository;

    public Pagination<ImagenDTO> getList(String filter, int page, int size) {

        Pagination<ImagenDTO> pagination = new Pagination();
        pagination.setCountFilter(this.imagenRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<ImagenEntity> ImagenEntities = this.imagenRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(ImagenEntities.stream().map(ImagenEntity::getImagenDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        return pagination;
    }

    //Agregar imagen
    public ApiResponse<ImagenDTO> add(ImagenDTO ImagenDTO) {
        ApiResponse<ImagenDTO> apiResponse = new ApiResponse<>();
        System.out.println(ImagenDTO.toString());
        ImagenDTO.setId(UUID.randomUUID().toString());
        ImagenDTO.setCode(Code.generateCode(Code.IMAGEN_CODE, this.imagenRepository.count() + 1, Code.IMAGEN_LENGTH));
        ImagenDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        ImagenDTO.setCreateAt(LocalDateTime.now());
        //validamos
        Optional<ImagenEntity> optionalImagenEntity = this.imagenRepository.findByName(ImagenDTO.getName());
        if (optionalImagenEntity.isPresent()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Imagen_EXISTS");
            apiResponse.setMessage("No se registro, la imagen existe");
            return apiResponse;
        }
        //change dto to entity
        ImagenEntity ImagenEntity = new ImagenEntity();
        ImagenEntity.setImagenDTO(ImagenDTO);

        apiResponse.setData(this.imagenRepository.save(ImagenEntity).getImagenDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar imagen
    public void update(ImagenDTO ImagenDTO) {
        Optional<ImagenEntity> optionalImagenEntity = this.imagenRepository.findByUniqueIdentifier(ImagenDTO.getId());
        if (optionalImagenEntity.isPresent()) {
            ImagenDTO.setUpdateAt(LocalDateTime.now());
            //validamos que no se repita
            Optional<ImagenEntity> optionalImagenEntityValidation = this.imagenRepository.findByName(ImagenDTO.getName(), ImagenDTO.getId());
            if (optionalImagenEntityValidation.isPresent()) {
                System.out.println("No se actulizo, la imagen existe");
                return;
            }
            ImagenEntity ImagenEntity = optionalImagenEntity.get();
            //set update data
            if (ImagenDTO.getCode() != null) {
                ImagenEntity.setCode(ImagenDTO.getCode());
            }
            if (ImagenDTO.getName() != null) {
                ImagenEntity.setName(ImagenDTO.getName());
            }
            ImagenEntity.setUpdateAt(ImagenDTO.getUpdateAt());
            //update in database
            this.imagenRepository.save(ImagenEntity);
        } else {
            System.out.println("No existe la imagen para poder actualizar");
        }
    }

    //Borrar Imagen
    public void delete(String id) {
        Optional<ImagenEntity> optionalImagenEntity = this.imagenRepository.findByUniqueIdentifier(id);
        if (optionalImagenEntity.isPresent()) {
            ImagenEntity ImagenEntity = optionalImagenEntity.get();
            ImagenEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            ImagenEntity.setDeleteAt(LocalDateTime.now());
            this.imagenRepository.save(ImagenEntity);
        } else {
            System.out.println("No existe el Imagen para poder eliminar");
        }
    }
}
