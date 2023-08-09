/*package com.rca.RCA.controller;

import com.rca.RCA.service.NoticiaService;
import com.rca.RCA.type.*;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoticiaRESTControllerTest {

    @Mock
    private NoticiaService noticiaService;

    @InjectMocks
    private NoticiaRESTController noticiaRESTController;

    private NoticiaDTO noticiaDTO;

    private UsuarioDTO usuarioDTO;

    private NoticiaFileDTO noticiaFileDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        noticiaDTO= new NoticiaDTO();
        noticiaDTO.setDate(Date.valueOf("2020-10-10"));
        noticiaDTO.setCode("NOT001");
        noticiaDTO.setRoute("/images/lkdasmdkas.jpg");
        noticiaDTO.setSommelier("Sommelier");
        noticiaDTO.setDescrip("Noticia descripcipón");
        noticiaDTO.setTitle("Titulo de la noticia");
        noticiaDTO.setId(UUID.randomUUID().toString());
        noticiaDTO.setCreateAt(LocalDateTime.now());

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setCreateAt(LocalDateTime.now());
        usuarioDTO.setId(UUID.randomUUID().toString());

        noticiaDTO.setUsuarioDTO(usuarioDTO);
    }

    @DisplayName("Test para listar las noticias")
    @Test
    void list() {
        // Given
        String filter = "";
        int page = 1;
        int size = 10;

        Pagination<NoticiaDTO> pagination = new Pagination<>();
        pagination.setCountFilter(2);
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        List<NoticiaDTO> noticiaDTOS = new ArrayList<>();
        noticiaDTOS.add(noticiaDTO);

        NoticiaDTO noticiaDTO2= new NoticiaDTO();
        noticiaDTO2.setDate(Date.valueOf("2020-10-10"));
        noticiaDTO2.setCode("NOT001");
        noticiaDTO2.setRoute("/images/lkdasmdkas.jpg");
        noticiaDTO2.setSommelier("Sommelier");
        noticiaDTO2.setDescrip("Noticia descripcipón");
        noticiaDTO2.setTitle("Titulo de la noticia");
        noticiaDTO2.setId(UUID.randomUUID().toString());
        noticiaDTO2.setCreateAt(LocalDateTime.now());
        noticiaDTOS.add(noticiaDTO2);
        pagination.setList(noticiaDTOS.stream().collect(Collectors.toList()));

        ApiResponse<Pagination<NoticiaDTO>> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(pagination);

        when(noticiaService.getList(filter, page, size)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<Pagination<NoticiaDTO>> actualApiResponse = noticiaRESTController.list(filter, page, size);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertEquals(expectedApiResponse.getData().getCountFilter(), actualApiResponse.getData().getCountFilter());
        assertThat(actualApiResponse.getMessage()).isEqualTo(expectedApiResponse.getMessage());
        assertThat(actualApiResponse.getData()).isEqualTo(expectedApiResponse.getData());

        verify(noticiaService).getList(filter, page, size);
    }

    @DisplayName("Test para obtener una noticia por id")
    @Test
    void one() throws ResourceNotFoundException {
        //given
        ApiResponse<NoticiaDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(noticiaDTO);

        when(noticiaService.one(noticiaDTO.getId())).thenReturn(expectedApiResponse);

        // When
        ApiResponse<NoticiaDTO> actualApiResponse = noticiaRESTController.one(noticiaDTO.getId());

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());

        verify(noticiaService, times(1)).one(noticiaDTO.getId());
    }

    @DisplayName("Test para agregar una noticia")
    @Test
    void add() throws ResourceNotFoundException, AttributeException {
        // given
        ApiResponse<NoticiaDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");

        noticiaFileDTO = new NoticiaFileDTO();
        noticiaFileDTO.setDate(Date.valueOf("2020-10-10"));
        noticiaFileDTO.setSommelier("Sommelier");
        noticiaFileDTO.setDescrip("Noticia descripcipón");
        noticiaFileDTO.setTitle("Titulo de la noticia");
        expectedApiResponse.setData(noticiaDTO);

        when(noticiaService.add(noticiaFileDTO)).thenReturn(expectedApiResponse);

        //when
        ApiResponse<NoticiaDTO> actualApiResponse = noticiaRESTController.add(noticiaFileDTO);

        // then
        assertThat(actualApiResponse).isNotNull();
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());

        verify(noticiaService).add(this.noticiaFileDTO);
    }

    @DisplayName("Test para actualizar una noticia")
    @Test
    void update() throws ResourceNotFoundException {
        //given
        NoticiaDTO noticiaDTO2 = noticiaDTO;
        noticiaDTO2.setTitle("Título editado");

        ApiResponse<NoticiaDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(noticiaDTO2);

        when(noticiaService.one(noticiaDTO2.getId())).thenReturn(expectedApiResponse);
        when(noticiaService.update(noticiaDTO2)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<NoticiaDTO> actualApiResponse = noticiaRESTController.update(noticiaDTO2);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getUpdateAt()).isEqualTo(expectedApiResponse.getData().getUpdateAt());
        verify(noticiaService).update(noticiaDTO2);
    }

    @DisplayName("Test para eliminar una noticia")
    @Test
    void delete() throws ResourceNotFoundException {
        // given
        ApiResponse<NoticiaDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(noticiaDTO);

        when(noticiaService.delete(noticiaDTO.getId())).thenReturn(expectedApiResponse);

        // when
        ApiResponse<NoticiaDTO> actualApiResponse = noticiaRESTController.delete(noticiaDTO.getId());

        // then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(noticiaService, times(1)).delete(noticiaDTO.getId());
    }
}

*/