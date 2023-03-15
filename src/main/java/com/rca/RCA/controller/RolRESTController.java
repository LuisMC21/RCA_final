package com.rca.RCA.controller;

import com.rca.RCA.service.RolService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.RolDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/rol")
public class RolRESTController {

    @Autowired
    private RolService rolService;

    public RolRESTController(){

    }

    @GetMapping
    public ApiResponse<Pagination<RolDTO>> list(
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
    public ApiResponse<RolDTO> delete(@PathVariable String id) {
        return this.rolService.delete(id);
    }
}
