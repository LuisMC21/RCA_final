package com.rca.RCA.service;


import com.rca.RCA.entity.*;
import com.rca.RCA.repository.*;
import com.rca.RCA.type.*;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.core.io.Resource;

import jakarta.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
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
    @Autowired
    private ClaseRepository claseRepository;
    @Autowired
    private ClaseService claseService;
    @Autowired
    private MatriculaRepository matriculaRepository;
    @Autowired
    private MatriculaService matriculaService;
    @Autowired
    private DocentexCursoService docentexCursoService;
    @Autowired
    private DocentexCursoRepository docentexCursoRepository;
    @Autowired
    private AnioLectivoRepository anioLectivoRepository;
    //Función para listar aulas con paginación-START
    public ApiResponse<Pagination<AulaDTO>> getList(String filter, int page, int size) {
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<AulaDTO>> apiResponse = new ApiResponse<>();
        Pagination<AulaDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.aulaRepository.findCountAula(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<AulaEntity> aulaEntities = this.aulaRepository.findAula(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            log.info(aulaEntities.size());
            pagination.setList(aulaEntities.stream().map(AulaEntity::getAulaDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Función para listar aulas-END

    //Función para listar aulas por año con paginación-START
    public ApiResponse<List<AulaDTO>> getList(String filter, String anio) {
        log.info("filter {}", filter);
        ApiResponse<List<AulaDTO>> apiResponse = new ApiResponse<>();
        List<AulaEntity> aulaEntities = this.aulaRepository.findAulaxAnio(ConstantsGeneric.CREATED_STATUS, anio, filter).orElse(new ArrayList<>());
        List<AulaDTO> aulaDTOS = aulaEntities.stream().map(AulaEntity::getAulaDTO).collect(Collectors.toList());
        apiResponse.setData(aulaDTOS);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Función para listar aulas-END
    //Función para obtener un aula con ID- START
    public ApiResponse<AulaDTO> one(String id) throws ResourceNotFoundException {
        AulaEntity aulaEntity = this.aulaRepository.findByUniqueIdentifier(id, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Aula no existe para eliminar"));
        ApiResponse<AulaDTO> apiResponse = new ApiResponse<>();
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(aulaEntity.getAulaDTO());
        return apiResponse;
    }
    //Función para obtener un aula con ID- END


    //Función para agregar un aula- START
    public ApiResponse<AulaDTO> add(AulaDTO aulaDTO) throws ResourceNotFoundException, AttributeException {
        //Excepciones
        if(aulaRepository.existsByGradoYSeccion(aulaDTO.getGradoDTO().getId(), aulaDTO.getSeccionDTO().getId(), ConstantsGeneric.CREATED_STATUS,""))
            throw new AttributeException("El aula ya existe");

        log.info("Grado Seccion {} {}", aulaDTO.getGradoDTO().getId(), aulaDTO.getSeccionDTO().getId());
        ApiResponse<AulaDTO> apiResponse = new ApiResponse<>();
        AulaEntity aulaEntity = new AulaEntity();
        GradoEntity gradoEntity = this.gradoRepository.findByUniqueIdentifier(aulaDTO.getGradoDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Grado no existe"));
        SeccionEntity seccionEntity = this.seccionRepository.findByUniqueIdentifier(aulaDTO.getSeccionDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Sección no existe"));
        //Update in database
        aulaEntity.setCode(Code.generateCode(Code.CLASSROOM_CODE, this.aulaRepository.count() + 1, Code.CLASSROOM_LENGTH));
        aulaEntity.setGradoEntity(gradoEntity);
        aulaEntity.setSeccionEntity(seccionEntity);
        aulaEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        aulaEntity.setStatus(ConstantsGeneric.CREATED_STATUS);
        aulaEntity.setCreateAt(LocalDateTime.now());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.aulaRepository.save(aulaEntity).getAulaDTO());
        return apiResponse;
    }
    //Función para agregar un aula- END

    //Función para actualizar un aula-START
    public ApiResponse<AulaDTO> update(AulaDTO aulaDTO) throws AttributeException, ResourceNotFoundException {
        //Excepciones
        if(aulaDTO.getId().isBlank())
            throw new ResourceNotFoundException("El aula no existe");
        if(aulaRepository.existsByGradoYSeccion(aulaDTO.getGradoDTO().getId(), aulaDTO.getSeccionDTO().getId(), ConstantsGeneric.CREATED_STATUS,aulaDTO.getId()))
            throw new AttributeException("El aula ya existe");
        ApiResponse<AulaDTO> apiResponse = new ApiResponse<>();
        AulaEntity aulaEntity = this.aulaRepository.findByUniqueIdentifier(aulaDTO.getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("El aula no existe"));
        GradoEntity gradoEntity = this.gradoRepository.findByUniqueIdentifier(aulaDTO.getGradoDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Grado no existe"));
        SeccionEntity seccionEntity = this.seccionRepository.findByUniqueIdentifier(aulaDTO.getSeccionDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Sección no existe"));
        //Set update data
        aulaEntity.setGradoEntity(gradoEntity);
        aulaEntity.setSeccionEntity(seccionEntity);
        aulaEntity.setUpdateAt(LocalDateTime.now());
        //Update in database
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.aulaRepository.save(aulaEntity).getAulaDTO());
         return apiResponse;
    }
    //Función para actualizar un aula-END

    //Función para cambiar estado a eliminado- START
    //id dto=uniqueIdentifier Entity
    public ApiResponse<AulaDTO> delete(String id) throws ResourceNotFoundException {
        ApiResponse<AulaDTO> apiResponse = new ApiResponse<>();
        //Verifica que el id y el status sean válidos
        AulaEntity aulaEntity = this.aulaRepository.findByUniqueIdentifier(id, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Aula no existe para eliminar"));
        aulaEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
        aulaEntity.setDeleteAt(LocalDateTime.now());

        //Eliminar lista de matriculas del aula
        List<MatriculaEntity> matriculaEntities = this.matriculaRepository.findByAula(aulaEntity.getUniqueIdentifier(), ConstantsGeneric.CREATED_STATUS).orElse(new ArrayList<>());
        for (int i = 0; i < matriculaEntities.size(); i++) {
            matriculaEntities.get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
            matriculaEntities.get(i).setDeleteAt(aulaEntity.getDeleteAt());
            this.matriculaService.delete(matriculaEntities.get(i).getUniqueIdentifier());
        }
        List<DocentexCursoEntity> docentexCursoEntities = this.docentexCursoRepository.findByAula(aulaEntity.getUniqueIdentifier(), ConstantsGeneric.CREATED_STATUS).orElse(new ArrayList<>());

        for (int i = 0; i < docentexCursoEntities.size(); i++) {
            docentexCursoEntities.get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
            docentexCursoEntities.get(i).setDeleteAt(aulaEntity.getDeleteAt());
            this.docentexCursoService.delete(docentexCursoEntities.get(i).getUniqueIdentifier());
        }
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.aulaRepository.save(aulaEntity).getAulaDTO());
        return apiResponse;
    }

    @NotNull
    public ResponseEntity<Resource> exportListApoderados(String id_aula, String id_aniolectivo) throws ResourceNotFoundException {
        log.info("id_aula {}", id_aula);
        AulaEntity aulaEntity = this.aulaRepository.findByUniqueIdentifier(id_aula, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Aula no existe"));
        AnioLectivoEntity anioLectivoEntity = this.anioLectivoRepository.findByUniqueIdentifier(id_aniolectivo, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Aula no existe"));
        try {
            final File file = ResourceUtils.getFile("classpath:reportes/lista_apoderados.jasper"); //la ruta del reporte
            final File imgLogo = ResourceUtils.getFile("classpath:images/logoC.jpg"); //Ruta de la imagen
            final JasperReport report = (JasperReport) JRLoader.loadObject(file);
            //Se consultan los datos para el reporte de apoderados DTO
            List<AlumnoEntity> alumnoEntities = this.aulaRepository.findAlumnosxAula(id_aula, id_aniolectivo, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("No contiene alumnos"));

            //Se agregan los datos para ReporteApoderadosDTO
            List<ReporteApoderadosDTO> reporteApoderadosDTOList= new ArrayList<>();
            for (int i = 0; i < alumnoEntities.size(); i++) {
                ReporteApoderadosDTO reporteApoderadosDTO = new ReporteApoderadosDTO();
                reporteApoderadosDTO.setAlumnoDTO(alumnoEntities.get(i).getAlumnoDTO());
                reporteApoderadosDTO.setApoderadoDTO(alumnoEntities.get(i).getApoderadoEntity().getApoderadoDTO());
                reporteApoderadosDTOList.add(reporteApoderadosDTO);
            }

            //Se llenan los parámetros del reporte
            final HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("logoEmpresa", new FileInputStream(imgLogo));
            parameters.put("grado", aulaEntity.getAulaDTO().getGradoDTO().getName().toString());
            parameters.put("seccion", aulaEntity.getAulaDTO().getSeccionDTO().getName().toString());
            parameters.put("año", anioLectivoEntity.getName());
            parameters.put("dsLA", new JRBeanArrayDataSource(reporteApoderadosDTOList.toArray()));

            //Se imprime el reporte
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
            byte [] reporte = JasperExportManager.exportReportToPdf(jasperPrint);
            String sdf = (new SimpleDateFormat("dd/MM/yyyy")).format(new Date());
            StringBuilder stringBuilder = new StringBuilder().append("ApoderadosPDF:");
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename(stringBuilder
                            .append(aulaEntity.getCode())
                            .append("generateDate:").append(sdf)
                            .append(".pdf").toString())
                    .build();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(contentDisposition);
            return ResponseEntity.ok().contentLength((long) reporte.length)
                    .contentType(MediaType.APPLICATION_PDF)
                    .headers(headers).body(new ByteArrayResource(reporte));
        } catch (Exception e) {
            new ResourceNotFoundException("Lista no encontrada");
        }
        return null;
    }
    //Función para cambiar estado a eliminado- END
}
