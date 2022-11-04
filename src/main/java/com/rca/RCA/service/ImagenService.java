package com.rca.RCA.service;

import com.rca.RCA.entity.*;
import com.rca.RCA.entity.ImagenEntity;
import com.rca.RCA.repository.ImagenRepository;
import com.rca.RCA.repository.UsuarioRepository;
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

    @Autowired
    private UsuarioRepository usuarioRepository;

    public ImagenService(ImagenRepository imagenRepository, UsuarioRepository usuarioRepository){
        this.imagenRepository = imagenRepository;
        this.usuarioRepository = usuarioRepository;
    }

    //Obtener imágenes
    public ApiResponse<Pagination<ImagenDTO>> getList(String filter, int page, int size) {
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<ImagenDTO>> apiResponse = new ApiResponse<>();
        Pagination<ImagenDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.imagenRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<ImagenEntity> imagenEntities = this.imagenRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(imagenEntities.stream().map(ImagenEntity::getImagenDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Agregar imagen
    public ApiResponse<ImagenDTO> add(ImagenDTO ImagenDTO) {
        ApiResponse<ImagenDTO> apiResponse = new ApiResponse<>();
        System.out.println(ImagenDTO.toString());
        ImagenDTO.setId(UUID.randomUUID().toString());
        ImagenDTO.setCode(Code.generateCode(Code.IMAGEN_CODE, this.imagenRepository.count() + 1, Code.IMAGEN_LENGTH));
        ImagenDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        ImagenDTO.setCreateAt(LocalDateTime.now());
        System.out.println(ImagenDTO.toString());
        //validamos
        Optional<ImagenEntity> optionalImagenEntity = this.imagenRepository.findByName(ImagenDTO.getName());
        if (optionalImagenEntity.isPresent()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Imagen_EXISTS");
            apiResponse.setMessage("No se registró, la imagen existe");
            return apiResponse;
        }
        //change dto to entity
        ImagenEntity ImagenEntity = new ImagenEntity();
        ImagenEntity.setImagenDTO(ImagenDTO);

        //set usaurio
        Optional<UsuarioEntity> optionalUsuarioEntity = this.usuarioRepository.findByUniqueIdentifier(ImagenDTO.getUsuarioDTO().getId());
        if (optionalUsuarioEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ROL_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el usaurio asociado a la imagen no existe");
            return apiResponse;
        }

        ImagenEntity.setUsuarioEntity(optionalUsuarioEntity.get());
        apiResponse.setData(this.imagenRepository.save(ImagenEntity).getImagenDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar imagen
    public ApiResponse<ImagenDTO> update(ImagenDTO ImagenDTO) {
        ApiResponse<ImagenDTO> apiResponse = new ApiResponse<>();
        System.out.println(ImagenDTO.toString());

        Optional<ImagenEntity> optionalImagenEntity = this.imagenRepository.findByUniqueIdentifier(ImagenDTO.getId());
        if (optionalImagenEntity.isPresent()) {
            ImagenDTO.setUpdateAt(LocalDateTime.now());

            //validamos
            Optional<ImagenEntity> optionalImagenEntityValidation = this.imagenRepository.findByName(ImagenDTO.getName(), ImagenDTO.getId());
            if (optionalImagenEntityValidation.isPresent()) {
                apiResponse.setSuccessful(false);
                apiResponse.setCode("IMAGEN_EXISTS");
                apiResponse.setMessage("No se actualizó, la imagen existe");
                return apiResponse;
            }

            //change dto to entity
            ImagenEntity ImagenEntity = optionalImagenEntity.get();
            ImagenEntity.setName(ImagenDTO.getName());
            ImagenEntity.setRoute(ImagenDTO.getRoute());
            ImagenEntity.setUpdateAt(ImagenDTO.getUpdateAt());

            //set rol
            Optional<UsuarioEntity> optionalUsuarioEntity = this.usuarioRepository.findByUniqueIdentifier(ImagenDTO.getUsuarioDTO().getId());
            if (optionalUsuarioEntity.isEmpty()) {
                apiResponse.setSuccessful(false);
                apiResponse.setCode("USUARIO_NOT_EXISTS");
                apiResponse.setMessage("No se registro, el usuario asociada a la imagen no existe");
                return apiResponse;
            }
            ImagenEntity.setUsuarioEntity(optionalUsuarioEntity.get());
            apiResponse.setData(this.imagenRepository.save(ImagenEntity).getImagenDTO());
            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
        }else {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("imagen_NOT_EXISTS");
            apiResponse.setMessage("No se encontró la imagen");
            return apiResponse;
        }

        return apiResponse;
    }

    //Borrar Imagen
    public ApiResponse<ImagenDTO> delete(String id) {
        ApiResponse<ImagenDTO> apiResponse = new ApiResponse<>();
        Optional<ImagenEntity> optionalImagenEntity = this.imagenRepository.findByUniqueIdentifier(id);
        if (optionalImagenEntity.isPresent()) {
            ImagenEntity ImagenEntity = optionalImagenEntity.get();
            ImagenEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            ImagenEntity.setDeleteAt(LocalDateTime.now());

            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.imagenRepository.save(ImagenEntity).getImagenDTO());
        } else {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("IMAGEN_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe la imagen para poder eliminar");;
        }

        return apiResponse;
    }
}
