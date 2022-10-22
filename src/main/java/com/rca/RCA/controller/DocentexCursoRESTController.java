package com.rca.RCA.controller;

import com.rca.RCA.service.DocentexCursoService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.AulaDTO;
import com.rca.RCA.type.DocentexCursoDTO;
import com.rca.RCA.type.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/asignatura")
public class DocentexCursoRESTController {
    @Autowired
    DocentexCursoService docentexCursoService;

    public DocentexCursoRESTController(){
    }

    @GetMapping
    public ApiResponse<Pagination<DocentexCursoDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return this.docentexCursoService.getList(filter, page, size);
    }

    @PostMapping
    public ApiResponse<DocentexCursoDTO> add(@RequestBody DocentexCursoDTO docentexCursoDTO) {
        return this.docentexCursoService.add(docentexCursoDTO);
    }

    @PutMapping
    public ApiResponse<DocentexCursoDTO> update(@RequestBody DocentexCursoDTO docentexCursoDTO) {
        return this.docentexCursoService.update(docentexCursoDTO);
    }
    @DeleteMapping("{id}")
    public ApiResponse<DocentexCursoDTO> delete(@PathVariable String id){
        return this.docentexCursoService.delete(id);
    }
}
