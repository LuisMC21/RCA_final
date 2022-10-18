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
@RequestMapping("/seccionxgrado")
public class AulaRESTController {
    @Autowired
    AulaService aulaService;

    public AulaRESTController(){
    }

    @GetMapping("/{id}")
    public ApiResponse<Pagination<SeccionDTO>> listSectxGrad(
            @PathVariable String id,
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return this.aulaService.getList(id, filter, page, size);
    }

    @PostMapping
    public ApiResponse<AulaDTO> add(@RequestBody Map ids) {
        return this.aulaService.add(ids);
    }

}
