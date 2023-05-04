package com.rca.RCA.controller;

import com.rca.RCA.entity.CursoEntity;
import com.rca.RCA.entity.SeccionEntity;
import com.rca.RCA.service.CursoService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.CursoDTO;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.SeccionDTO;
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

class CursoRESTControllerTest {

    @Mock
    private CursoService cursoService;

    @InjectMocks
    private CursoRESTController cursoRESTController;

    private CursoEntity cursoEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cursoEntity= new CursoEntity();
        cursoEntity.setName("MATEMÁTICAS");
        cursoEntity.setCode("CUR001");
        cursoEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        cursoEntity.setCreateAt(LocalDateTime.now());
    }

    @DisplayName("Test para listar los cursos")
    @Test
    void list() {
        // Given
        String filter = "";
        int page = 1;
        int size = 10;

        Pagination<CursoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(2);
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        List<CursoEntity> cursoEntities = new ArrayList<>();
        cursoEntities.add(cursoEntity);
        CursoEntity cursoEntity2 = new CursoEntity();
        cursoEntity2.setName("COMUNICACIÓN");
        cursoEntities.add(cursoEntity2);
        pagination.setList(cursoEntities.stream().map(CursoEntity::getCursoDTO).collect(Collectors.toList()));

        ApiResponse<Pagination<CursoDTO>> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(pagination);

        when(cursoService.getList(filter, page, size)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<Pagination<CursoDTO>> actualApiResponse = cursoRESTController.list(filter, page, size);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertEquals(expectedApiResponse.getData().getCountFilter(), actualApiResponse.getData().getCountFilter());
        assertThat(actualApiResponse.getMessage()).isEqualTo(expectedApiResponse.getMessage());
        assertThat(actualApiResponse.getData()).isEqualTo(expectedApiResponse.getData());

        verify(cursoService).getList(filter, page, size);
    }

    @DisplayName("Test para obtener un curso por id")
    @Test
    void one() throws ResourceNotFoundException {
        //given
        ApiResponse<CursoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(cursoEntity.getCursoDTO());

        when(cursoService.one(cursoEntity.getUniqueIdentifier())).thenReturn(expectedApiResponse);

        // When
        ApiResponse<CursoDTO> actualApiResponse = cursoRESTController.one(cursoEntity.getUniqueIdentifier());

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());

        verify(cursoService, times(1)).one(cursoEntity.getUniqueIdentifier());
    }

    @DisplayName("Test para agregar un curso")
    @Test
    void add() throws AttributeException {
        // given
        ApiResponse<CursoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(cursoEntity.getCursoDTO());
        when(cursoService.add(cursoEntity.getCursoDTO())).thenReturn(expectedApiResponse);

        //when
        ApiResponse<CursoDTO> actualApiResponse = cursoRESTController.add(cursoEntity.getCursoDTO());

        // then
        assertThat(actualApiResponse).isNotNull();
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());

        verify(cursoService).add(this.cursoEntity.getCursoDTO());
    }

    @DisplayName("Test para actualizar un curso")
    @Test
    void update() throws ResourceNotFoundException, AttributeException {
        //given
        CursoDTO cursoDTO2 = new CursoDTO();
        cursoDTO2.setName("COMUNICACION");
        cursoDTO2.setCode("CUR001");
        cursoDTO2.setUpdateAt(LocalDateTime.now());

        ApiResponse<CursoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(cursoDTO2);

        when(cursoService.one(cursoEntity.getUniqueIdentifier())).thenReturn(expectedApiResponse);
        when(cursoService.update(cursoDTO2)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<CursoDTO> actualApiResponse = cursoRESTController.update(cursoDTO2);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getUpdateAt()).isEqualTo(expectedApiResponse.getData().getUpdateAt());

        verify(cursoService).update(cursoDTO2);
    }

    @DisplayName("Test para eliminar un curso")
    @Test
    void delete() throws ResourceNotFoundException {
        // given
        ApiResponse<CursoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(cursoEntity.getCursoDTO());

        when(cursoService.delete(cursoEntity.getUniqueIdentifier())).thenReturn(expectedApiResponse);

        // when
        ApiResponse<CursoDTO> actualApiResponse = cursoRESTController.delete(cursoEntity.getUniqueIdentifier());

        // then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(cursoService, times(1)).delete(cursoEntity.getUniqueIdentifier());
    }
}