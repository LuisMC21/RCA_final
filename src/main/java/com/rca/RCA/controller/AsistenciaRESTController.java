package com.rca.RCA.controller;

import com.rca.RCA.service.AsistenciaService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.GradoDTO;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.AsistenciaDTO;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/asistencia")
public class AsistenciaRESTController {

    @Autowired
    private AsistenciaService asistenciaService;

    public AsistenciaRESTController(){

    }

    @GetMapping
    public ApiResponse<Pagination<AsistenciaDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return this.asistenciaService.getList(filter, page, size);
    }

    @GetMapping("/apac")
    public ApiResponse<Pagination<AsistenciaDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String periodo,
            @RequestParam String aula,
            @RequestParam String curso
    ) {
        return this.asistenciaService.getList(filter, page, size, periodo, aula, curso);
    }

    @GetMapping("/asfc")
    public ApiResponse<Pagination<AsistenciaDTO>> listWithAlumno(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String periodo,
            @RequestParam String alumno,
            @RequestParam String curso
    ) {
        return this.asistenciaService.getListWithAlumno(filter, page, size, periodo, alumno, curso);
    }
    @GetMapping("/asbyclase")
    public ApiResponse<Pagination<AsistenciaDTO>> getListByClase(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String id_clase
    ) {
        return this.asistenciaService.getListByClase(filter, id_clase, page, size);
    }

    @GetMapping("/asbypacc")
    public ApiResponse<Pagination<AsistenciaDTO>> getListBypacc(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String periodo,
            @RequestParam String aula,
            @RequestParam String curso,
            @RequestParam String clase
    ) {
        return this.asistenciaService.getListByPeriodoAulaCursoClase(filter, periodo, aula, curso, clase, page, size);
    }

    @PostMapping
    public ApiResponse<AsistenciaDTO> add(@RequestBody @Valid AsistenciaDTO AsistenciaDTO) throws ResourceNotFoundException {
        return this.asistenciaService.add(AsistenciaDTO);
    }

    @GetMapping("{id}")
    public ApiResponse<AsistenciaDTO> one(@PathVariable String id) throws ResourceNotFoundException {
        return this.asistenciaService.one(id);
    }

    @PutMapping
    public ApiResponse<AsistenciaDTO> update(@RequestBody AsistenciaDTO asistenciaDTO) throws ResourceNotFoundException {
        return this.asistenciaService.update(asistenciaDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<AsistenciaDTO> delete(@PathVariable String id) {
        return this.asistenciaService.delete(id);
    }
    @GetMapping("exportAsistencia")
    public ResponseEntity<Resource> exportAsistencia(@RequestParam String id_alumno,
                                                    @RequestParam String id_periodo,
                                                     @RequestParam String id_aniolectivo) throws ResourceNotFoundException {
        return this.asistenciaService.exportAsistencia(id_alumno, id_periodo,id_aniolectivo);
    }
    @GetMapping("exportAsistAula")
    public ResponseEntity<Resource> exportAsistAula(@RequestParam String id_curso,
                                                    @RequestParam String id_aula,
                                                     @RequestParam String id_aniolectivo) throws ResourceNotFoundException {
        return this.asistenciaService.exportAsistAula(id_curso, id_aula,id_aniolectivo);
    }
    @GetMapping("exportAsistClase")
    public ResponseEntity<Resource> exportAsistClase(@RequestParam String id_clase) throws ResourceNotFoundException {
        return this.asistenciaService.exportAsistClase(id_clase);
    }


}
