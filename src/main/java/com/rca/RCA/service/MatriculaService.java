package com.rca.RCA.service;

import com.rca.RCA.entity.*;
import com.rca.RCA.repository.AlumnoRepository;
import com.rca.RCA.repository.AnioLectivoRepository;
import com.rca.RCA.repository.AulaRepository;
import com.rca.RCA.repository.MatriculaRepository;
import com.rca.RCA.type.*;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
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
import org.springframework.http.*;
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

    //Función para obtener una matricula-START
    public ApiResponse<MatriculaDTO> one(String id) throws ResourceNotFoundException {
        MatriculaEntity matriculaEntity = this.matriculaRepository.findByUniqueIdentifier(id, ConstantsGeneric.CREATED_STATUS).orElseThrow(() -> new ResourceNotFoundException("Matrícula no existe"));

        ApiResponse<MatriculaDTO> apiResponse = new ApiResponse<>();
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(matriculaEntity.getMatriculaDTO());
        return apiResponse;
    }
        ApiResponse<MatriculaDTO> apiResponse = new ApiResponse<>();
    //Función para obtener una matricula-END

    //Función para agregar una matricula- START
    public ApiResponse<MatriculaDTO> add(MatriculaDTO matriculaDTO) throws ResourceNotFoundException, AttributeException {
        log.info("Aula Alumno AnioLectivo {} {} {}", matriculaDTO.getAulaDTO().getId(), matriculaDTO.getAlumnoDTO().getId(), matriculaDTO.getAnioLectivoDTO().getId());
        if(matriculaRepository.existsByAuAlAn("", matriculaDTO.getAulaDTO().getId(),matriculaDTO.getAlumnoDTO().getId(), matriculaDTO.getAnioLectivoDTO().getId(), ConstantsGeneric.CREATED_STATUS))
            throw new AttributeException("La matrícula ya existe");
        ApiResponse<MatriculaDTO> apiResponse = new ApiResponse<>();
        MatriculaEntity matriculaEntity = new MatriculaEntity();
        AulaEntity aulaEntity=this.aulaRepository.findByUniqueIdentifier(matriculaDTO.getAulaDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Aula no existe"));
        AlumnoEntity alumnoEntity=this.alumnoRepository.findByUniqueIdentifier(matriculaDTO.getAlumnoDTO().getId()).orElseThrow(()-> new ResourceNotFoundException("Alumno no existe"));
        AnioLectivoEntity anioLectivoEntity=this.anioLectivoRepository.findByUniqueIdentifier(matriculaDTO.getAnioLectivoDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Año lectivo no existe"));
        //Update in database


        matriculaEntity.setCode(Code.generateCode(Code.MAT_CODE, this.matriculaRepository.count() + 1, Code.MAT_LENGTH));
        matriculaEntity.setDate(matriculaDTO.getDate());
        matriculaEntity.setAulaEntity(aulaEntity);
        matriculaEntity.setAlumnoEntity(alumnoEntity);
        matriculaEntity.setAnio_lectivoEntity(anioLectivoEntity);
        matriculaEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        matriculaEntity.setStatus(ConstantsGeneric.CREATED_STATUS);
        matriculaEntity.setCreateAt(LocalDateTime.now());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.matriculaRepository.save(matriculaEntity).getMatriculaDTO());
        return apiResponse;
    }
    //Función para agregar una matricula- END

      //Función para actualizar una matricula-START
    public ApiResponse<MatriculaDTO> update(MatriculaDTO matriculaDTO) throws AttributeException, ResourceNotFoundException {
        if(matriculaDTO.getId().isBlank())
            throw new ResourceNotFoundException("Matrícula no existe");
        if(matriculaRepository.existsByAuAlAn(matriculaDTO.getId(), matriculaDTO.getAulaDTO().getId(),matriculaDTO.getAlumnoDTO().getId(), matriculaDTO.getAnioLectivoDTO().getId(), ConstantsGeneric.CREATED_STATUS))
            throw new AttributeException("La matrícula ya existe");
        ApiResponse<MatriculaDTO> apiResponse = new ApiResponse<>();
        MatriculaEntity matriculaEntity = this.matriculaRepository.findByUniqueIdentifier(matriculaDTO.getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()->new ResourceNotFoundException("Matrícula no existe"));
        //Verifica que el id y el status sean válidos
        AlumnoEntity alumnoEntity = this.alumnoRepository.findByUniqueIdentifier(matriculaDTO.getAlumnoDTO().getId()).orElseThrow(()-> new ResourceNotFoundException("Alumno no existe"));
        AulaEntity aulaEntity= this.aulaRepository.findByUniqueIdentifier(matriculaDTO.getAulaDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Aula no existe"));
        AnioLectivoEntity anioLectivoEntity = this.anioLectivoRepository.findByUniqueIdentifier(matriculaDTO.getAnioLectivoDTO().getId(), ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Año lectivo no existe"));
        //Set update data
        matriculaEntity.setDate(matriculaDTO.getDate());
        matriculaEntity.setAlumnoEntity(alumnoEntity);
        matriculaEntity.setAulaEntity(aulaEntity);
        matriculaEntity.setAnio_lectivoEntity(anioLectivoEntity);
        matriculaEntity.setUpdateAt(LocalDateTime.now());
        //Update in database
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.matriculaRepository.save(matriculaEntity).getMatriculaDTO());
        return apiResponse;
    }
    //Función para actualizar una matricula -END

    //Función para cambiar estado a eliminado- START
    //id dto=uniqueIdentifier Entity
    public ApiResponse<MatriculaDTO> delete(String id) throws ResourceNotFoundException {
        MatriculaEntity matriculaEntity=this.matriculaRepository.findByUniqueIdentifier(id, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Matrícula no existe"));

        ApiResponse<MatriculaDTO> apiResponse = new ApiResponse<>();
        matriculaEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
        matriculaEntity.setDeleteAt(LocalDateTime.now());
        log.info("Eliminación exitosa");
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(this.matriculaRepository.save(matriculaEntity).getMatriculaDTO());
        return apiResponse;
    }

    public ResponseEntity<Resource> exportMatricula(String id_alumno, String id_aniolectivo) throws ResourceNotFoundException {
        log.info("id_alumno id_aniolectivo {} {}", id_alumno, id_aniolectivo);
        AlumnoEntity alumnoEntity = this.alumnoRepository.findByUniqueIdentifier(id_alumno).orElseThrow(()-> new ResourceNotFoundException("Alumno no existe"));
        AnioLectivoEntity anioLectivoEntity = this.anioLectivoRepository.findByUniqueIdentifier(id_aniolectivo, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Año lectivo no existe"));
        AulaEntity aulaEntity = this.matriculaRepository.findAulaMatriculado(id_alumno, id_aniolectivo, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("Aula  no existe"));
        try {
            final File file = ResourceUtils.getFile("classpath:reportes/ficha_matricula.jasper"); //la ruta del reporte
            final File imgLogo = ResourceUtils.getFile("classpath:images/logo.png"); //Ruta de la imagen
            final JasperReport report = (JasperReport) JRLoader.loadObject(file);

            //Se consultan los datos para el reporte de cursos matriculados DTO
            List<CursoEntity> cursoEntities = this.matriculaRepository.findCursosMatriculados(id_alumno, id_aniolectivo, ConstantsGeneric.CREATED_STATUS).orElse(new ArrayList<>());
            List<DocentexCursoEntity> docentexCursoEntities = this.matriculaRepository.findDocentexCursosMatriculados(id_alumno, id_aniolectivo, ConstantsGeneric.CREATED_STATUS).orElse(new ArrayList<>());

            //Se agregan los datos para ReporteApoderadosDTO
            List<ReporteFichaMatriculaDTO> reporteFichaMatriculaDTOS= new ArrayList<>();

            for (int i = 0; i < docentexCursoEntities.size(); i++) {
                ReporteFichaMatriculaDTO reporteFichaMatriculaDTO = new ReporteFichaMatriculaDTO();
                reporteFichaMatriculaDTO.setCursoDTO(cursoEntities.get(i).getCursoDTO());
                reporteFichaMatriculaDTO.setDocenteDTO(docentexCursoEntities.get(i).getDocenteEntity().getDocenteDTO());
                reporteFichaMatriculaDTOS.add(reporteFichaMatriculaDTO);
            }
            //Se llenan los parámetros del reporte
            final HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("logoEmpresa", new FileInputStream(imgLogo));
            parameters.put("nombreAlumno", alumnoEntity.getNombresCompletosAl());
            parameters.put("docAlumno", alumnoEntity.getUsuarioEntity().getNumdoc());
            parameters.put("fechaNacimiento", alumnoEntity.getUsuarioEntity().getBirthdate());
            parameters.put("grado", aulaEntity.getGradoEntity().getName().toString());
            parameters.put("seccion", aulaEntity.getSeccionEntity().getName().toString());
            parameters.put("año", anioLectivoEntity.getName());
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
            new ResourceNotFoundException("Reporte no encontrado");
        }
        return null;
    }

    //Función para cambiar estado a eliminado- END

    //Función para generar pdf
    public ResponseEntity<Resource> exportListaAlumnos(String uniqueIdentifierAula, String uniqueIdentifierAnio) throws ResourceNotFoundException {

        try{
            List<AlumnoEntity> alumnoEntities = this.alumnoRepository.findByAulaPeriodo(uniqueIdentifierAula, uniqueIdentifierAnio, ConstantsGeneric.CREATED_STATUS).orElseThrow(()->new ResourceNotFoundException("Alumnos no encontrados"));
            AulaEntity aulaEntity = this.aulaRepository.findByUniqueIdentifier(uniqueIdentifierAula, ConstantsGeneric.CREATED_STATUS).orElseThrow(()->new ResourceNotFoundException("Aula no encontrada"));
            AnioLectivoEntity anioLectivoEntity = this.anioLectivoRepository.findByUniqueIdentifier(uniqueIdentifierAnio, ConstantsGeneric.CREATED_STATUS).orElseThrow(()->new ResourceNotFoundException("Año lectivo no encontrado"));
            final File file = ResourceUtils.getFile("classpath:reportes/alumnosAula.jasper");
            final File imgLogo = ResourceUtils.getFile("classpath:images/logo.png");
            final JasperReport report = (JasperReport) JRLoader.loadObject(file);

            final HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("logoEmpresa", new FileInputStream(imgLogo));
            parameters.put("Grado", String.valueOf(aulaEntity.getAulaDTO().getGradoDTO().getName()));
            parameters.put("Seccion", String.valueOf(aulaEntity.getAulaDTO().getSeccionDTO().getName()));
            parameters.put("Anio", anioLectivoEntity.getAnioLectivoDTO().getName());
            parameters.put("dsAlumnosAula", new JRBeanCollectionDataSource((Collection<?>) this.alumnoRepository.findByAulaPeriodoI(uniqueIdentifierAula, uniqueIdentifierAnio, ConstantsGeneric.CREATED_STATUS)));

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ByteArrayResource("Error al generar el reporte PDF".getBytes()));
        }
    }
}
