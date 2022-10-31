package com.rca.RCA.controller;

import com.rca.RCA.service.RolService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.RolDTO;
import com.rca.RCA.type.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/rol")
public class RolRESTController {

    @Autowired
    private RolService rolService;

    public RolRESTController(){

    }

    @GetMapping
    public Pagination<RolDTO> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return this.rolService.getList(filter, page, size);
    }
    @PostMapping
    public ApiResponse<RolDTO> add(@RequestBody @Valid RolDTO rolDTO){
        return this.rolService.add(rolDTO);
    }

    @PutMapping
    public ApiResponse<RolDTO> update(@RequestBody RolDTO rolDTO) {
        return this.rolService.update(rolDTO);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        this.rolService.delete(id);
    }
}
