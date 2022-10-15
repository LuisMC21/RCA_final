package com.rca.RCA.controller;

import com.rca.RCA.service.NoticiaService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.NoticiaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("noticia")
public class NoticiaRESTController {

    @Autowired
    private NoticiaService noticiaService;

    public NoticiaRESTController(){

    }

    @GetMapping
    public Pagination<NoticiaDTO> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return this.noticiaService.getList(filter, page, size);
    }

    @PostMapping
    public ApiResponse<NoticiaDTO> add(@RequestBody NoticiaDTO NoticiaDTO) {
        return this.noticiaService.add(NoticiaDTO);
    }

    @PutMapping
    public void update(@RequestBody NoticiaDTO NoticiaDTO) {
        this.noticiaService.update(NoticiaDTO);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        this.noticiaService.delete(id);
    }
}
