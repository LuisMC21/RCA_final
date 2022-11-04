package com.rca.RCA.controller;

import com.rca.RCA.service.ClaseService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.ClaseDTO;
import com.rca.RCA.type.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clase")
public class ClaseRESTController {

    @Autowired
    private ClaseService claseService;

    public ClaseRESTController(){

    }

    @GetMapping
    public ApiResponse<Pagination<ClaseDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return this.claseService.getList(filter, page, size);
    }
    @PostMapping
    public ApiResponse<ClaseDTO> add(@RequestBody ClaseDTO ClaseDTO){
        return this.claseService.add(ClaseDTO);
    }

    @PutMapping
    public ApiResponse<ClaseDTO> update(@RequestBody ClaseDTO ClaseDTO) {
        return this.claseService.update(ClaseDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<ClaseDTO> delete(@PathVariable String id) {
        return this.claseService.delete(id);
    }
}
