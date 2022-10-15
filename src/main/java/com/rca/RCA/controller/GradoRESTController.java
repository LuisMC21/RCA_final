package com.rca.RCA.controller;

import com.rca.RCA.service.GradoService;
import com.rca.RCA.service.SeccionService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.GradoDTO;
import com.rca.RCA.type.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grado")
public class GradoRESTController {

    @Autowired
    private GradoService gradoService;
    @Autowired
    private SeccionService seccionService;

    public GradoRESTController(){
    }

    @GetMapping
    public ApiResponse<Pagination<GradoDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return this.gradoService.getList(filter, page, size);
    }
/*
    @GetMapping("/{id}/secciones")
    public ApiResponse<Pagination<SeccionDTO>> listSectxGrad(
            @PathVariable String id,
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return this.seccionService.getListSxG(id, filter, page, size);
    }
*/
    @PostMapping
    public ApiResponse<GradoDTO> add(@RequestBody GradoDTO gradoDTO){
        return this.gradoService.add(gradoDTO);
    }

    @PutMapping
    public ApiResponse<GradoDTO> update(@RequestBody GradoDTO gradoDTO){
        return this.gradoService.update(gradoDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<GradoDTO> delete(@PathVariable String id){
        return this.gradoService.delete(id);
    }
}
