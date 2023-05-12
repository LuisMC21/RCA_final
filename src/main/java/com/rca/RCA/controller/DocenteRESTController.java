package com.rca.RCA.controller;

import com.rca.RCA.service.DocenteService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.DocenteDTO;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.UsuarioDTO;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/docente")
public class DocenteRESTController {

    @Autowired
    private DocenteService docenteService;

    public DocenteRESTController(){
    }

    @GetMapping
    public ApiResponse<Pagination<DocenteDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return this.docenteService.getList(filter, page, size);
    }

    @GetMapping("{id}")
    public ApiResponse<DocenteDTO> one(@PathVariable String id) throws ResourceNotFoundException {
        return this.docenteService.one(id);
    }
    @PostMapping
    public ApiResponse<DocenteDTO> add(@RequestBody @Valid DocenteDTO docenteDTO) throws ResourceNotFoundException, AttributeException {
        return this.docenteService.add(docenteDTO);
    }

    @PutMapping
    public ApiResponse<DocenteDTO> update(@RequestBody @Valid DocenteDTO docenteDTO) throws ResourceNotFoundException, AttributeException {
        return this.docenteService.update(docenteDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<DocenteDTO> delete(@PathVariable String id) throws ResourceNotFoundException {
        return this.docenteService.delete(id);
    }

}
