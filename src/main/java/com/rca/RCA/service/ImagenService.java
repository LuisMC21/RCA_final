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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
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
    public ApiResponse<ImagenDTO> add(ImagenFileDTO ImagenFileDTO){
        ApiResponse<ImagenDTO> apiResponse = new ApiResponse<>();
        System.out.println(ImagenFileDTO.toString());
        ImagenDTO ImagenDTO = new ImagenDTO();
        ImagenDTO.setId(UUID.randomUUID().toString());
        String code = Code.generateCode(Code.IMAGEN_CODE, this.imagenRepository.count() + 1, Code.IMAGEN_LENGTH);
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

        //set usaurio
        Optional<UsuarioEntity> optionalUsuarioEntity = this.usuarioRepository.findByUniqueIdentifier(ImagenDTO.getUsuarioDTO().getId());
        if (optionalUsuarioEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ROL_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el usaurio asociado a la imagen no existe");
            return apiResponse;
        }

        //set name
        ImagenDTO.setName(ImagenFileDTO.getName());

        //Decodificar la imagen base64
        byte[] imageBytes = Base64.getDecoder().decode(ImagenFileDTO.getImagenBase64());
        Path path = Paths.get("C:/temp/image.jpg");
        try {
            Files.write(path, imageBytes);
            System.out.println("Imagen guardada en: " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*String nombreArchivo = file.getOriginalFilename();
        File archivo = new File(Code.RUTA_IMAGENES + "/" + ImagenEntity.getUniqueIdentifier());
        file.transferTo(archivo);
        ImagenEntity.setRoute(Code.RUTA_IMAGENES + "/" + nombreArchivo);*/

        //set route
        ImagenDTO.setRoute("C:/Temp/image.jpg");


        //change dto to entity
        ImagenEntity ImagenEntity = new ImagenEntity();
        ImagenEntity.setImagenDTO(ImagenDTO);
        ImagenEntity.setCode(code);


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
