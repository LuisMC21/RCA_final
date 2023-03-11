package com.rca.RCA.service;


import com.rca.RCA.entity.*;
import com.rca.RCA.repository.*;
import com.rca.RCA.type.*;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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

import javax.validation.constraints.NotNull;
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
    //Función para agregar un aula- START
    public ApiResponse<AulaDTO> add(AulaDTO aulaDTO) {
        log.info("Grado Seccion {} {}", aulaDTO.getGradoDTO().getId(), aulaDTO.getSeccionDTO().getId());
        ApiResponse<AulaDTO> apiResponse = new ApiResponse<>();
        AulaEntity aulaEntity = new AulaEntity();
        Optional<GradoEntity> optionalGradoEntity = this.gradoRepository.findByUniqueIdentifier(aulaDTO.getGradoDTO().getId());
        Optional<SeccionEntity> optionalSeccionEntity = this.seccionRepository.findByUniqueIdentifier(aulaDTO.getSeccionDTO().getId());
        if (optionalGradoEntity.isPresent() &&
                optionalSeccionEntity.isPresent()
                && this.aulaRepository.findByGradoYSeccion(optionalGradoEntity.get().getId(), optionalSeccionEntity.get().getId(), ConstantsGeneric.CREATED_STATUS).isEmpty()
                && optionalGradoEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)
                && optionalSeccionEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
            //Update in database
            aulaEntity.setCode(Code.generateCode(Code.CLASSROOM_CODE, this.aulaRepository.count() + 1, Code.CLASSROOM_LENGTH));
            aulaEntity.setGradoEntity(optionalGradoEntity.get());
            aulaEntity.setSeccionEntity(optionalSeccionEntity.get());
            aulaEntity.setUniqueIdentifier(UUID.randomUUID().toString());
            aulaEntity.setStatus(ConstantsGeneric.CREATED_STATUS);
            aulaEntity.setCreateAt(LocalDateTime.now());
            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.aulaRepository.save(aulaEntity).getAulaDTO());
            return apiResponse;

        } else {
            log.warn("No se completó el registro");

            if (optionalGradoEntity.isEmpty() || optionalGradoEntity.get().getStatus().equals(ConstantsGeneric.DELETED_STATUS)) {
                apiResponse.setCode("GRADE_DOES_NOT_EXISTS");
            }
            if (optionalSeccionEntity.isEmpty() || optionalSeccionEntity.get().getStatus().equals(ConstantsGeneric.DELETED_STATUS)) {
                apiResponse.setCode("SECTION_DOES_NOT_EXISTS");
            }
            if (optionalGradoEntity.isPresent() && optionalSeccionEntity.isPresent()) {
                if (this.aulaRepository.findByGradoYSeccion(optionalGradoEntity.get().getId(), optionalSeccionEntity.get().getId(), ConstantsGeneric.CREATED_STATUS).isPresent()) {
                    apiResponse.setCode("CLASSROOM_EXISTS");
                }
            }

            apiResponse.setMessage("No se pudo registrar la sección en el grado");
        }
        return apiResponse;
    }
    //Función para agregar un aula- END

    //Función para actualizar un aula-START
    public ApiResponse<AulaDTO> update(AulaDTO aulaDTO) {
        ApiResponse<AulaDTO> apiResponse = new ApiResponse<>();
        if (!aulaDTO.getId().isEmpty()) {
            Optional<AulaEntity> optionalAulaEntity = this.aulaRepository.findByUniqueIdentifier(aulaDTO.getId());
            //Verifica que el id y el status sean válidos
            if (optionalAulaEntity.isPresent()
                    && optionalAulaEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
                Optional<GradoEntity> optionalGradoEntity = Optional.empty();
                Optional<SeccionEntity> optionalSeccionEntity = Optional.empty();
                if (aulaDTO.getGradoDTO().getId() != null) {
                    optionalGradoEntity = this.gradoRepository.findByUniqueIdentifier(aulaDTO.getGradoDTO().getId());
                }
                if (aulaDTO.getSeccionDTO().getId() != null) {
                    optionalSeccionEntity = this.seccionRepository.findByUniqueIdentifier(aulaDTO.getSeccionDTO().getId());
                }
                //Set update data
                if (optionalGradoEntity.isPresent() && optionalGradoEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
                    optionalAulaEntity.get().setGradoEntity(optionalGradoEntity.get());
                } else {
                    log.warn("No se actualizó el registro");
                    apiResponse.setSuccessful(false);
                    apiResponse.setMessage("Grado no existente, no se puede actualizar");
                    apiResponse.setCode("GRADE_DOES_NOT_EXISTS");
                    return apiResponse;
                }
                if (optionalSeccionEntity.isPresent() && optionalSeccionEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
                    optionalAulaEntity.get().setSeccionEntity(optionalSeccionEntity.get());
                } else {
                    log.warn("No se actualizó el registro");
                    apiResponse.setSuccessful(false);
                    apiResponse.setMessage("Sección no existente, no se puede actualizar");
                    apiResponse.setCode("SECTION_DOES_NOT_EXISTS");
                    return apiResponse;
                }
                if (this.aulaRepository.findByGradoYSeccion(optionalAulaEntity.get().getGradoEntity().getId(), optionalAulaEntity.get().getSeccionEntity().getId(), ConstantsGeneric.CREATED_STATUS).isPresent()
                        && this.aulaRepository.findByGradoYSeccion(optionalAulaEntity.get().getGradoEntity().getId(), optionalAulaEntity.get().getSeccionEntity().getId(), ConstantsGeneric.CREATED_STATUS).get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
                    log.warn("No se actualizó el registro");
                    apiResponse.setSuccessful(false);
                    apiResponse.setMessage("Aula ya existente, no se puede actualizar");
                    apiResponse.setCode("CLASSROOM_EXISTS");
                    return apiResponse;
                }
                optionalAulaEntity.get().setUpdateAt(LocalDateTime.now());
                //Update in database
                apiResponse.setSuccessful(true);
                apiResponse.setMessage("ok");
                apiResponse.setData(this.aulaRepository.save(optionalAulaEntity.get()).getAulaDTO());
            } else {
                log.warn("No se actualizó el registro");
                apiResponse.setSuccessful(false);
                apiResponse.setMessage("No existe el aula para poder actualizar");
                apiResponse.setCode("CLASSROOM_DOES_NOT_EXISTS");
            }
            return apiResponse;
        }
        log.warn("No se actualizó el registro");
        apiResponse.setSuccessful(false);
        apiResponse.setMessage("No existe el aula para poder actualizar");
        apiResponse.setCode("CLASSROOM_DOES_NOT_EXISTS");
        return apiResponse;
    }
    //Función para actualizar un aula-END

    //Función para cambiar estado a eliminado- START
    //id dto=uniqueIdentifier Entity
    public ApiResponse<AulaDTO> delete(String id) {
        ApiResponse<AulaDTO> apiResponse = new ApiResponse<>();
        //Verifica que el id y el status sean válidos
        Optional<AulaEntity> optionalAulaEntity = this.aulaRepository.findByUniqueIdentifier(id);
        if (optionalAulaEntity.isPresent() && optionalAulaEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
            AulaEntity aulaEntity = optionalAulaEntity.get();
            aulaEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            aulaEntity.setDeleteAt(LocalDateTime.now());

            //Eliminar lista de matriculas del aula
            Optional<List<MatriculaEntity>> optionalMatriculaEntities = this.matriculaRepository.findByAula(aulaEntity.getUniqueIdentifier(), ConstantsGeneric.CREATED_STATUS);
            for (int i = 0; i < optionalMatriculaEntities.get().size(); i++) {
                optionalMatriculaEntities.get().get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
                optionalMatriculaEntities.get().get(i).setDeleteAt(aulaEntity.getDeleteAt());
                this.matriculaService.delete(optionalMatriculaEntities.get().get(i).getCode());
            }

            Optional<List<DocentexCursoEntity>> optionalDocentexCursoEntities = this.docentexCursoRepository.findByAula(aulaEntity.getUniqueIdentifier(), ConstantsGeneric.CREATED_STATUS);
            for (int i = 0; i < optionalDocentexCursoEntities.get().size(); i++) {
                optionalDocentexCursoEntities.get().get(i).setStatus(ConstantsGeneric.DELETED_STATUS);
                optionalDocentexCursoEntities.get().get(i).setDeleteAt(aulaEntity.getDeleteAt());
                this.docentexCursoService.delete(optionalDocentexCursoEntities.get().get(i).getCode());
            }

            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.aulaRepository.save(aulaEntity).getAulaDTO());
        } else {
            log.warn("No se eliminó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("CLASSROOM_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe el aula para poder eliminar");
        }
        return apiResponse;
    }

    @NotNull
    public ResponseEntity<Resource> exportListApoderados(String id_aula, String id_aniolectivo) {
        log.info("id_aula {}", id_aula);
        Optional<AulaEntity> optionalAulaEntity = this.aulaRepository.findByUniqueIdentifier(id_aula);
        Optional<AnioLectivoEntity> optionalAnioLectivoEntity = this.anioLectivoRepository.findByUniqueIdentifier(id_aniolectivo);
        if (optionalAulaEntity.isPresent() && (optionalAulaEntity.get().getStatus().equalsIgnoreCase(ConstantsGeneric.CREATED_STATUS)) &&
                optionalAnioLectivoEntity.isPresent() && optionalAulaEntity.get().getStatus().equalsIgnoreCase(ConstantsGeneric.CREATED_STATUS)) {
            try {
                final AulaEntity aulaEntity = optionalAulaEntity.get();
                final File file = ResourceUtils.getFile("classpath:reportes/lista_apoderados.jasper"); //la ruta del reporte
                final File imgLogo = ResourceUtils.getFile("classpath:images/logoC.jpg"); //Ruta de la imagen
                final JasperReport report = (JasperReport) JRLoader.loadObject(file);
                //Se consultan los datos para el reporte de apoderados DTO
                Optional<List<AlumnoEntity>> optionalAlumnoDTOS = this.aulaRepository.findAlumnosxAula(id_aula, id_aniolectivo, ConstantsGeneric.CREATED_STATUS);
                Optional<List<ApoderadoEntity>> optionalApoderadoDTOS = this.aulaRepository.findApoderadosxAula(id_aula, id_aniolectivo, ConstantsGeneric.CREATED_STATUS);
                //Se agregan los datos para ReporteApoderadosDTO
                List<ReporteApoderadosDTO> reporteApoderadosDTOList= new ArrayList<>();
                for (int i = 0; i < optionalAlumnoDTOS.get().size(); i++) {
                    ReporteApoderadosDTO reporteApoderadosDTO = new ReporteApoderadosDTO();
                    reporteApoderadosDTO.setAlumnoDTO(optionalAlumnoDTOS.get().get(i).getAlumnoDTO());
                    reporteApoderadosDTO.setApoderadoDTO(optionalApoderadoDTOS.get().get(i).getApoderadoDTO());
                    reporteApoderadosDTOList.add(reporteApoderadosDTO);
                }

                //Se llenan los parámetros del reporte
                final HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("logoEmpresa", new FileInputStream(imgLogo));
                parameters.put("grado", aulaEntity.getAulaDTO().getGradoDTO().getName().toString());
                parameters.put("seccion", aulaEntity.getAulaDTO().getSeccionDTO().getName().toString());
                parameters.put("año", optionalAnioLectivoEntity.get().getName());
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
                e.printStackTrace();
            }
        }else{
            return ResponseEntity.noContent().build();//Reporte no encontrado
        }
        return null;
    }
    //Función para cambiar estado a eliminado- END
}
