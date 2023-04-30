package com.rca.RCA.service;

import com.rca.RCA.entity.*;
import com.rca.RCA.entity.AsistenciaEntity;
import com.rca.RCA.repository.*;
import com.rca.RCA.type.*;
import com.rca.RCA.type.AsistenciaDTO;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
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
    private AnioLectivoRepository anioLectivoRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;
    @Autowired
    private PeriodoRepository periodoRepository;

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

    //Agregar Asistencia
    public ApiResponse<AsistenciaDTO> add(AsistenciaDTO AsistenciaDTO) {
        ApiResponse<AsistenciaDTO> apiResponse = new ApiResponse<>();
        System.out.println(AsistenciaDTO.toString());
        AsistenciaDTO.setId(UUID.randomUUID().toString());
        AsistenciaDTO.setCode(Code.generateCode(Code.ASIS_CODE, this.asistenciaRepository.count() + 1, Code.ASIS_LENGTH));
        AsistenciaDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        AsistenciaDTO.setCreateAt(LocalDateTime.now());
        System.out.println(AsistenciaDTO.toString());

        //change dto to entity
        AsistenciaEntity AsistenciaEntity = new AsistenciaEntity();
        AsistenciaEntity.setAsistenciaDTO(AsistenciaDTO);

        //set usuario
        Optional<AlumnoEntity> optionalAlumnoEntity = this.alumnoRepository.findByUniqueIdentifier(AsistenciaDTO.getAlumnoDTO().getId());
        if (optionalAlumnoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("alumno_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el alumno asociado a la Asistencia no existe");
            return apiResponse;
        }

        //set clase
        Optional<ClaseEntity> optionalClaseEntity = this.claseRepository.findByUniqueIdentifier(AsistenciaDTO.getClaseDTO().getId());
        if (optionalClaseEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("CLASE_NOT_EXISTS");
            apiResponse.setMessage("No se registró, la clase asociada a la Asistencia no existe");
            return apiResponse;
        }

        AsistenciaEntity.setAlumnoEntity(optionalAlumnoEntity.get());
        AsistenciaEntity.setClaseEntity(optionalClaseEntity.get());
        apiResponse.setData(this.asistenciaRepository.save(AsistenciaEntity).getAsistenciaDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar Asistencia
    public ApiResponse<AsistenciaDTO> update(AsistenciaDTO AsistenciaDTO) {
        ApiResponse<AsistenciaDTO> apiResponse = new ApiResponse<>();
        System.out.println(AsistenciaDTO.toString());

        Optional<AsistenciaEntity> optionalAsistenciaEntity = this.asistenciaRepository.findByUniqueIdentifier(AsistenciaDTO.getId());
        if (optionalAsistenciaEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Asistencia_NOT_EXISTS");
            apiResponse.setMessage("No se encontro la Asistencia");
            return apiResponse;
        }

        //change dto to entity
        AsistenciaEntity AsistenciaEntity = optionalAsistenciaEntity.get();
        AsistenciaEntity.setState(AsistenciaDTO.getState());

        //set alumno
        Optional<AlumnoEntity> optionalAlumnoEntity = this.alumnoRepository.findByUniqueIdentifier(AsistenciaDTO.getAlumnoDTO().getId());
        if (optionalAlumnoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Alumno_NOT_EXISTS");
            apiResponse.setMessage("No se registro, el alumno asociada a la Asistencia no existe");
            return apiResponse;
        }

        //set Clase
        Optional<ClaseEntity> optionalClaseEntity = this.claseRepository.findByUniqueIdentifier(AsistenciaDTO.getClaseDTO().getId());
        if (optionalClaseEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("CLASE_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el apoderado asociado al Asistencia no existe");
            return apiResponse;
        }

        AsistenciaEntity.setAlumnoEntity(optionalAlumnoEntity.get());
        AsistenciaEntity.setClaseEntity(optionalClaseEntity.get());
        apiResponse.setData(this.asistenciaRepository.save(AsistenciaEntity).getAsistenciaDTO());
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
}
