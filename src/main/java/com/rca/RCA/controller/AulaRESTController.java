package com.rca.RCA.controller;

import com.rca.RCA.service.AulaService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.SeccionDTO;
import com.rca.RCA.type.AulaDTO;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/aula")
public class AulaRESTController {
    @Autowired
    AulaService aulaService;

    public AulaRESTController(){
    }

    @GetMapping
    public ApiResponse<Pagination<AulaDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return this.aulaService.getList(filter, page, size);
    }

    @GetMapping("/anio")
    public ApiResponse<List<AulaDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "") String anio){
        return this.aulaService.getList(filter, anio);

    }
    @GetMapping("{id}")
    public ApiResponse<AulaDTO> one(@PathVariable String id) throws ResourceNotFoundException {
        return this.aulaService.one(id);
    }

    @PostMapping
    public ApiResponse<AulaDTO> add(@RequestBody AulaDTO aulaDTO) throws ResourceNotFoundException, AttributeException {
        return this.aulaService.add(aulaDTO);
    }
    @PutMapping
    public ApiResponse<AulaDTO> update(@RequestBody AulaDTO aulaDTO) throws ResourceNotFoundException, AttributeException {
        return this.aulaService.update(aulaDTO);
    }
    @DeleteMapping("{id}")
    public ApiResponse<AulaDTO> delete(@PathVariable String id) throws ResourceNotFoundException {
        return this.aulaService.delete(id);
    }
    @GetMapping("exportApoderados")
    public ResponseEntity<Resource> exportListApoderados(@RequestParam String id_aula,
                                                        @RequestParam String id_aniolectivo) throws ResourceNotFoundException {
        return this.aulaService.exportListApoderados(id_aula, id_aniolectivo);
    }
}
