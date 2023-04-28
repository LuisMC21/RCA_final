package com.rca.RCA.service;

import com.rca.RCA.entity.NoticiaEntity;
import com.rca.RCA.entity.UsuarioEntity;
import com.rca.RCA.repository.NoticiaRepository;
import com.rca.RCA.repository.UsuarioRepository;
import com.rca.RCA.type.*;
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
    public ApiResponse<NoticiaDTO> add(NoticiaDTO NoticiaDTO) {
        ApiResponse<NoticiaDTO> apiResponse = new ApiResponse<>();
        System.out.println(NoticiaDTO.toString());
        NoticiaDTO.setId(UUID.randomUUID().toString());
        NoticiaDTO.setCode(Code.generateCode(Code.NEWS_CODE, this.noticiaRepository.count() + 1, Code.NEWS_LENGTH));
        NoticiaDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        NoticiaDTO.setCreateAt(LocalDateTime.now());
        System.out.println(NoticiaDTO.toString());
        //validamos
        Optional<NoticiaEntity> optionalNoticiaEntity = this.noticiaRepository.findByTitle(NoticiaDTO.getTitle());
        if (optionalNoticiaEntity.isPresent()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Noticia_EXISTS");
            apiResponse.setMessage("No se registro, la noticia existe");
            return apiResponse;
        }
        //change dto to entity
        NoticiaEntity NoticiaEntity = new NoticiaEntity();
        NoticiaEntity.setNoticiaDTO(NoticiaDTO);

        //set usaurio
        Optional<UsuarioEntity> optionalUsuarioEntity = this.usuarioRepository.findByUniqueIdentifier(NoticiaDTO.getUsuarioDTO().getId(), ConstantsGeneric.CREATED_STATUS);
        if (optionalUsuarioEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ROL_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el usaurio asociado a la imagen no existe");
            return apiResponse;
        }

        NoticiaEntity.setUsuarioEntity(optionalUsuarioEntity.get());
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
        NoticiaEntity.setImage(noticiaDTO.getImage());
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
    public ApiResponse<NoticiaDTO> delete(String id) {
        ApiResponse<NoticiaDTO> apiResponse = new ApiResponse<>();
        Optional<NoticiaEntity> optionalNoticiaEntity = this.noticiaRepository.findByUniqueIdentifier(id);
        if (optionalNoticiaEntity.isPresent()) {
            NoticiaEntity NoticiaEntity = optionalNoticiaEntity.get();
            NoticiaEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            NoticiaEntity.setDeleteAt(LocalDateTime.now());

            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.noticiaRepository.save(NoticiaEntity).getNoticiaDTO());
        } else {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("NOTICIA_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe la noticia para poder eliminar");;
        }

        return apiResponse;
    }
}
