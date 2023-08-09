package com.rca.RCA.controller;

import com.rca.RCA.service.ClaseService;
import com.rca.RCA.type.*;
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

class ClaseRESTControllerTest {

    @Mock
    private ClaseService claseService;

    @InjectMocks
    private  ClaseRESTController claseRESTController;

    private ClaseDTO claseDTO;

    private PeriodoDTO periodoDTO;

    private DocentexCursoDTO docentexCursoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        claseDTO = new ClaseDTO();
        claseDTO.setId(UUID.randomUUID().toString());
        claseDTO.setCode("CLA001");
        claseDTO.setCreateAt(LocalDateTime.now());
        claseDTO.setStatus("CREATED");

        periodoDTO = new PeriodoDTO();
        periodoDTO.setId(UUID.randomUUID().toString());
        periodoDTO.setName("I");
        periodoDTO.setStatus("CREATED");
        periodoDTO.setDate_start(Date.valueOf("2020-10-10"));
        periodoDTO.setDate_end(Date.valueOf("2020-10-10"));

        docentexCursoDTO = new DocentexCursoDTO();
        docentexCursoDTO.setId(UUID.randomUUID().toString());
        docentexCursoDTO.setCode("DXC001");
        docentexCursoDTO.setCreateAt(LocalDateTime.now());
        docentexCursoDTO.setStatus("CREATED");
    }
    @DisplayName("Test para listar clases")
    @Test
    void list() {
        // Given
        String filter = "";
        int page = 1;
        int size = 10;

        Pagination<ClaseDTO> pagination = new Pagination<>();
        pagination.setCountFilter(1);
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        List<ClaseDTO> claseDTOS = new ArrayList<>();
        claseDTOS.add(claseDTO);

        pagination.setList(claseDTOS.stream().collect(Collectors.toList()));

        ApiResponse<Pagination<ClaseDTO>> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(pagination);

        when(claseService.getList(filter, page, size)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<Pagination<ClaseDTO>> actualApiResponse = claseRESTController.list(filter, page, size);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertEquals(expectedApiResponse.getData().getCountFilter(), actualApiResponse.getData().getCountFilter());
        assertThat(actualApiResponse.getMessage()).isEqualTo(expectedApiResponse.getMessage());
        assertThat(actualApiResponse.getData()).isEqualTo(expectedApiResponse.getData());

        verify(claseService).getList(filter, page, size);
    }

    @DisplayName("Test para obtener una clase con id")
    @Test
    void one() throws ResourceNotFoundException {
        //given
        ApiResponse<ClaseDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(claseDTO);

        when(claseService.one(claseDTO.getId())).thenReturn(expectedApiResponse);

        // When
        ApiResponse<ClaseDTO> actualApiResponse = claseRESTController.one(claseDTO.getId());

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(claseService, times(1)).one(claseDTO.getId());
    }

    @DisplayName("Test para agregar una clase")
    @Test
    void add() throws ResourceNotFoundException {
        // given
        ApiResponse<ClaseDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(claseDTO);
        when(claseService.add(claseDTO)).thenReturn(expectedApiResponse);

        //when
        ApiResponse<ClaseDTO> actualApiResponse = claseRESTController.add(claseDTO);

        // then
        assertThat(actualApiResponse).isNotNull();
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals("ok", actualApiResponse.getMessage());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getCreateAt()).isEqualTo(expectedApiResponse.getData().getCreateAt());
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        verify(claseService).add(this.claseDTO);
    }

    @DisplayName("Test para actualizar una clase")
    @Test
    void update() throws ResourceNotFoundException {
        //given
        ClaseDTO claseDTO2 = claseDTO;
        claseDTO2.setDate(Date.valueOf("2020-10-30"));

        ApiResponse<ClaseDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(claseDTO2);

        when(claseService.one(claseDTO2.getId())).thenReturn(expectedApiResponse);
        when(claseService.update(claseDTO2)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<ClaseDTO> actualApiResponse = claseRESTController.update(claseDTO2);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getUpdateAt()).isEqualTo(expectedApiResponse.getData().getUpdateAt());
        verify(claseService).update(claseDTO2);
    }

    @DisplayName("Test para eliminar una clase")
    @Test
    void delete() {
        // given
        ApiResponse<ClaseDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(claseDTO);

        when(claseService.delete(claseDTO.getId())).thenReturn(expectedApiResponse);

        // when
        ApiResponse<ClaseDTO> actualApiResponse = claseRESTController.delete(claseDTO.getId());

        // then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(claseService, times(1)).delete(claseDTO.getId());
    }
}