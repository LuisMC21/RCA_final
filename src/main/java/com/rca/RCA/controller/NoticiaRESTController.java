package com.rca.RCA.controller;

import com.rca.RCA.service.NoticiaService;
import com.rca.RCA.type.*;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/noticia")
public class NoticiaRESTController {

    @Autowired
    private NoticiaService noticiaService;

    public NoticiaRESTController(){

    }

    @GetMapping
    public ApiResponse<Pagination<NoticiaDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return this.noticiaService.getList(filter, page, size);
    }

    @GetMapping("{id}")
    public ApiResponse<NoticiaDTO> one(@PathVariable String id) throws ResourceNotFoundException {
        return this.noticiaService.one(id);
    }

    @PostMapping
    public ApiResponse<NoticiaDTO> add(@RequestBody @Valid NoticiaFileDTO noticiaFileDTO) throws AttributeException, ResourceNotFoundException {
        return this.noticiaService.add(noticiaFileDTO);
    }

    @PutMapping
    public ApiResponse<NoticiaDTO> update(@RequestBody NoticiaFileDTO NoticiaFileDTO) throws ResourceNotFoundException, AttributeException {
        return this.noticiaService.update(NoticiaFileDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<NoticiaDTO> delete(@PathVariable String id) throws ResourceNotFoundException {
        return this.noticiaService.delete(id);
    }
}
