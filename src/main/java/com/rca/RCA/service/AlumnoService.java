package com.rca.RCA.service;

import com.rca.RCA.entity.AlumnoEntity;
import com.rca.RCA.entity.ApoderadoEntity;
import com.rca.RCA.entity.UsuarioEntity;
import com.rca.RCA.repository.AlumnoRepository;
import com.rca.RCA.repository.ApoderadoRepository;
import com.rca.RCA.repository.UsuarioRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.AlumnoDTO;
import com.rca.RCA.type.Pagination;
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
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;
    private UsuarioRepository usuarioRepository;
    private ApoderadoRepository apoderadoRepository;

    public AlumnoService(AlumnoRepository alumnoRepository, UsuarioRepository usuarioRepository, ApoderadoRepository apoderadoRepository){
        this.alumnoRepository = alumnoRepository;
        this.usuarioRepository = usuarioRepository;
        this.apoderadoRepository = apoderadoRepository;
    }

    public ApiResponse<Pagination<AlumnoDTO>> getList(String filter, int page, int size) {
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<AlumnoDTO>> apiResponse = new ApiResponse<>();
        Pagination<AlumnoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.alumnoRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<AlumnoEntity> AlumnoEntities = this.alumnoRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(AlumnoEntities.stream().map(AlumnoEntity::getAlumnoDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Agregar Alumno
    public ApiResponse<AlumnoDTO> add(AlumnoDTO AlumnoDTO) {
        ApiResponse<AlumnoDTO> apiResponse = new ApiResponse<>();
        System.out.println(AlumnoDTO.toString());
        AlumnoDTO.setId(UUID.randomUUID().toString());
        AlumnoDTO.setCode(Code.generateCode(Code.ALU_CODE, this.alumnoRepository.count() + 1, Code.ALU_LENGTH));
        AlumnoDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        AlumnoDTO.setCreateAt(LocalDateTime.now());
        System.out.println(AlumnoDTO.toString());

        //change dto to entity
        AlumnoEntity AlumnoEntity = new AlumnoEntity();
        AlumnoEntity.setAlumnoDTO(AlumnoDTO);

        //set usaurio
        Optional<UsuarioEntity> optionalUsuarioEntity = this.usuarioRepository.findByUniqueIdentifier(AlumnoDTO.getUsuarioDTO().getId());
        if (optionalUsuarioEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("USUARIO_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el usaurio asociado al Alumno no existe");
            return apiResponse;
        }

        //set Apoderado
        Optional<ApoderadoEntity> optionalApoderadoEntity = this.apoderadoRepository.findByUniqueIdentifier(AlumnoDTO.getApoderadoDTO().getId());
        if (optionalUsuarioEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("APODERADO_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el apoderado asociado al Alumno no existe");
            return apiResponse;
        }

        AlumnoEntity.setUsuarioEntity(optionalUsuarioEntity.get());
        AlumnoEntity.setApoderadoEntity(optionalApoderadoEntity.get());
        apiResponse.setData(this.alumnoRepository.save(AlumnoEntity).getAlumnoDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar Alumno
    public ApiResponse<AlumnoDTO> update(AlumnoDTO AlumnoDTO) {
        ApiResponse<AlumnoDTO> apiResponse = new ApiResponse<>();
        System.out.println(AlumnoDTO.toString());

        Optional<AlumnoEntity> optionalAlumnoEntity = this.alumnoRepository.findByUniqueIdentifier(AlumnoDTO.getId());
        if (optionalAlumnoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Alumno_NOT_EXISTS");
            apiResponse.setMessage("No se encontro la Alumno");
            return apiResponse;
        }

        //change dto to entity
        AlumnoEntity AlumnoEntity = optionalAlumnoEntity.get();
        AlumnoEntity.setDiseases(AlumnoDTO.getDiseases());
        AlumnoEntity.setNamecon_pri(AlumnoDTO.getNamecon_pri());
        AlumnoEntity.setTelcon_pri(AlumnoDTO.getTelcon_pri());
        AlumnoEntity.setNamecon_sec(AlumnoDTO.getNamecon_sec());
        AlumnoEntity.setTelcon_sec(AlumnoDTO.getTelcon_sec());
        AlumnoEntity.setVaccine(AlumnoDTO.getVaccine());
        AlumnoEntity.setType_insurance(AlumnoDTO.getType_insurance());

        //set usuario
        Optional<UsuarioEntity> optionalUsuarioEntity = this.usuarioRepository.findByUniqueIdentifier(AlumnoDTO.getUsuarioDTO().getId());
        if (optionalUsuarioEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("USUARIO_NOT_EXISTS");
            apiResponse.setMessage("No se registro, el usuario asociada a la Alumno no existe");
            return apiResponse;
        }

        //set Apoderado
        Optional<ApoderadoEntity> optionalApoderadoEntity = this.apoderadoRepository.findByUniqueIdentifier(AlumnoDTO.getApoderadoDTO().getId());
        if (optionalUsuarioEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("APODERADO_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el apoderado asociado al Alumno no existe");
            return apiResponse;
        }

        AlumnoEntity.setUsuarioEntity(optionalUsuarioEntity.get());
        AlumnoEntity.setApoderadoEntity(optionalApoderadoEntity.get());
        apiResponse.setData(this.alumnoRepository.save(AlumnoEntity).getAlumnoDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Borrar Alumno
    public void delete(String id) {
        Optional<AlumnoEntity> optionalAlumnoEntity = this.alumnoRepository.findByUniqueIdentifier(id);
        if (optionalAlumnoEntity.isPresent()) {
            AlumnoEntity AlumnoEntity = optionalAlumnoEntity.get();
            AlumnoEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            AlumnoEntity.setDeleteAt(LocalDateTime.now());
            this.alumnoRepository.save(AlumnoEntity);
        } else {
            System.out.println("No existe el Alumno para poder eliminar");
        }
    }


}
