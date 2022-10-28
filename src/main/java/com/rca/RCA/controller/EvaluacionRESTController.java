package com.rca.RCA.controller;

import com.rca.RCA.service.EvaluacionService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.EvaluacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evaluacion")
public class EvaluacionRESTController {

    @Autowired
    private EvaluacionService evaluacionService;

    public EvaluacionRESTController(){

    }

    @GetMapping
    public ApiResponse<Pagination<EvaluacionDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return this.evaluacionService.getList(filter, page, size);
    }

    @PostMapping
    public ApiResponse<EvaluacionDTO> add(@RequestBody EvaluacionDTO EvaluacionDTO) {
        return this.evaluacionService.add(EvaluacionDTO);
    }

    @PutMapping
    public ApiResponse<EvaluacionDTO> update(@RequestBody EvaluacionDTO EvaluacionDTO) {
        return this.evaluacionService.update(EvaluacionDTO);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        this.evaluacionService.delete(id);
    }
}
