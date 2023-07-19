package com.rca.RCA.controller;

import com.rca.RCA.service.MatriculaService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.MatriculaDTO;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/matricula")
public class MatriculaRESTController {
    @Autowired
    MatriculaService matriculaService;

    public MatriculaRESTController(){
    }

    @GetMapping
    public ApiResponse<Pagination<MatriculaDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return this.matriculaService.getList(filter, page, size);
    }
    @GetMapping("{id}")
    public ApiResponse<MatriculaDTO> one(@PathVariable String id) throws ResourceNotFoundException {
        return this.matriculaService.one(id);
    }

    @PostMapping
    public ApiResponse<MatriculaDTO> add(@RequestBody MatriculaDTO matriculaDTO) throws ResourceNotFoundException, AttributeException {
        return this.matriculaService.add(matriculaDTO);
    }

    @PutMapping
    public ApiResponse<MatriculaDTO> update(@RequestBody MatriculaDTO matriculaDTO) throws ResourceNotFoundException, AttributeException {
        return this.matriculaService.update(matriculaDTO);
    }
    @DeleteMapping("{id}")
    public ApiResponse<MatriculaDTO> delete(@PathVariable String id) throws ResourceNotFoundException {
        return this.matriculaService.delete(id);
    }

    //Exportar reporte de alumnos matriuclados por aula y periodo
    @GetMapping("alumnosAula")
    public ResponseEntity<Resource> exportListAlumnos(@RequestParam String uniqueIdentifierAula, @RequestParam String uniqueIdentifierAnio) throws ResourceNotFoundException {
        return this.matriculaService.exportListaAlumnos(uniqueIdentifierAula, uniqueIdentifierAnio);
    }

    //Exportar reporte de matrícula por alumno y año lectivo
    @GetMapping("exportMatricula")
    public ResponseEntity<Resource> exportMatricula(@RequestParam String id_alumno,
                                                    @RequestParam String id_aniolectivo) throws ResourceNotFoundException {
        return this.matriculaService.exportMatricula(id_alumno, id_aniolectivo);
    }
}
