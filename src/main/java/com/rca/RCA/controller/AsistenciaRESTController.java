package com.rca.RCA.controller;

import com.rca.RCA.service.AsistenciaService;
import com.rca.RCA.type.ApiResponse;
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
@RequestMapping("asistencia")
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

    @PostMapping
    public ApiResponse<AsistenciaDTO> add(@RequestBody @Valid AsistenciaDTO AsistenciaDTO) throws ResourceNotFoundException {
        return this.asistenciaService.add(AsistenciaDTO);
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
                                                     @RequestParam String id_aniolectivo){
        return this.asistenciaService.exportAsistencia(id_alumno, id_periodo,id_aniolectivo);
    }
}
