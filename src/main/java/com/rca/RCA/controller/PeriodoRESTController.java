package com.rca.RCA.controller;

import com.rca.RCA.service.PeriodoService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.PeriodoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping("{aniolectivo_id}")
    public ApiResponse<PeriodoDTO> add(@PathVariable String aniolectivo_id,
                                       @RequestBody PeriodoDTO periodoDTO){
        return this.periodoService.add(aniolectivo_id, periodoDTO);
    }

    @PutMapping
    public ApiResponse<PeriodoDTO> update(@RequestBody PeriodoDTO periodoDTO){
        return this.periodoService.update(periodoDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<PeriodoDTO> delete(@PathVariable String id){
        return this.periodoService.delete(id);
    }
}
