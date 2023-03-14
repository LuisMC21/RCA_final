package com.rca.RCA.controller;

import com.rca.RCA.service.CursoService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.CursoDTO;
import com.rca.RCA.type.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/curso")
public class CursoRESTController {

    @Autowired
    private CursoService cursoService;

    public CursoRESTController(){
    }

    @GetMapping
    public ApiResponse<Pagination<CursoDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return this.cursoService.getList(filter, page, size);
    }

    @PostMapping
    public ApiResponse<CursoDTO> add(@RequestBody CursoDTO cursoDTO){
        return this.cursoService.add(cursoDTO);
    }

    @PutMapping
    public ApiResponse<CursoDTO> update(@RequestBody CursoDTO cursoDTO){
        return this.cursoService.update(cursoDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<CursoDTO> delete(@PathVariable String id){
        return this.cursoService.delete(id);
    }

}
