package com.rca.RCA.controller;

import com.rca.RCA.service.ApoderadoService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.ApoderadoDTO;
import com.rca.RCA.type.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("apoderado")
public class ApoderadoRESTController {

    @Autowired
    private ApoderadoService apoderadoService;

    public ApoderadoRESTController(){

    }

    @GetMapping
    public ApiResponse<Pagination<ApoderadoDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return this.apoderadoService.getList(filter, page, size);
    }

    @PostMapping
    public ApiResponse<ApoderadoDTO> add(@RequestBody @Valid ApoderadoDTO ApoderadoDTO) {
        return this.apoderadoService.add(ApoderadoDTO);
    }

    @PutMapping
    public ApiResponse<ApoderadoDTO> update(@RequestBody ApoderadoDTO apoderadoDTO) {
        return this.apoderadoService.update(apoderadoDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<ApoderadoDTO> delete(@PathVariable String id) {
        return this.apoderadoService.delete(id);
    }
}
