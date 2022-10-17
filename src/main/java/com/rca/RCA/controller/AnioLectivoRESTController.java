package com.rca.RCA.controller;

import com.rca.RCA.service.AnioLectivoService;
import com.rca.RCA.type.AnioLectivoDTO;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aniolectivo")
public class AnioLectivoRESTController {

    @Autowired
    private AnioLectivoService anioLectivoService;

    public AnioLectivoRESTController(){
    }

    @GetMapping
    public ApiResponse<Pagination<AnioLectivoDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return this.anioLectivoService.getList(filter, page, size);
    }

    @PostMapping
    public ApiResponse<AnioLectivoDTO> add(@RequestBody AnioLectivoDTO anioLectivoDTO){
        return this.anioLectivoService.add(anioLectivoDTO);
    }

    @PutMapping
    public ApiResponse<AnioLectivoDTO> update(@RequestBody AnioLectivoDTO anioLectivoDTO){
        return this.anioLectivoService.update(anioLectivoDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<AnioLectivoDTO> delete(@PathVariable String id){
        return this.anioLectivoService.delete(id);
    }
}
