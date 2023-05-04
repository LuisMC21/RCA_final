package com.rca.RCA.controller;

import com.rca.RCA.service.DocentexCursoService;
import com.rca.RCA.type.*;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

class DocentexCursoRESTControllerTest {
    @Mock
    private DocentexCursoService docentexCursoService;

    @InjectMocks
    private DocentexCursoRESTController docentexCursoRESTController;

    private DocentexCursoDTO docentexCursoDTO;

    private DocenteDTO docenteDTO;

    private CursoDTO cursoDTO;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        docentexCursoDTO =new DocentexCursoDTO();

        docenteDTO = new DocenteDTO();
        docenteDTO.setSpecialty("CIENCIAS");
        docenteDTO.setDose('1');
        docenteDTO.setCode("DOC001");
        docenteDTO.setId(UUID.randomUUID().toString());
        docenteDTO.setCreateAt(LocalDateTime.now());

        docentexCursoDTO.setDocenteDTO(docenteDTO);

        cursoDTO= new CursoDTO();
        cursoDTO.setName("MATEMÁTICAS");
        cursoDTO.setCode("CUR001");
        cursoDTO.setId(UUID.randomUUID().toString());
        cursoDTO.setCreateAt(LocalDateTime.now());

        docentexCursoDTO.setCursoDTO(cursoDTO);
    }

    @DisplayName("Test para listar asignaturas")
    @Test
    void list() {
        // Given
        String filter = "";
        int page = 1;
        int size = 10;

        Pagination<DocentexCursoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(2);
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        List<DocentexCursoDTO> docentexCursoDTOS = new ArrayList<>();
        docentexCursoDTOS.add(docentexCursoDTO);

        DocentexCursoDTO docentexCursoDTO2 = new DocentexCursoDTO();

        DocenteDTO docenteDTO2 = new DocenteDTO();
        docenteDTO2.setSpecialty("CIENCIAS SOCIALES");
        docenteDTO2.setDose('1');
        docenteDTO2.setCode("DOC002");
        docenteDTO2.setId(UUID.randomUUID().toString());
        docenteDTO2.setCreateAt(LocalDateTime.now());

        docentexCursoDTO2.setDocenteDTO(docenteDTO2);

        CursoDTO cursoDTO2= new CursoDTO();
        cursoDTO2.setName("MATEMÁTICAS");
        cursoDTO2.setCode("CUR001");
        cursoDTO2.setId(UUID.randomUUID().toString());
        cursoDTO2.setCreateAt(LocalDateTime.now());

        docentexCursoDTO2.setCursoDTO(cursoDTO2);

        docentexCursoDTOS.add(docentexCursoDTO2);

        pagination.setList(docentexCursoDTOS.stream().collect(Collectors.toList()));

        ApiResponse<Pagination<DocentexCursoDTO>> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(pagination);

        when(docentexCursoService.getList(filter, page, size)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<Pagination<DocentexCursoDTO>> actualApiResponse = docentexCursoRESTController.list(filter, page, size);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertEquals(expectedApiResponse.getData().getCountFilter(), actualApiResponse.getData().getCountFilter());
        assertThat(actualApiResponse.getMessage()).isEqualTo(expectedApiResponse.getMessage());
        assertThat(actualApiResponse.getData()).isEqualTo(expectedApiResponse.getData());

        verify(docentexCursoService).getList(filter, page, size);
    }
    @DisplayName("Test para obtener una asignatura por id")
    @Test
    void one() throws ResourceNotFoundException {
        //given
        ApiResponse<DocentexCursoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(docentexCursoDTO);

        when(docentexCursoService.one(docentexCursoDTO.getId())).thenReturn(expectedApiResponse);

        // When
        ApiResponse<DocentexCursoDTO> actualApiResponse = docentexCursoRESTController.one(docentexCursoDTO.getId());

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(docentexCursoService, times(1)).one(docentexCursoDTO.getId());
    }
    @DisplayName("Test para agregar una asignatura")
    @Test
    void add() throws ResourceNotFoundException, AttributeException {
        // given
        ApiResponse<DocentexCursoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(docentexCursoDTO);
        when(docentexCursoService.add(docentexCursoDTO)).thenReturn(expectedApiResponse);

        //when
        ApiResponse<DocentexCursoDTO> actualApiResponse = docentexCursoRESTController.add(docentexCursoDTO);

        // then
        assertThat(actualApiResponse).isNotNull();
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals("ok", actualApiResponse.getMessage());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getCreateAt()).isEqualTo(expectedApiResponse.getData().getCreateAt());
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        verify(docentexCursoService).add(this.docentexCursoDTO);
    }

    @DisplayName("Test para actualizar una asignatura")
    @Test
    void update() throws ResourceNotFoundException, AttributeException {
        //given
        DocentexCursoDTO docentexCursoDTO2 = docentexCursoDTO;
        CursoDTO cursoDTO2= new CursoDTO();
        cursoDTO2.setName("COMUNICACION");
        cursoDTO2.setCode("CUR002");
        cursoDTO2.setId(UUID.randomUUID().toString());
        cursoDTO2.setCreateAt(LocalDateTime.now());

        docentexCursoDTO2.setCursoDTO(cursoDTO2);

        ApiResponse<DocentexCursoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(docentexCursoDTO2);

        when(docentexCursoService.one(docentexCursoDTO2.getId())).thenReturn(expectedApiResponse);
        when(docentexCursoService.update(docentexCursoDTO2)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<DocentexCursoDTO> actualApiResponse = docentexCursoRESTController.update(docentexCursoDTO2);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getUpdateAt()).isEqualTo(expectedApiResponse.getData().getUpdateAt());
        verify(docentexCursoService).update(docentexCursoDTO2);
    }
    @DisplayName("Test para eliminar una asignatura")
    @Test
    void delete() throws ResourceNotFoundException {
        // given
        ApiResponse<DocentexCursoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(docentexCursoDTO);

        when(docentexCursoService.delete(docentexCursoDTO.getId())).thenReturn(expectedApiResponse);

        // when
        ApiResponse<DocentexCursoDTO> actualApiResponse = docentexCursoRESTController.delete(docentexCursoDTO.getId());

        // then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(docentexCursoService, times(1)).delete(docentexCursoDTO.getId());
    }
}