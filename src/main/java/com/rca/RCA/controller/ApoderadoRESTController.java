package com.rca.RCA.controller;

import com.rca.RCA.service.ApoderadoService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.GradoDTO;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.ApoderadoDTO;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("apoderado")
public class ApoderadoRESTController {

    @Autowired
    private ApoderadoService apoderadoService;

    public ApoderadoRESTController(){

    }

    @GetMapping
    public ApiResponse<Pagination<ApoderadoDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return this.apoderadoService.getList(filter, page, size);
    }

    @GetMapping("{id}")
    public ApiResponse<ApoderadoDTO> one(@PathVariable String id) throws ResourceNotFoundException {
        return this.apoderadoService.one(id);
    }

    @PostMapping
    public ApiResponse<ApoderadoDTO> add(@RequestBody @Valid ApoderadoDTO ApoderadoDTO) throws AttributeException, ResourceNotFoundException {
        return this.apoderadoService.add(ApoderadoDTO);
    }

    @PutMapping
    public ApiResponse<ApoderadoDTO> update(@RequestBody ApoderadoDTO apoderadoDTO) throws ResourceNotFoundException, AttributeException {
        return this.apoderadoService.update(apoderadoDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<ApoderadoDTO> delete(@PathVariable String id) throws ResourceNotFoundException {
        return this.apoderadoService.delete(id);
    }
}
