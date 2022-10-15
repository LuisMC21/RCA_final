package com.rca.RCA.controller;

import com.rca.RCA.service.SeccionxGradoService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.SeccionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seccionxgrado")
public class SeccionxGradoRESTController {
    @Autowired
    SeccionxGradoService seccionxGradoService;

    public SeccionxGradoRESTController(){
    }

    @GetMapping("/{id}")
    public ApiResponse<Pagination<SeccionDTO>> listSectxGrad(
            @PathVariable String id,
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return this.seccionxGradoService.getListSxG(id, filter, page, size);
    }


}
