package com.rca.RCA.service;

import com.rca.RCA.entity.*;
import com.rca.RCA.entity.AsistenciaEntity;
import com.rca.RCA.repository.*;
import com.rca.RCA.type.*;
import com.rca.RCA.type.AsistenciaDTO;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import com.rca.RCA.util.exceptions.GlobalException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.*;
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
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;
    @Autowired
    private AlumnoRepository alumnoRepository;
    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private AnioLectivoRepository anioLectivoRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;
    @Autowired
    private PeriodoRepository periodoRepository;

    @Autowired
    private AulaRepository aulaRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    public AsistenciaService(AsistenciaRepository asistenciaRepository, AlumnoRepository alumnoRepository, ClaseRepository claseRepository){
        this.asistenciaRepository = asistenciaRepository;
        this.alumnoRepository = alumnoRepository;
        this.claseRepository = claseRepository;
    }

    //Listar asistencia
    public ApiResponse<Pagination<AsistenciaDTO>> getList(String filter, int page, int size) {
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<AsistenciaDTO>> apiResponse = new ApiResponse<>();
        Pagination<AsistenciaDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.asistenciaRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<AsistenciaEntity> AsistenciaEntities = this.asistenciaRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(AsistenciaEntities.stream().map(AsistenciaEntity::getAsistenciaDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    public ApiResponse<Pagination<AsistenciaDTO>> getList(String filter, int page, int size, String periodo, String aula, String curso) {
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<AsistenciaDTO>> apiResponse = new ApiResponse<>();
        Pagination<AsistenciaDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.asistenciaRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<AsistenciaEntity> AsistenciaEntities = this.asistenciaRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(AsistenciaEntities.stream().map(AsistenciaEntity::getAsistenciaDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    public ApiResponse<AsistenciaDTO> one(String id) throws ResourceNotFoundException {
        AsistenciaEntity asistenciaEntity=this.asistenciaRepository.findByUniqueIdentifier(id).orElseThrow(()-> new ResourceNotFoundException("Asistencia no encontrado"));
        ApiResponse<AsistenciaDTO> apiResponse = new ApiResponse<>();
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(asistenciaEntity.getAsistenciaDTO());
        return apiResponse;
    }

    //Agregar Asistencia
    public ApiResponse<AsistenciaDTO> add(AsistenciaDTO AsistenciaDTO) throws ResourceNotFoundException {
        ApiResponse<AsistenciaDTO> apiResponse = new ApiResponse<>();

        //Add data Asistencia DTO
        AsistenciaDTO.setId(UUID.randomUUID().toString());
        AsistenciaDTO.setCode(Code.generateCode(Code.ASIS_CODE, this.asistenciaRepository.count() + 1, Code.ASIS_LENGTH));
        AsistenciaDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        AsistenciaDTO.setCreateAt(LocalDateTime.now());

        //change dto to entity
        AsistenciaEntity AsistenciaEntity = new AsistenciaEntity();
        AsistenciaEntity.setAsistenciaDTO(AsistenciaDTO);

        //Validar alumno
        AlumnoEntity alumnoEntity = this.alumnoRepository.findByUniqueIdentifier(AsistenciaDTO.getAlumnoDTO().getId()).orElseThrow(()-> new ResourceNotFoundException("Alumno no encontrado"));

        //Validar clase
        ClaseEntity claseEntity = this.claseRepository.findByUniqueIdentifier(AsistenciaDTO.getClaseDTO().getId()).orElseThrow(()-> new ResourceNotFoundException("Clase no encontrada"));

        AsistenciaEntity.setAlumnoEntity(alumnoEntity);
        AsistenciaEntity.setClaseEntity(claseEntity);
        apiResponse.setData(this.asistenciaRepository.save(AsistenciaEntity).getAsistenciaDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar Asistencia
    public ApiResponse<AsistenciaDTO> update(AsistenciaDTO AsistenciaDTO) throws ResourceNotFoundException {
        ApiResponse<AsistenciaDTO> apiResponse = new ApiResponse<>();

        //Validar asistencia
        AsistenciaEntity asistenciaEntity = this.asistenciaRepository.findByUniqueIdentifier(AsistenciaDTO.getId()).orElseThrow(()->new ResourceNotFoundException("Asistencia no encontrada"));

        //set data
        asistenciaEntity.setState(AsistenciaDTO.getState());

        //Validar alumno
        AlumnoEntity alumnoEntity = this.alumnoRepository.findByUniqueIdentifier(AsistenciaDTO.getAlumnoDTO().getId()).orElseThrow(()-> new ResourceNotFoundException("Alumno no encontrado"));

        //Validar clase
        ClaseEntity claseEntity = this.claseRepository.findByUniqueIdentifier(AsistenciaDTO.getClaseDTO().getId()).orElseThrow(()-> new ResourceNotFoundException("Clase no encontrada"));

        asistenciaEntity.setAlumnoEntity(alumnoEntity);
        asistenciaEntity.setClaseEntity(claseEntity);
        apiResponse.setData(this.asistenciaRepository.save(asistenciaEntity).getAsistenciaDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Borrar asistencia
    public ApiResponse<AsistenciaDTO> delete(String id) {

        ApiResponse<AsistenciaDTO> apiResponse = new ApiResponse<>();
        Optional<AsistenciaEntity> optionalAsistenciaEntity = this.asistenciaRepository.findByUniqueIdentifier(id);
        if (optionalAsistenciaEntity.isPresent()) {
            AsistenciaEntity AsistenciaEntity = optionalAsistenciaEntity.get();
            AsistenciaEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            AsistenciaEntity.setDeleteAt(LocalDateTime.now());

            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.asistenciaRepository.save(AsistenciaEntity).getAsistenciaDTO());
        } else {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ROL_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existela asistencia para poder eliminar");
        }

        return  apiResponse;
    }

    public ResponseEntity<Resource> exportAsistencia(String id_alumno, String id_periodo, String id_aniolectivo) {
        log.info("id_alumno id_periodo id_aniolectivo {} {} {}", id_alumno, id_periodo, id_aniolectivo);
        Optional<AlumnoEntity> optionalAlumnoEntity = this.alumnoRepository.findByUniqueIdentifier(id_alumno);
        Optional<AnioLectivoEntity> optionalAnioLectivoEntity = this.anioLectivoRepository.findByUniqueIdentifier(id_aniolectivo, ConstantsGeneric.CREATED_STATUS);
        Optional<GradoEntity> optionalGradoEntity = this.matriculaRepository.findGradoMatriculado(id_alumno, id_aniolectivo, ConstantsGeneric.CREATED_STATUS);
        Optional<SeccionEntity> optionalSeccionEntity = this.matriculaRepository.findSeccionMatriculado(id_alumno, id_aniolectivo, ConstantsGeneric.CREATED_STATUS);
        Optional<PeriodoEntity> optionalPeriodoEntity = this.periodoRepository.findByUniqueIdentifier(id_periodo, ConstantsGeneric.CREATED_STATUS);
        if (optionalAlumnoEntity.isPresent() && (optionalAlumnoEntity.get().getStatus().equalsIgnoreCase(ConstantsGeneric.CREATED_STATUS)) &&
                optionalAnioLectivoEntity.isPresent() && optionalAnioLectivoEntity.get().getStatus().equalsIgnoreCase(ConstantsGeneric.CREATED_STATUS) &&
                optionalGradoEntity.isPresent() && optionalGradoEntity.get().getStatus().equalsIgnoreCase(ConstantsGeneric.CREATED_STATUS) &&
                optionalSeccionEntity.isPresent() && optionalSeccionEntity.get().getStatus().equalsIgnoreCase(ConstantsGeneric.CREATED_STATUS)) {
            try {
                final AlumnoEntity alumnoEntity = optionalAlumnoEntity.get();
                final File file = ResourceUtils.getFile("classpath:reportes/asistencias_alumno.jasper"); //la ruta del reporte
                final File imgLogo = ResourceUtils.getFile("classpath:images/logoC.jpg"); //Ruta de la imagen
                final JasperReport report = (JasperReport) JRLoader.loadObject(file);
                //Se consultan los datos para el reporte de asistencias DTO
                Optional<List<CursoEntity>> optionalCursoEntities = this.matriculaRepository.findCursosMatriculados(id_alumno, id_aniolectivo, ConstantsGeneric.CREATED_STATUS);
                Optional<List<AsistenciaEntity>> optionalAsistenciaEntities = this.asistenciaRepository.findAsistencias(id_alumno, id_periodo, id_aniolectivo, ConstantsGeneric.CREATED_STATUS);
                Optional<List<ClaseEntity>> optionalClaseEntities = this.asistenciaRepository.findClasesDeAsistencias(id_alumno, id_periodo, id_aniolectivo, ConstantsGeneric.CREATED_STATUS);
                //Se agregan los datos para Reporte de Asistencias
                List<ReporteAsistenciaAlumnoDTO> reporteAsistenciaAlumnoDTOS= new ArrayList<>();
                for (int i = 0; i < optionalCursoEntities.get().size(); i++) {
                    ReporteAsistenciaAlumnoDTO reporteAsistenciaAlumnoDTO = new ReporteAsistenciaAlumnoDTO();
                    reporteAsistenciaAlumnoDTO.setCursoDTO(optionalCursoEntities.get().get(i).getCursoDTO());
                    reporteAsistenciaAlumnoDTO.setAsistenciaDTO(optionalAsistenciaEntities.get().get(i).getAsistenciaDTO());
                    reporteAsistenciaAlumnoDTO.setClaseDTO(optionalClaseEntities.get().get(i).getClaseDTO());
                    reporteAsistenciaAlumnoDTOS.add(reporteAsistenciaAlumnoDTO);
                }
                //Se llenan los parámetros del reporte
                final HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("logoEmpresa", new FileInputStream(imgLogo));
                parameters.put("nombreAlumno", alumnoEntity.getNombresCompletosAl());
                parameters.put("periodo", optionalPeriodoEntity.get().getName());
                parameters.put("grado", optionalGradoEntity.get().getName().toString());
                parameters.put("seccion", optionalSeccionEntity.get().getName().toString());
                parameters.put("año", optionalAnioLectivoEntity.get().getName());
                parameters.put("dsLA", new JRBeanArrayDataSource(reporteAsistenciaAlumnoDTOS.toArray()));

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
                return ResponseEntity.ok().contentLength(reporte.length)
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
    public ResponseEntity<Resource> exportAsistAula(String id_curso, String id_aula, String id_aniolectivo) {
        log.info("id_curso id_aula id_aniolectivo {} {} {}", id_curso, id_aula, id_aniolectivo);
        try {
            AnioLectivoEntity anioLectivoEntity = this.anioLectivoRepository.findByUniqueIdentifier(id_aniolectivo, ConstantsGeneric.CREATED_STATUS).orElseThrow(()->new ResourceNotFoundException("Año lectivo no existente"));
            AulaEntity aulaEntity = this.aulaRepository.findByUniqueIdentifier(id_aula, ConstantsGeneric.CREATED_STATUS).orElseThrow(()->new ResourceNotFoundException("Aula no existente"));
            CursoEntity cursoEntity = this.cursoRepository.findByUniqueIdentifier(id_curso, ConstantsGeneric.CREATED_STATUS).orElseThrow(()->new ResourceNotFoundException("Curso no encontrado"));
            DocenteEntity docenteEntity = this.docenteRepository.findAulaAnio(id_aula, id_curso, id_aniolectivo, ConstantsGeneric.CREATED_STATUS).orElse(new DocenteEntity());

            final File file = ResourceUtils.getFile("classpath:reportes/asistencias_aula.jasper"); //la ruta del reporte
            final File imgLogo = ResourceUtils.getFile("classpath:images/logoC.jpg"); //Ruta de la imagen
            final JasperReport report = (JasperReport) JRLoader.loadObject(file);
            //Se consultan los datos para el reporte de asistencias DTO
            List<AlumnoEntity> alumnoEntities = this.aulaRepository.findAlumnosxAula(id_aula, id_aniolectivo, ConstantsGeneric.CREATED_STATUS).orElse(new ArrayList<>());
            //Se agregan los datos para Reporte de Asistencias
            List<ReporteAsistenciaAulaDTO> reporteAsistenciaAulaDTOS= new ArrayList<>();
            int tasistencias = 0;
            int tfaltas = 0;
            int tjustificadas = 0;

            for (AlumnoEntity alumnoEntity : alumnoEntities) {
                ReporteAsistenciaAulaDTO reporteAsistenciaAulaDTO = new ReporteAsistenciaAulaDTO();
                reporteAsistenciaAulaDTO.setAlumno(alumnoEntity.getNombresCompletosAl());
                reporteAsistenciaAulaDTO.setAsistencias(this.asistenciaRepository.countAsistenciasAulaAño(alumnoEntity.getUniqueIdentifier(), "PRESENTE", id_curso, id_aula, id_aniolectivo, ConstantsGeneric.CREATED_STATUS));
                tasistencias += reporteAsistenciaAulaDTO.getAsistencias();
                reporteAsistenciaAulaDTO.setFaltas(this.asistenciaRepository.countAsistenciasAulaAño(alumnoEntity.getUniqueIdentifier(), "AUSENTE", id_curso, id_aula, id_aniolectivo, ConstantsGeneric.CREATED_STATUS));
                tfaltas += reporteAsistenciaAulaDTO.getFaltas();
                reporteAsistenciaAulaDTO.setJustificadas(this.asistenciaRepository.countAsistenciasAulaAño(alumnoEntity.getUniqueIdentifier(), "JUSTIFICADA", id_curso, id_aula, id_aniolectivo, ConstantsGeneric.CREATED_STATUS));
                tjustificadas += reporteAsistenciaAulaDTO.getJustificadas();
                reporteAsistenciaAulaDTOS.add(reporteAsistenciaAulaDTO);
            }

            //Se llenan los parámetros del reporte
            final HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("logoEmpresa", new FileInputStream(imgLogo));
            parameters.put("curso", cursoEntity.getName());
            parameters.put("grado", aulaEntity.getGradoEntity().getName().toString());
            parameters.put("seccion", aulaEntity.getSeccionEntity().getName().toString());
            parameters.put("docente", docenteEntity.getUsuarioEntity().getNameCompleto());
            parameters.put("año", anioLectivoEntity.getName());
            parameters.put("tasistencias", tasistencias);
            parameters.put("tfaltas", tfaltas);
            parameters.put("tjustificadas", tjustificadas);
            parameters.put("dsAsistAula", new JRBeanArrayDataSource(reporteAsistenciaAulaDTOS.toArray()));
            //Se imprime el reporte
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());

            byte [] reporte = JasperExportManager.exportReportToPdf(jasperPrint);
            String sdf = (new SimpleDateFormat("dd/MM/yyyy")).format(new Date());
            StringBuilder stringBuilder = new StringBuilder().append("ResumenAsistenciaPDF:");
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                        .filename(stringBuilder
                                .append(aulaEntity.getCode())
                                .append("generateDate:").append(sdf)
                                .append(".pdf").toString())
                        .build();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentDisposition(contentDisposition);
            log.info("1");

            return ResponseEntity.ok().contentLength(reporte.length)
                        .contentType(MediaType.APPLICATION_PDF)
                        .headers(headers).body(new ByteArrayResource(reporte));
            } catch (Exception e) {
                new Exception("Ha ocurrido un error");
        }
        return null;
    }
    public ResponseEntity<Resource> exportAsistClase(String id_clase) {
        log.info("id_clase {}", id_clase);
        try {
            ClaseEntity claseEntity = this.claseRepository.findByUniqueIdentifier(id_clase).orElseThrow(()-> new ResourceNotFoundException("No existe la clase"));
            List<AsistenciaEntity> asistenciaEntities = this.asistenciaRepository.findByClase(id_clase, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new ResourceNotFoundException("No existen asistencias"));
            final File file = ResourceUtils.getFile("classpath:reportes/asistencias_clase.jasper"); //la ruta del reporte
            final File imgLogo = ResourceUtils.getFile("classpath:images/logoC.jpg"); //Ruta de la imagen
            final JasperReport report = (JasperReport) JRLoader.loadObject(file);
            //Se consultan los datos para el reporte de asistencias DTO
            //Se agregan los datos para Reporte de Asistencias
            List<ReporteAsistenciaClaseDTO> reporteAsistenciaClaseDTOS= new ArrayList<>();
            int tasistencias = 0;
            int tfaltas = 0;
            int tjustificadas = 0;
            log.info("0");
            System.out.println("númerode filas: "+ asistenciaEntities.size());
            for (AsistenciaEntity asistenciaEntity : asistenciaEntities) {
                ReporteAsistenciaClaseDTO reporteAsistenciaClaseDTO = new ReporteAsistenciaClaseDTO();
                reporteAsistenciaClaseDTO.setAlumno(asistenciaEntity.getAlumnoEntity().getNombresCompletosAl());
                System.out.println(reporteAsistenciaClaseDTO.getAlumno());
                reporteAsistenciaClaseDTO.setDocumento(asistenciaEntity.getAlumnoEntity().getUsuarioEntity().getNumdoc());
                System.out.println(reporteAsistenciaClaseDTO.getDocumento());
                reporteAsistenciaClaseDTO.setTelefono(asistenciaEntity.getAlumnoEntity().getApoderadoEntity().getTel());
                System.out.println(reporteAsistenciaClaseDTO.getTelefono());

                reporteAsistenciaClaseDTO.setEstado(asistenciaEntity.getState());
                System.out.println(reporteAsistenciaClaseDTO.getEstado());

                if(asistenciaEntity.getState().equalsIgnoreCase("PRESENTE"))
                    tasistencias+= 1;
                if(asistenciaEntity.getState().equalsIgnoreCase("AUSENTE"))
                    tfaltas += 1;
                if(asistenciaEntity.getState().equalsIgnoreCase("JUSTIFICADA"))
                    tjustificadas += 1;
                reporteAsistenciaClaseDTOS.add(reporteAsistenciaClaseDTO);
            }
            log.info("1");

            //Se llenan los parámetros del reporte
            final HashMap<String, Object> parameters = new HashMap<>();
            log.info("2");

            parameters.put("logoEmpresa", new FileInputStream(imgLogo));
            parameters.put("curso", claseEntity.getDocentexCursoEntity().getCursoEntity().getName());
            parameters.put("grado", claseEntity.getDocentexCursoEntity().getAulaEntity().getGradoEntity().getName().toString());
            parameters.put("seccion", claseEntity.getDocentexCursoEntity().getAulaEntity().getSeccionEntity().getName().toString());
            parameters.put("docente", claseEntity.getDocentexCursoEntity().getDocenteEntity().getUsuarioEntity().getNameCompleto());
            parameters.put("fecha-clase", claseEntity.getDate());
            parameters.put("año", claseEntity.getPeriodoEntity().getAnio_lectivoEntity().getName());
            parameters.put("tasistencias", tasistencias);
            parameters.put("tfaltas", tfaltas);
            parameters.put("tjustificadas", tjustificadas);
            parameters.put("dsAsistClase", new JRBeanArrayDataSource(reporteAsistenciaClaseDTOS.toArray()));
            for (int i = 0; i < parameters.size(); i++) {
                System.out.println(parameters.get(i));
            }
            //Se imprime el reporte
            log.info("3");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
            log.info("4");

            byte [] reporte = JasperExportManager.exportReportToPdf(jasperPrint);
            String sdf = (new SimpleDateFormat("dd/MM/yyyy")).format(new Date());
            StringBuilder stringBuilder = new StringBuilder().append("ResumenAsistenciaPDF:");
            log.info("5");

            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                        .filename(stringBuilder
                                .append(claseEntity.getCode())
                                .append("generateDate:").append(sdf)
                                .append(".pdf").toString())
                        .build();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentDisposition(contentDisposition);
            return ResponseEntity.ok().contentLength(reporte.length)
                        .contentType(MediaType.APPLICATION_PDF)
                        .headers(headers).body(new ByteArrayResource(reporte));
            } catch (Exception e) {
                new Exception("Ha ocurrido un error");
        }
        return null;
    }
}
