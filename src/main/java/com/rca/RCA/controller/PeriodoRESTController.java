package com.rca.RCA.controller;

import com.rca.RCA.service.PeriodoService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.PeriodoDTO;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/periodo")
public class PeriodoRESTController {

    @Autowired
    private PeriodoService periodoService;

    public PeriodoRESTController(){
    }

    @GetMapping
    public ApiResponse<Pagination<PeriodoDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return this.periodoService.getList(filter, page, size);
    }

    @GetMapping("{id}")
    public ApiResponse<PeriodoDTO> one(@PathVariable String id) throws ResourceNotFoundException {
        return this.periodoService.one(id);
    }

    @PostMapping
    public ApiResponse<PeriodoDTO> add(@RequestBody @Valid PeriodoDTO periodoDTO) throws ResourceNotFoundException, AttributeException {
        return this.periodoService.add(periodoDTO);
    }

    @PutMapping
    public ApiResponse<PeriodoDTO> update(@RequestBody @Valid PeriodoDTO periodoDTO) throws ResourceNotFoundException, AttributeException {
        return this.periodoService.update(periodoDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<PeriodoDTO> delete(@PathVariable String id) throws ResourceNotFoundException {
        return this.periodoService.delete(id);
    }
}
