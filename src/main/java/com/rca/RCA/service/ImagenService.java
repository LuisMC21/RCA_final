package com.rca.RCA.service;

import com.rca.RCA.entity.*;
import com.rca.RCA.entity.ImagenEntity;
import com.rca.RCA.repository.ImagenRepository;
import com.rca.RCA.repository.UsuarioRepository;
import com.rca.RCA.type.*;
import com.rca.RCA.type.ImagenDTO;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ImagenService {

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ResourceLoader resourceLoader;

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

    public ApiResponse<ImagenDTO> one(String id) throws ResourceNotFoundException {
        ImagenEntity imagenEntity=this.imagenRepository.findByUniqueIdentifier(id).orElseThrow(()-> new ResourceNotFoundException("Imagen no encontrada"));
        ApiResponse<ImagenDTO> apiResponse = new ApiResponse<>();
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(imagenEntity.getImagenDTO());
        return apiResponse;
    }

    //Agregar imagen
    public ApiResponse<ImagenDTO> add(ImagenFileDTO ImagenFileDTO) throws AttributeException, ResourceNotFoundException {

        //Validamos que eno exista una imagen con el mismo nombre
        if(imagenRepository.existsByName("",ConstantsGeneric.CREATED_STATUS, ImagenFileDTO.getName()))
            throw new AttributeException("Imagen ya existe");

        ApiResponse<ImagenDTO> apiResponse = new ApiResponse<>();
        ImagenDTO ImagenDTO = new ImagenDTO();
        ImagenDTO.setId(UUID.randomUUID().toString());
        String code = Code.generateCode(Code.IMAGEN_CODE, this.imagenRepository.count() + 1, Code.IMAGEN_LENGTH);
        ImagenDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        ImagenDTO.setCreateAt(LocalDateTime.now());

        //setUsuario
        ImagenDTO.setUsuarioDTO(ImagenFileDTO.getUsuarioDTO());
        //set name
        ImagenDTO.setName(ImagenFileDTO.getName());

        //Verificar que exixte el usuario
        UsuarioEntity usuarioEntity = this.usuarioRepository.findByUniqueIdentifier(ImagenDTO.getId(), ConstantsGeneric.CREATED_STATUS
        ).orElseThrow(()-> new ResourceNotFoundException("Usuario no exixte"));

        //Decodificar la imagen base64
        String base64 = ImagenFileDTO.getImagenBase64();
        byte[] imageBytes = Base64.getMimeDecoder().decode(base64);

        // Obtener la ruta de la carpeta donde se guardarán las imágenes
        String rutaCarpeta = "src/main/resources/images";
        Path rutaCompleta = Paths.get(rutaCarpeta);
        System.out.println(rutaCompleta);

        // Verificar si la carpeta existe, y crearla en caso contrario
        if (!Files.exists(rutaCompleta)) {
            try {
                Files.createDirectories(rutaCompleta);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Construir la ruta completa del archivo de imagen
        String nombreArchivo = code + ImagenDTO.getName()+".jpg"; // el nombre del archivo
        Path rutaArchivo = rutaCompleta.resolve(nombreArchivo);

        // Guardar la imagen en el archivo
        try {
            Files.write(rutaArchivo, imageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //set route
        String route = rutaCarpeta + "/" + nombreArchivo;
        ImagenDTO.setRoute(route);


        //change dto to entity
        ImagenEntity ImagenEntity = new ImagenEntity();
        ImagenEntity.setImagenDTO(ImagenDTO);
        ImagenEntity.setCode(code);



        ImagenEntity.setUsuarioEntity(usuarioEntity);
        apiResponse.setData(this.imagenRepository.save(ImagenEntity).getImagenDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar imagen
    public ApiResponse<ImagenDTO> update(ImagenDTO ImagenDTO) throws ResourceNotFoundException, AttributeException {
        ApiResponse<ImagenDTO> apiResponse = new ApiResponse<>();
        System.out.println(ImagenDTO.toString());

        ImagenEntity imagenEntity = this.imagenRepository.findByUniqueIdentifier(ImagenDTO.getId()).orElseThrow(()->new ResourceNotFoundException("Imagen no encontrada"));

        //validamos
        if(imagenRepository.existsByName(ImagenDTO.getId(),ConstantsGeneric.CREATED_STATUS, ImagenDTO.getName()))
            throw new AttributeException("La imagen ya existe");

        //change dto to entity
        ImagenEntity ImagenEntity = imagenEntity;
        ImagenEntity.setName(ImagenDTO.getName());
        ImagenEntity.setRoute(ImagenDTO.getRoute());
        ImagenEntity.setUpdateAt(ImagenDTO.getUpdateAt());

        //Validar usuario
        UsuarioEntity usuarioEntity = this.usuarioRepository.findByUniqueIdentifier(ImagenDTO.getUsuarioDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()->new ResourceNotFoundException("Usuario no encontrado"));

        ImagenEntity.setUsuarioEntity(usuarioEntity);
        apiResponse.setData(this.imagenRepository.save(ImagenEntity).getImagenDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");


        return apiResponse;
    }

    //Borrar Imagen
    public ApiResponse<ImagenDTO> delete(String id) throws  ResourceNotFoundException{

        ImagenEntity  imagenEntity = this.imagenRepository.findByUniqueIdentifier(id).orElseThrow(()-> new ResourceNotFoundException("Imagen no exixte"));
        ApiResponse<ImagenDTO> apiResponse = new ApiResponse<>();
        imagenEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
        imagenEntity.setDeleteAt(LocalDateTime.now());

        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.imagenRepository.save(imagenEntity).getImagenDTO());

        //borrar imagen
        String rutaArchivo = imagenEntity.getRoute();
        File archivo = new File(rutaArchivo);
        if (archivo.delete()) {
            System.out.println("Archivo eliminado correctamente.");
        } else {
            System.out.println("No se pudo eliminar el archivo.");
        }

        return apiResponse;
    }
}
