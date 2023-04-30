package com.rca.RCA.service;

import com.rca.RCA.entity.NoticiaEntity;
import com.rca.RCA.entity.UsuarioEntity;
import com.rca.RCA.repository.NoticiaRepository;
import com.rca.RCA.repository.UsuarioRepository;
import com.rca.RCA.type.*;
import com.rca.RCA.type.NoticiaDTO;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
public class NoticiaService {

    @Autowired
    private NoticiaRepository noticiaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public NoticiaService(NoticiaRepository noticiaRepository, UsuarioRepository usuarioRepository){
        this.noticiaRepository = noticiaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    //Obtener noticias
    public ApiResponse<Pagination<NoticiaDTO>> getList(String filter, int page, int size) {
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<NoticiaDTO>> apiResponse = new ApiResponse<>();
        Pagination<NoticiaDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.noticiaRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<NoticiaEntity> noticiaEntities = this.noticiaRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(noticiaEntities.stream().map(NoticiaEntity::getNoticiaDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Agreagar noticia
    public ApiResponse<NoticiaDTO> add(@Valid NoticiaFileDTO NoticiaFileDTO) throws AttributeException, ResourceNotFoundException {

        if(noticiaRepository.existsByTitle("", ConstantsGeneric.CREATED_STATUS, NoticiaFileDTO.getTitle()))
            throw new AttributeException("Noticia ya existe");

        ApiResponse<NoticiaDTO> apiResponse = new ApiResponse<>();
        System.out.println(NoticiaFileDTO.toString());
        NoticiaDTO NoticiaDTO = new NoticiaDTO();
        NoticiaDTO.setId(UUID.randomUUID().toString());
        NoticiaDTO.setCode(Code.generateCode(Code.NEWS_CODE, this.noticiaRepository.count() + 1, Code.NEWS_LENGTH));
        NoticiaDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        NoticiaDTO.setCreateAt(LocalDateTime.now());
        String code = Code.generateCode(Code.NEWS_CODE, this.noticiaRepository.count() + 1, Code.IMAGEN_LENGTH);


        //fileDTO a DTO
        NoticiaDTO.setSommelier(NoticiaFileDTO.getSommelier());
        NoticiaDTO.setTitle(NoticiaFileDTO.getTitle());
        NoticiaDTO.setDate(NoticiaFileDTO.getDate());
        NoticiaDTO.setDescrip(NoticiaFileDTO.getDescrip());
        NoticiaDTO.setUsuarioDTO(NoticiaFileDTO.getUsuarioDTO());

        //set usaurio
        UsuarioEntity usuarioEntity = this.usuarioRepository.findByUniqueIdentifier(NoticiaDTO.getId()).orElseThrow(()-> new ResourceNotFoundException("Usuario no exixte"));

        //Decodificar la imagen base64
        String base64 = NoticiaFileDTO.getImagenBase64();
        byte[] imageBytes = Base64.getMimeDecoder().decode(base64);

        // Obtener la ruta de la carpeta donde se guardarán las imágenes
        String rutaCarpeta = "src/main/resources/images";
        Path rutaCompleta = Paths.get(rutaCarpeta);
        System.out.println("--------------------------");
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
        String nombreArchivo = code + NoticiaDTO.getTitle()+".jpg"; // el nombre del archivo
        Path rutaArchivo = rutaCompleta.resolve(nombreArchivo);

        // Guardar la imagen en el archivo
        try {
            Files.write(rutaArchivo, imageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String route = rutaCarpeta + "/" + nombreArchivo;
        NoticiaDTO.setRoute(route);

        //change dto to entity
        NoticiaEntity NoticiaEntity = new NoticiaEntity();
        NoticiaEntity.setNoticiaDTO(NoticiaDTO);



        NoticiaEntity.setUsuarioEntity(usuarioEntity);
        apiResponse.setData(this.noticiaRepository.save(NoticiaEntity).getNoticiaDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar Noticia
    public ApiResponse<NoticiaDTO> update(NoticiaDTO noticiaDTO) {
        ApiResponse<NoticiaDTO> apiResponse = new ApiResponse<>();
        System.out.println(noticiaDTO.toString());

        Optional<NoticiaEntity> optionalNoticiaEntity = this.noticiaRepository.findByUniqueIdentifier(noticiaDTO.getId());
        if (optionalNoticiaEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Usuario_NOT_EXISTS");
            apiResponse.setMessage("No se encontro el Usuario");
            return apiResponse;
        }

        //validamos
        Optional<NoticiaEntity> optionalImagenEntityValidation = this.noticiaRepository.findByTitle(noticiaDTO.getTitle(), noticiaDTO.getId());
        if (optionalImagenEntityValidation.isPresent()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("NOTICIA_EXISTS");
            apiResponse.setMessage("No se actualizó, la noticia existe");
            return apiResponse;
        }

        //change dto to entity
        NoticiaEntity NoticiaEntity = optionalNoticiaEntity.get();
        NoticiaEntity.setTitle(noticiaDTO.getTitle());
        NoticiaEntity.setSommelier(noticiaDTO.getSommelier());
        NoticiaEntity.setDescrip(noticiaDTO.getDescrip());
        NoticiaEntity.setDate(noticiaDTO.getDate());
        NoticiaEntity.setRoute(noticiaDTO.getRoute());
        NoticiaEntity.setUpdateAt(LocalDateTime.now());

        //set usuario
        Optional<UsuarioEntity> optionalUsuarioEntity = this.usuarioRepository.findByUniqueIdentifier(noticiaDTO.getUsuarioDTO().getId(), ConstantsGeneric.CREATED_STATUS);
        if (optionalUsuarioEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("USUARIO_NOT_EXISTS");
            apiResponse.setMessage("No se registro, el usuario asociada a la imagen no existe");
            return apiResponse;
        }

        NoticiaEntity.setUsuarioEntity(optionalUsuarioEntity.get());
        apiResponse.setData(this.noticiaRepository.save(NoticiaEntity).getNoticiaDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");

        return apiResponse;
    }

    //Borrar Noticia
    public ApiResponse<NoticiaDTO> delete(String id)  throws  ResourceNotFoundException{

        NoticiaEntity noticiaEntity = this.noticiaRepository.findByUniqueIdentifier(id).orElseThrow(()-> new ResourceNotFoundException("Noticia no existe"));
        ApiResponse<NoticiaDTO> apiResponse = new ApiResponse<>();
        noticiaEntity.setDeleteAt(LocalDateTime.now());
        noticiaEntity.setStatus(ConstantsGeneric.DELETED_STATUS);

        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.noticiaRepository.save(noticiaEntity).getNoticiaDTO());

        //borrar imagen
        String rutaArchivo = noticiaEntity.getRoute();
        File archivo = new File(rutaArchivo);
        if (archivo.delete()) {
            System.out.println("Archivo eliminado correctamente.");
        } else {
            System.out.println("No se pudo eliminar el archivo.");
        }

        return apiResponse;
    }
}
