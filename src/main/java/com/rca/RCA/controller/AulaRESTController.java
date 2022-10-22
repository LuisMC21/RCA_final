package com.rca.RCA.controller;

import com.rca.RCA.service.AulaService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.SeccionDTO;
import com.rca.RCA.type.AulaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping
    public ApiResponse<AulaDTO> add(@RequestBody AulaDTO aulaDTO) {
        return this.aulaService.add(aulaDTO);
    }

    @PutMapping
    public ApiResponse<AulaDTO> update(@RequestBody AulaDTO aulaDTO) {
        return this.aulaService.update(aulaDTO);
    }
    @DeleteMapping("{id}")
    public ApiResponse<AulaDTO> delete(@PathVariable String id){
        return this.aulaService.delete(id);
    }
}
