/*package com.rca.RCA.controller;

import com.rca.RCA.service.UsuarioService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuario")
public class UsuarioRESTController {

    @Autowired
    private UsuarioService usuarioService;

    public UsuarioRESTController(){

    }

    @GetMapping
    public Pagination<UsuarioDTO> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return this.usuarioService.getList(filter, page, size);
    }

    @PostMapping
    public ApiResponse<UsuarioDTO> add(@RequestBody UsuarioDTO UsuarioDTO) {
        return this.usuarioService.add(UsuarioDTO);
    }

    @PutMapping
    public void update(@RequestBody UsuarioDTO UsuarioDTO) {
        this.usuarioService.update(UsuarioDTO);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        this.usuarioService.delete(id);
    }
}

 */
