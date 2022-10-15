package com.rca.RCA.controller;

import com.rca.RCA.repository.AsistenciaRepository;
import com.rca.RCA.service.AsistenciaService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.AsistenciaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("asistencia")
public class AsistenciaRESTController {

    @Autowired
    private AsistenciaService asistenciaService;

    public AsistenciaRESTController(){

    }

    @GetMapping
    public Pagination<AsistenciaDTO> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return this.asistenciaService.getList(filter, page, size);
    }

    @PostMapping
    public ApiResponse<AsistenciaDTO> add(@RequestBody AsistenciaDTO AsistenciaDTO) {
        return this.asistenciaService.add(AsistenciaDTO);
    }

    @PutMapping
    public void update(@RequestBody AsistenciaDTO AsistenciaDTO) {
        this.asistenciaService.update(AsistenciaDTO);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        this.asistenciaService.delete(id);
    }
}
