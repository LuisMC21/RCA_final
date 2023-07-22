package com.rca.RCA.controller;

import com.rca.RCA.service.GradoService;
import com.rca.RCA.service.SeccionService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.GradoDTO;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:4200")
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
    @GetMapping("{id}")
    public ApiResponse<GradoDTO> one(@PathVariable String id) throws ResourceNotFoundException {
        return this.gradoService.one(id);
    }
    @PostMapping
    public ApiResponse<GradoDTO> add(@RequestBody GradoDTO gradoDTO) throws AttributeException {
        return this.gradoService.add(gradoDTO);
    }

    @PutMapping
    public ApiResponse<GradoDTO> update(@RequestBody GradoDTO gradoDTO) throws ResourceNotFoundException, AttributeException {
        return this.gradoService.update(gradoDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<GradoDTO> delete(@PathVariable String id) throws ResourceNotFoundException, AttributeException {
        return this.gradoService.delete(id);
    }
}
