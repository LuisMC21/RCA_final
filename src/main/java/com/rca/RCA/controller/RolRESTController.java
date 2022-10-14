package com.rca.RCA.controller;

import com.rca.RCA.service.RolService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.RolDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/rol")
public class RolRESTController {

    @Autowired
    private RolService rolService;

    public RolRESTController(){

    }

    @PostMapping
    public ApiResponse<RolDTO> add(@RequestBody RolDTO rolDTO){
        return this.rolService.add(rolDTO);
    }

    @PutMapping
    public ApiResponse<RolDTO> update(@RequestBody RolDTO rolDTO){
        return this.rolService.update(rolDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<RolDTO> delete(@PathVariable String id){
        return this.rolService.delete(id);
    }
}
