package com.rca.RCA.controller;

import com.rca.RCA.service.ClaseService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.GradoDTO;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.ClaseDTO;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")
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

    @GetMapping("cpau")
    public ApiResponse<Pagination<ClaseDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String periodo,
            @RequestParam String aula,
            @RequestParam String curso
    ) {
        return this.claseService.getList(filter, page, size, periodo, aula, curso);
    }

    @GetMapping("{id}")
    public ApiResponse<ClaseDTO> one(@PathVariable String id) throws ResourceNotFoundException {
        return this.claseService.one(id);
    }
    @PostMapping
    public ApiResponse<ClaseDTO> add(@RequestBody @Valid ClaseDTO ClaseDTO) throws ResourceNotFoundException {
        return this.claseService.add(ClaseDTO);
    }

    @PutMapping
    public ApiResponse<ClaseDTO> update(@RequestBody ClaseDTO ClaseDTO) throws ResourceNotFoundException {
        return this.claseService.update(ClaseDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<ClaseDTO> delete(@PathVariable String id) {
        return this.claseService.delete(id);
    }
}
