package com.rca.RCA.service;

import com.rca.RCA.entity.*;
import com.rca.RCA.repository.*;
import com.rca.RCA.type.*;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
public class EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;
    private AlumnoRepository alumnoRepository;
    private DocentexCursoRepository docentexCursoRepository;
    private PeriodoRepository periodoRepository;

    private AnioLectivoRepository anioLectivoRepository;

    private CursoRepository cursoRepository;

    private AulaRepository aulaRepository;

    public EvaluacionService(EvaluacionRepository evaluacionRepository, AlumnoRepository alumnoRepository,
                             DocentexCursoRepository docentexCursoRepository, PeriodoRepository periodoRepository,
                             AnioLectivoRepository anioLectivoRepository, CursoRepository cursoRepository){
        this.evaluacionRepository = evaluacionRepository;
        this.alumnoRepository = alumnoRepository;
        this.docentexCursoRepository = docentexCursoRepository;
        this.periodoRepository = periodoRepository;
        this.anioLectivoRepository = anioLectivoRepository;
        this.cursoRepository = cursoRepository;
    }

    public ApiResponse<String> generatedEvaluations(String id_periodo, String filter) throws ResourceNotFoundException {
        ApiResponse<String> apiResponse = new ApiResponse<>();

        PeriodoEntity periodoEntity = this.periodoRepository.findByUniqueIdentifier(id_periodo, ConstantsGeneric.CREATED_STATUS).orElseThrow(()->new ResourceNotFoundException("Periodo no encontrado"));

        List<AulaEntity> aulaEntities = this.aulaRepository.findAulaxAnio(ConstantsGeneric.CREATED_STATUS, periodoEntity.getAnio_lectivoEntity().getUniqueIdentifier(), filter).orElseThrow(()-> new ResourceNotFoundException("Aulas no encontradas"));

        for (int i = 0; aulaEntities.size() > i; i++) {
            List<CursoEntity> cursoEntities = this.cursoRepository.findCursoByAulaAnio(ConstantsGeneric.CREATED_STATUS, aulaEntities.get(i).getUniqueIdentifier(), periodoEntity.getAnio_lectivoEntity().getUniqueIdentifier()).orElseThrow(()-> new ResourceNotFoundException("Cursos no encontrados"));
            List<AlumnoEntity> alumnoEntities = this.alumnoRepository.findEntities(ConstantsGeneric.CREATED_STATUS, periodoEntity.getAnio_lectivoEntity().getUniqueIdentifier(), aulaEntities.get(i).getUniqueIdentifier(), "").orElseThrow(()->new ResourceNotFoundException("Alumnos no encontrados"));

        }
        return apiResponse;
    }

    //Obtener Evaluaciones
    public ApiResponse<Pagination<EvaluacionDTO>> getList(String filter, int page, int size) {
        log.info("filter page size {} {} {}", filter, page, size);
        ApiResponse<Pagination<EvaluacionDTO>> apiResponse = new ApiResponse<>();
        Pagination<EvaluacionDTO> pagination = new Pagination<>();
        pagination.setCountFilter(this.evaluacionRepository.findCountEntities(ConstantsGeneric.CREATED_STATUS, filter));
        if (pagination.getCountFilter() > 0) {
            Pageable pageable = PageRequest.of(page, size);
            List<EvaluacionEntity> EvaluacionEntities = this.evaluacionRepository.findEntities(ConstantsGeneric.CREATED_STATUS, filter, pageable).orElse(new ArrayList<>());
            pagination.setList(EvaluacionEntities.stream().map(EvaluacionEntity::getEvaluacionDTO).collect(Collectors.toList()));
        }
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        apiResponse.setData(pagination);
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    public ApiResponse<EvaluacionDTO> one(String id) throws ResourceNotFoundException {
        EvaluacionEntity evaluacionEntity=this.evaluacionRepository.findByUniqueIdentifier(id).orElseThrow(()-> new ResourceNotFoundException("Evaluacion no encontrado"));
        ApiResponse<EvaluacionDTO> apiResponse = new ApiResponse<>();
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        apiResponse.setData(evaluacionEntity.getEvaluacionDTO());
        return apiResponse;
    }

    //Agreagar Evaluacion
    public ApiResponse<EvaluacionDTO> add(EvaluacionDTO EvaluacionDTO) {
        ApiResponse<EvaluacionDTO> apiResponse = new ApiResponse<>();
        System.out.println(EvaluacionDTO.toString());
        EvaluacionDTO.setId(UUID.randomUUID().toString());
        EvaluacionDTO.setCode(Code.generateCode(Code.EVA_CODE, this.evaluacionRepository.count() + 1, Code.EVA_LENGTH));
        EvaluacionDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        EvaluacionDTO.setCreateAt(LocalDateTime.now());
        System.out.println(EvaluacionDTO.toString());

        //validamos
        Optional<EvaluacionEntity> optionalEvaluacionEntity = this.evaluacionRepository.findByUniqueIdentifier(EvaluacionDTO.getId());
        if (optionalEvaluacionEntity.isPresent()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Evaluacion_EXISTS");
            apiResponse.setMessage("No se registró, la Evaluacion existe");
            return apiResponse;
        }
        //change dto to entity
        EvaluacionEntity EvaluacionEntity = new EvaluacionEntity();
        EvaluacionEntity.setEvaluacionDTO(EvaluacionDTO);

        //set Alumno
        Optional<AlumnoEntity> optionalAlumnoEntity = this.alumnoRepository.findByUniqueIdentifier(EvaluacionDTO.getAlumnoDTO().getId());
        if (optionalAlumnoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ALUMNO_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el alumno asociado a la evaluacion no existe");
            return apiResponse;
        }

        //set Periodo
        Optional<PeriodoEntity> optionalPeriodoEntity = this.periodoRepository.findByUniqueIdentifier(EvaluacionDTO.getPeriodoDTO().getId(), ConstantsGeneric.CREATED_STATUS);
        if (optionalAlumnoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("PERIODO_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el periodo asociado a la evaluacion no existe");
            return apiResponse;
        }

        //set docentexCurso
        Optional<DocentexCursoEntity> optionalDocentexCursoEntity = this.docentexCursoRepository.findByUniqueIdentifier(EvaluacionDTO.getDocentexCursoDTO().getId());
        if (optionalDocentexCursoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("docentexCurso_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el docentexCurso asociado a la evaluacion no existe");
            return apiResponse;
        }

        EvaluacionEntity.setAlumnoEntity(optionalAlumnoEntity.get());
        EvaluacionEntity.setPeriodoEntity(optionalPeriodoEntity.get());
        EvaluacionEntity.setDocentexCursoEntity(optionalDocentexCursoEntity.get());
        apiResponse.setData(this.evaluacionRepository.save(EvaluacionEntity).getEvaluacionDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    //Modificar Evaluacion
    public ApiResponse<EvaluacionDTO> update(EvaluacionDTO EvaluacionDTO) {
        ApiResponse<EvaluacionDTO> apiResponse = new ApiResponse<>();
        System.out.println(EvaluacionDTO.toString());

        Optional<EvaluacionEntity> optionalEvaluacionEntity = this.evaluacionRepository.findByUniqueIdentifier(EvaluacionDTO.getId());
        if (optionalEvaluacionEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("evaluacion_NOT_EXISTS");
            apiResponse.setMessage("No se encontro la evaluacion");
            return apiResponse;
        }

        //change dto to entity
        EvaluacionEntity EvaluacionEntity = optionalEvaluacionEntity.get();
        EvaluacionEntity.setNote(EvaluacionDTO.getNote());
        EvaluacionEntity.setDate(EvaluacionDTO.getDate());

        //set Alumno
        Optional<AlumnoEntity> optionalAlumnoEntity = this.alumnoRepository.findByUniqueIdentifier(EvaluacionDTO.getAlumnoDTO().getId());
        if (optionalAlumnoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ALUMNO_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el alumno asociado a la evaluacion no existe");
            return apiResponse;
        }

        //set Periodo
        Optional<PeriodoEntity> optionalPeriodoEntity = this.periodoRepository.findByUniqueIdentifier(EvaluacionDTO.getPeriodoDTO().getId(), ConstantsGeneric.CREATED_STATUS);
        if (optionalAlumnoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("PERIODO_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el periodo asociado a la evaluacion no existe");
            return apiResponse;
        }

        //set docentexCurso
        Optional<DocentexCursoEntity> optionalDocentexCursoEntity = this.docentexCursoRepository.findByUniqueIdentifier(EvaluacionDTO.getDocentexCursoDTO().getId());
        if (optionalDocentexCursoEntity.isEmpty()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("docentexCurso_NOT_EXISTS");
            apiResponse.setMessage("No se registró, el docentexCurso asociado a la evaluacion no existe");
            return apiResponse;
        }

        EvaluacionEntity.setAlumnoEntity(optionalAlumnoEntity.get());
        EvaluacionEntity.setPeriodoEntity(optionalPeriodoEntity.get());
        EvaluacionEntity.setDocentexCursoEntity(optionalDocentexCursoEntity.get());
        apiResponse.setData(this.evaluacionRepository.save(EvaluacionEntity).getEvaluacionDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");

        return apiResponse;
    }

    //Borrar Evaluacion
    public ApiResponse<EvaluacionDTO> delete(String id) {
        ApiResponse<EvaluacionDTO> apiResponse = new ApiResponse<>();
        Optional<EvaluacionEntity> optionalEvaluacionEntity = this.evaluacionRepository.findByUniqueIdentifier(id);
        if (optionalEvaluacionEntity.isPresent()) {
            EvaluacionEntity EvaluacionEntity = optionalEvaluacionEntity.get();
            EvaluacionEntity.setStatus(ConstantsGeneric.DELETED_STATUS);
            EvaluacionEntity.setDeleteAt(LocalDateTime.now());

            apiResponse.setSuccessful(true);
            apiResponse.setMessage("ok");
            apiResponse.setData(this.evaluacionRepository.save(EvaluacionEntity).getEvaluacionDTO());
        } else {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("ROL_DOES_NOT_EXISTS");
            apiResponse.setMessage("No existe la evaluacion para poder eliminar");
        }

        return apiResponse;
    }

    public ResponseEntity<Resource> exportBoletaNotas(String periodo, String anio, String alumno) {
        Optional<AlumnoEntity> optionalAlumnoEntity = this.alumnoRepository.findByUniqueIdentifier(alumno);
        Optional<PeriodoEntity> optionalPeriodoEntity = this.periodoRepository.findByUniqueIdentifier(periodo, ConstantsGeneric.CREATED_STATUS);
        Optional<AnioLectivoEntity> optionalAnioLectivoEntity = this.anioLectivoRepository.findByUniqueIdentifier(anio, ConstantsGeneric.CREATED_STATUS);
        if (optionalAlumnoEntity.isPresent() && optionalPeriodoEntity.isPresent()){

            List<Object[]> tuples = this.evaluacionRepository.findByAlumnoPeriodoAnio(alumno, anio, periodo);
            List<CursoEvaluacionDTO> cursos = new ArrayList<>();

            for (Object[] tuple : tuples) {
                String name = (String) tuple[0];
                String note = (String) tuple[1];
                CursoEvaluacionDTO curso = new CursoEvaluacionDTO(name, note);
                cursos.add(curso);
            }

            try{
                final PeriodoEntity periodoEntity = optionalPeriodoEntity.get();
                final AlumnoEntity alumnoEntity = optionalAlumnoEntity.get();
                final AnioLectivoEntity anioLectivoEntity = optionalAnioLectivoEntity.get();
                final File file = ResourceUtils.getFile("classpath:reportes/cursosEvaluacion.jasper");
                final File imgLogo = ResourceUtils.getFile("classpath:images/logo.png");
                final JasperReport report = (JasperReport) JRLoader.loadObject(file);



                final HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("logoEmpresa", new FileInputStream(imgLogo));
                parameters.put("apellidoPaterno", alumnoEntity.getUsuarioEntity().getPa_surname());
                parameters.put("apellidoMaterno", alumnoEntity.getUsuarioEntity().getMa_surname());
                parameters.put("nombres", alumnoEntity.getUsuarioEntity().getName());
                parameters.put("Periodo", periodoEntity.getName());
                parameters.put("anio", anioLectivoEntity.getAnioLectivoDTO().getName());
                parameters.put("gradoSeccion", "3");
                parameters.put("dsCursos",  new JRBeanCollectionDataSource(cursos));

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

    public ResponseEntity<Resource> exportNotas(String curso, String periodo, String anio) {
        Optional<PeriodoEntity> optionalPeriodoEntity = this.periodoRepository.findByUniqueIdentifier(periodo, ConstantsGeneric.CREATED_STATUS);
        Optional<AnioLectivoEntity> optionalAnioLectivoEntity = this.anioLectivoRepository.findByUniqueIdentifier(anio, ConstantsGeneric.CREATED_STATUS);
        Optional<CursoEntity> optionalCursoEntity = this.cursoRepository.findByUniqueIdentifier(curso, ConstantsGeneric.CREATED_STATUS);

        if (optionalCursoEntity.isPresent() && optionalPeriodoEntity.isPresent() && optionalAnioLectivoEntity.isPresent()){

            List<Object[]> tuples = this.evaluacionRepository.findByCursoPeriodoAnio(curso, anio, periodo);
            List<CursoNotasDTO> notas = new ArrayList<>();

            for (Object[] tuple : tuples) {
                String estudiante = (String) tuple[0];
                String note = (String) tuple[1];
                CursoNotasDTO nota = new CursoNotasDTO(estudiante, note);
                notas.add(nota);
            }

            try{
                final PeriodoEntity periodoEntity = optionalPeriodoEntity.get();
                final CursoEntity cursoEntity = optionalCursoEntity.get();
                final AnioLectivoEntity anioLectivoEntity = optionalAnioLectivoEntity.get();
                final File file = ResourceUtils.getFile("classpath:reportes/notasCurso.jasper");
                final File imgLogo = ResourceUtils.getFile("classpath:images/logo.png");
                final JasperReport report = (JasperReport) JRLoader.loadObject(file);



                final HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("logoEmpresa", new FileInputStream(imgLogo));
                parameters.put("curso", cursoEntity.getName());
                parameters.put("Periodo", periodoEntity.getName());
                parameters.put("anio", anioLectivoEntity.getAnioLectivoDTO().getName());
                parameters.put("dsAlumnos",  new JRBeanCollectionDataSource(notas));

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
