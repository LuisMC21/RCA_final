package com.rca.RCA.controller;

import com.rca.RCA.service.CursoService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.CursoDTO;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
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
    @GetMapping("{id}")
    public ApiResponse<CursoDTO> one(@PathVariable String id) throws ResourceNotFoundException {
        return this.cursoService.one(id);
    }

        @PostMapping
    public ApiResponse<CursoDTO> add(@RequestBody CursoDTO cursoDTO) throws AttributeException {
        return this.cursoService.add(cursoDTO);
    }

    @PutMapping
    public ApiResponse<CursoDTO> update(@RequestBody CursoDTO cursoDTO) throws ResourceNotFoundException, AttributeException {
        return this.cursoService.update(cursoDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<CursoDTO> delete(@PathVariable String id) throws ResourceNotFoundException {
        return this.cursoService.delete(id);
    }

}
