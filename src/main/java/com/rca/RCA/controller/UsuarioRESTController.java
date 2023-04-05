package com.rca.RCA.controller;

import com.rca.RCA.service.UsuarioService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/usuario")
public class UsuarioRESTController {

    @Autowired
    private UsuarioService usuarioService;

    public UsuarioRESTController(){

    }

    @GetMapping
    public ApiResponse<Pagination<UsuarioDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return this.usuarioService.getList(filter, page, size);
    }

    @PostMapping
    public ApiResponse<UsuarioDTO> add(@RequestBody @Valid UsuarioDTO UsuarioDTO) {
        return this.usuarioService.add(UsuarioDTO);
    }

    @PutMapping
    public ApiResponse<UsuarioDTO> update(@RequestBody UsuarioDTO UsuarioDTO) {
        return this.usuarioService.update(UsuarioDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<UsuarioDTO> delete(@PathVariable String id) {
        return this.usuarioService.delete(id);
    }
}
