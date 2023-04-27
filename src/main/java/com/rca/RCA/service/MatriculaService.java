package com.rca.RCA.service;

import com.rca.RCA.entity.*;
import com.rca.RCA.repository.AlumnoRepository;
import com.rca.RCA.repository.AnioLectivoRepository;
import com.rca.RCA.repository.AulaRepository;
import com.rca.RCA.repository.MatriculaRepository;
import com.rca.RCA.type.*;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.*;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class MatriculaService {
    @Autowired
    private MatriculaRepository matriculaRepository;
    @Autowired
    private AlumnoRepository alumnoRepository;
    @Autowired
    private AulaRepository aulaRepository;
    @Autowired
    private AnioLectivoRepository anioLectivoRepository;

    //Función para listar mastriculas con paginación-START
    public ApiResponse<Pagination<MatriculaDTO>> getList(String filter, int page, int size){
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<MatriculaDTO>> apiResponse = new ApiResponse<>();
        Pagination<MatriculaDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.matriculaRepository.findCountMatricula(ConstantsGeneric.CREATED_STATUS, filter));
        if(pagination.getCountFilter()>0){
            Pageable pageable= PageRequest.of(page, size);
            List<MatriculaEntity> matriculaEntities=this.matriculaRepository.findMatricula(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            log.info(matriculaEntities.size());
            pagination.setList(matriculaEntities.stream().map(MatriculaEntity::getMatriculaDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }
    //Función para listar matriculas-END

    //Función para agregar una matricula- START
    public ApiResponse<MatriculaDTO> add(MatriculaDTO matriculaDTO){
        log.info("Aula Alumno AnioLectivo {} {} {}", matriculaDTO.getAulaDTO().getId(), matriculaDTO.getAlumnoDTO().getId(), matriculaDTO.getAnioLectivoDTO().getId());
        ApiResponse<MatriculaDTO> apiResponse = new ApiResponse<>();
        MatriculaEntity matriculaEntity = new MatriculaEntity();
        Optional<AulaEntity> optionalAulaEntity=this.aulaRepository.findByUniqueIdentifier(matriculaDTO.getAulaDTO().getId(), ConstantsGeneric.CREATED_STATUS);
        Optional<AlumnoEntity> optionalAlumnoEntity=this.alumnoRepository.findByUniqueIdentifier(matriculaDTO.getAlumnoDTO().getId());
        Optional<AnioLectivoEntity> optionalAnioLectivoEntity=this.anioLectivoRepository.findByUniqueIdentifier(matriculaDTO.getAnioLectivoDTO().getId());
        if(optionalAulaEntity.isPresent() && optionalAulaEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)
                && optionalAlumnoEntity.isPresent() && optionalAlumnoEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)
                && optionalAnioLectivoEntity.isPresent() && optionalAnioLectivoEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)){
            if(this.matriculaRepository.findByAuAlAn(matriculaDTO.getAulaDTO().getId(), matriculaDTO.getAlumnoDTO().getId(), matriculaDTO.getAnioLectivoDTO().getId(), ConstantsGeneric.CREATED_STATUS).isEmpty()) {
                //Update in database
                matriculaEntity.setCode(Code.generateCode(Code.MAT_CODE, this.matriculaRepository.count() + 1, Code.MAT_LENGTH));
                matriculaEntity.setDate(matriculaDTO.getDate());
                matriculaEntity.setAulaEntity(optionalAulaEntity.get());
                matriculaEntity.setAlumnoEntity(optionalAlumnoEntity.get());
                matriculaEntity.setAnio_lectivoEntity(optionalAnioLectivoEntity.get());
                matriculaEntity.setUniqueIdentifier(UUID.randomUUID().toString());
                matriculaEntity.setStatus(ConstantsGeneric.CREATED_STATUS);
                matriculaEntity.setCreateAt(LocalDateTime.now());
                apiResponse.setSuccessful(true);
                apiResponse.setMessage("ok");
                apiResponse.setData(this.matriculaRepository.save(matriculaEntity).getMatriculaDTO());
                return apiResponse;
            }else{
                log.warn("No se completó el registro");
                apiResponse.setSuccessful(false);
                apiResponse.setCode("ENROLLMENT_EXISTS");
            }
        }else{
            log.warn("No se completó el registro");
            apiResponse.setSuccessful(false);
            if(optionalAulaEntity.isEmpty() || optionalAulaEntity.get().getStatus().equals(ConstantsGeneric.DELETED_STATUS)) {
                apiResponse.setCode("CLASSROOM_DOES_NOT_EXISTS");
            }
            if(optionalAlumnoEntity.isEmpty() || optionalAlumnoEntity.get().getStatus().equals(ConstantsGeneric.DELETED_STATUS)) {
                apiResponse.setCode("STUDENT_DOES_NOT_EXISTS");
            }
            if(optionalAnioLectivoEntity.isEmpty() || optionalAnioLectivoEntity.get().getStatus().equals(ConstantsGeneric.DELETED_STATUS)) {
                apiResponse.setCode("SCHOOL_YEAR_DOES_NOT_EXISTS");
            }

        }
        apiResponse.setMessage("No se pudo registrar la matrícula");
        return apiResponse;
    }
    //Función para agregar una matricula- END

      //Función para actualizar una matricula-START
    public ApiResponse<MatriculaDTO> update(MatriculaDTO matriculaDTO){
        ApiResponse<MatriculaDTO> apiResponse = new ApiResponse<>();
        if(!matriculaDTO.getId().isEmpty()) {
            Optional<MatriculaEntity> optionalMatriculaEntity = this.matriculaRepository.findByUniqueIdentifier(matriculaDTO.getId());
            //Verifica que el id y el status sean válidos
            if (optionalMatriculaEntity.isPresent() && optionalMatriculaEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)) {
                Optional<AulaEntity> optionalAulaEntity=Optional.empty();
                Optional<AlumnoEntity> optionalAlumnoEntity=Optional.empty();
                Optional<AnioLectivoEntity> optionalAnioLectivoEntity = Optional.empty();
                if (matriculaDTO.getAlumnoDTO().getId() != null) {
                    optionalAlumnoEntity = this.alumnoRepository.findByUniqueIdentifier(matriculaDTO.getAlumnoDTO().getId());
                }
                if (matriculaDTO.getAulaDTO().getId() != null) {
                    optionalAulaEntity = this.aulaRepository.findByUniqueIdentifier(matriculaDTO.getAulaDTO().getId(), ConstantsGeneric.CREATED_STATUS);
                }
                if (matriculaDTO.getAnioLectivoDTO().getId() != null) {
                    optionalAnioLectivoEntity = this.anioLectivoRepository.findByUniqueIdentifier(matriculaDTO.getAnioLectivoDTO().getId());
                }
                //Set update data
                optionalAlumnoEntity.ifPresent(alumnoEntity -> optionalMatriculaEntity.get().setAlumnoEntity(alumnoEntity));
                optionalAulaEntity.ifPresent(aulaEntity -> optionalMatriculaEntity.get().setAulaEntity(aulaEntity));
                optionalAnioLectivoEntity.ifPresent(anioLectivoEntity -> optionalMatriculaEntity.get().setAnio_lectivoEntity(anioLectivoEntity));

                if(this.matriculaRepository.findByAuAlAn(optionalMatriculaEntity.get().getAulaEntity().getUniqueIdentifier(),optionalMatriculaEntity.get().getAlumnoEntity().getUniqueIdentifier(), optionalMatriculaEntity.get().getAnio_lectivoEntity().getUniqueIdentifier(), ConstantsGeneric.CREATED_STATUS).isEmpty()) {
                    optionalMatriculaEntity.get().setUpdateAt(LocalDateTime.now());
                    //Update in database
                    apiResponse.setSuccessful(true);
                    apiResponse.setMessage("ok");
                    apiResponse.setData(this.matriculaRepository.save(optionalMatriculaEntity.get()).getMatriculaDTO());
                }else{
                    log.warn("No se actualizó el registro");
                    apiResponse.setSuccessful(false);
                    apiResponse.setMessage("Ya existe esta matrícula");
                    apiResponse.setCode("ENROLLMENT_EXISTS");
                }
                return apiResponse;
            }
        }
        log.warn("No se actualizó el registro");
        apiResponse.setSuccessful(false);
        apiResponse.setMessage("No existe la matrícula para poder actualizar");
        apiResponse.setCode("ENROLLMENT_DOES_NOT_EXISTS");
        return apiResponse;
    }
    //Función para actualizar una matricula -END

    //Función para cambiar estado a eliminado- START
    //id dto=uniqueIdentifier Entity
    public ApiResponse<MatriculaDTO> delete(String id){
        ApiResponse<MatriculaDTO> apiResponse = new ApiResponse<>();
        //Verifica que el id y el status sean válidos
        Optional<MatriculaEntity> optionalMatriculaEntity=this.matriculaRepository.findByUniqueIdentifier(id);
        if(optionalMatriculaEntity.isPresent() && optionalMatriculaEntity.get().getStatus().equals(ConstantsGeneric.CREATED_STATUS)){
            MatriculaEntity matriculaEntity =optionalMatriculaEntity.get();
            matriculaEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            matriculaEntity.setDeleteAt(LocalDateTime.now());

            log.info("Eliminación exitosa");
            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.matriculaRepository.save(matriculaEntity).getMatriculaDTO());
        } else{
            log.warn("No se eliminó el registro");
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ENROLLMENT_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe la matrícula para poder eliminar");
        }
        return apiResponse;
    }

    public ResponseEntity<Resource> exportMatricula(String id_alumno, String id_aniolectivo) {
        log.info("id_alumno id_aniolectivo {} {}", id_alumno, id_aniolectivo);
        Optional<AlumnoEntity> optionalAlumnoEntity = this.alumnoRepository.findByUniqueIdentifier(id_alumno);
        Optional<AnioLectivoEntity> optionalAnioLectivoEntity = this.anioLectivoRepository.findByUniqueIdentifier(id_aniolectivo);
        Optional<GradoEntity> optionalGradoEntity = this.matriculaRepository.findGradoMatriculado(id_alumno, id_aniolectivo, ConstantsGeneric.CREATED_STATUS);
        Optional<SeccionEntity> optionalSeccionEntity = this.matriculaRepository.findSeccionMatriculado(id_alumno, id_aniolectivo, ConstantsGeneric.CREATED_STATUS);

        if (optionalAlumnoEntity.isPresent() && (optionalAlumnoEntity.get().getStatus().equalsIgnoreCase(ConstantsGeneric.CREATED_STATUS)) &&
                optionalAnioLectivoEntity.isPresent() && optionalAnioLectivoEntity.get().getStatus().equalsIgnoreCase(ConstantsGeneric.CREATED_STATUS) &&
                optionalGradoEntity.isPresent() && optionalGradoEntity.get().getStatus().equalsIgnoreCase(ConstantsGeneric.CREATED_STATUS) &&
                optionalSeccionEntity.isPresent() && optionalSeccionEntity.get().getStatus().equalsIgnoreCase(ConstantsGeneric.CREATED_STATUS)) {
            try {
                final AlumnoEntity alumnoEntity = optionalAlumnoEntity.get();
                final File file = ResourceUtils.getFile("classpath:reportes/ficha_matricula.jasper"); //la ruta del reporte
                final File imgLogo = ResourceUtils.getFile("classpath:images/logoC.jpg"); //Ruta de la imagen
                final JasperReport report = (JasperReport) JRLoader.loadObject(file);
                //Se consultan los datos para el reporte de cursos matriculados DTO
                Optional<List<CursoEntity>> optionalCursoEntities = this.matriculaRepository.findCursosMatriculados(id_alumno, id_aniolectivo, ConstantsGeneric.CREATED_STATUS);
                Optional<List<DocenteEntity>> optionalDocenteEntities = this.matriculaRepository.findDocentesdeCursosMatriculados(id_alumno, id_aniolectivo, ConstantsGeneric.CREATED_STATUS);
                //Se agregan los datos para ReporteApoderadosDTO
                List<ReporteFichaMatriculaDTO> reporteFichaMatriculaDTOS= new ArrayList<>();
                for (int i = 0; i < optionalCursoEntities.get().size(); i++) {
                    ReporteFichaMatriculaDTO reporteFichaMatriculaDTO = new ReporteFichaMatriculaDTO();
                    reporteFichaMatriculaDTO.setCursoDTO(optionalCursoEntities.get().get(i).getCursoDTO());
                    reporteFichaMatriculaDTO.setDocenteDTO(optionalDocenteEntities.get().get(i).getDocenteDTO());
                    reporteFichaMatriculaDTOS.add(reporteFichaMatriculaDTO);
                }
                //Se llenan los parámetros del reporte
                final HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("logoEmpresa", new FileInputStream(imgLogo));
                parameters.put("nombreAlumno", alumnoEntity.getNombresCompletosAl());
                parameters.put("docAlumno", alumnoEntity.getUsuarioEntity().getNumdoc());
                parameters.put("fechaNacimiento", alumnoEntity.getUsuarioEntity().getBirthdate());
                parameters.put("grado", optionalGradoEntity.get().getName().toString());
                parameters.put("seccion", optionalSeccionEntity.get().getName().toString());
                parameters.put("año", optionalAnioLectivoEntity.get().getName());
                parameters.put("dsLA", new JRBeanArrayDataSource(reporteFichaMatriculaDTOS.toArray()));
                //Se imprime el reporte
                JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
                byte [] reporte = JasperExportManager.exportReportToPdf(jasperPrint);
                String sdf = (new SimpleDateFormat("dd/MM/yyyy")).format(new Date());
                StringBuilder stringBuilder = new StringBuilder().append("MatriculaPDF:");
                ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                        .filename(stringBuilder
                                .append(alumnoEntity.getCode())
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

    //Función para generar pdf
    public ResponseEntity<Resource> exportListaAlumnos(String uniqueIdentifierAula, String uniqueIdentifierPeriodo) {

        Optional<List<AlumnoEntity>> optionalAlumnoEntity = this.alumnoRepository.findByAulaPeriodo(uniqueIdentifierAula, uniqueIdentifierPeriodo);
        Optional<AulaEntity> optionalAulaEntity = this.aulaRepository.findByUniqueIdentifier(uniqueIdentifierAula, ConstantsGeneric.CREATED_STATUS);
        Optional<AnioLectivoEntity> optionalAnioLectivoEntity = this.anioLectivoRepository.findByUniqueIdentifier(uniqueIdentifierPeriodo);

        if (optionalAlumnoEntity.isPresent() && optionalAlumnoEntity.isPresent() && optionalAnioLectivoEntity.isPresent()){

            try{
                final AnioLectivoEntity anioLectivoEntity = optionalAnioLectivoEntity.get();
                final AulaEntity aulaEntity = optionalAulaEntity.get();
                final File file = ResourceUtils.getFile("classpath:reportes/alumnosAula.jasper");
                final File imgLogo = ResourceUtils.getFile("classpath:images/logo.png");
                final JasperReport report = (JasperReport) JRLoader.loadObject(file);

                final HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("logoEmpresa", new FileInputStream(imgLogo));
                parameters.put("Grado", String.valueOf(aulaEntity.getAulaDTO().getGradoDTO().getName()));
                parameters.put("Seccion", String.valueOf(aulaEntity.getAulaDTO().getSeccionDTO().getName()));
                parameters.put("Anio", anioLectivoEntity.getAnioLectivoDTO().getName());
                parameters.put("dsAlumnosAula", new JRBeanCollectionDataSource((Collection<?>) this.alumnoRepository.findByAulaPeriodoI(uniqueIdentifierAula, uniqueIdentifierPeriodo)));

                JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
                byte[] reporte = JasperExportManager.exportReportToPdf(jasperPrint);
                String sdf = (new SimpleDateFormat("dd/MM/yyyy")).format(new Date());
                StringBuilder stringBuilder = new StringBuilder().append("InvoicePDF:");
                ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                        .filename(stringBuilder.append("1")
                                .append("generateDate:")
                                .append(sdf)
                                .append(".pdf")
                                .toString())
                        .build();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentDisposition(contentDisposition);

                return ResponseEntity.ok().contentLength((long) reporte.length)
                        .contentType(MediaType.APPLICATION_PDF)
                        .headers(httpHeaders).body(new ByteArrayResource(reporte));
            }catch (Exception e){
                e.printStackTrace();
            }

        }else{
            return  ResponseEntity.noContent().build();
        }
        return null;
    }
}
