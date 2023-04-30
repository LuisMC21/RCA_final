package com.rca.RCA.controller;

import com.rca.RCA.service.AnioLectivoService;
import com.rca.RCA.type.AnioLectivoDTO;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
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
    @GetMapping("{id}")
    public ApiResponse<AnioLectivoDTO> one(@PathVariable String id) throws ResourceNotFoundException {
        return this.anioLectivoService.one(id);
    }
    @PostMapping
    public ApiResponse<AnioLectivoDTO> add(@RequestBody @Valid AnioLectivoDTO anioLectivoDTO) throws ResourceNotFoundException, AttributeException {
        return this.anioLectivoService.add(anioLectivoDTO);
    }

    @PutMapping
    public ApiResponse<AnioLectivoDTO> update(@RequestBody @Valid AnioLectivoDTO anioLectivoDTO) throws ResourceNotFoundException, AttributeException {
        return this.anioLectivoService.update(anioLectivoDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<AnioLectivoDTO> delete(@PathVariable String id) throws ResourceNotFoundException {
        return this.anioLectivoService.delete(id);
    }
}
