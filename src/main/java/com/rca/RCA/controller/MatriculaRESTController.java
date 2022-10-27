package com.rca.RCA.controller;

import com.rca.RCA.service.MatriculaService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.MatriculaDTO;
import com.rca.RCA.type.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ApiResponse<MatriculaDTO> add(@RequestBody MatriculaDTO matriculaDTO) {
        return this.matriculaService.add(matriculaDTO);
    }
    /*

    @PutMapping
    public ApiResponse<AulaDTO> update(@RequestBody AulaDTO aulaDTO) {
        return this.aulaService.update(aulaDTO);
    }
    @DeleteMapping("{id}")
    public ApiResponse<AulaDTO> delete(@PathVariable String id){
        return this.aulaService.delete(id);
    }

 */
}