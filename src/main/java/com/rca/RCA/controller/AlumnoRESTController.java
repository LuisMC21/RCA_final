package com.rca.RCA.controller;

import com.rca.RCA.service.AlumnoService;
import com.rca.RCA.type.*;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import javax.management.AttributeNotFoundException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/alumno")
public class AlumnoRESTController {

    @Autowired
    private AlumnoService alumnoService;

    public AlumnoRESTController(){

    }

    @GetMapping
    public ApiResponse<Pagination<AlumnoDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return this.alumnoService.getList(filter, page, size);
    }

    @GetMapping("auc")
    public ApiResponse<Pagination<AlumnoDTO>> list(
            @RequestParam String filter,
            @RequestParam String anio,
            @RequestParam String aula,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return this.alumnoService.getListAlumnosAnioAulaCurso(filter, anio, aula,  page, size);
    }

    @GetMapping("{id}")
    public ApiResponse<AlumnoDTO> one(@PathVariable String id) throws ResourceNotFoundException {
        return this.alumnoService.one(id);
    }

    @PostMapping
    public ApiResponse<AlumnoDTO> add(@RequestBody @Valid  AlumnoDTO AlumnoDTO) throws ResourceNotFoundException, AttributeException {
        return this.alumnoService.add(AlumnoDTO);
    }

    @PutMapping
    public ApiResponse<AlumnoDTO> update(@RequestBody AlumnoDTO alumnoDTO) throws ResourceNotFoundException, AttributeException {
        return this.alumnoService.update(alumnoDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<AlumnoDTO> delete(@PathVariable String id) throws ResourceNotFoundException {
         return this.alumnoService.delete(id);
    }

    @GetMapping("datosPersonales")
    public ResponseEntity<Resource> datosPersonales(@RequestParam String uniqueIdentifier){

        ResponseEntity<Resource> response = this.alumnoService.datosPersonales(uniqueIdentifier);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "datosPersonales.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(response.getBody());
    }
}