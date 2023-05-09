package com.rca.RCA.controller;

import com.rca.RCA.entity.AnioLectivoEntity;
import com.rca.RCA.entity.PeriodoEntity;
import com.rca.RCA.service.PeriodoService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.PeriodoDTO;
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

class PeriodoRESTControllerTest {
    @Mock
    private PeriodoService periodoService;

    @InjectMocks
    private PeriodoRESTController periodoRESTController;

    private PeriodoEntity periodoEntity;

    private AnioLectivoEntity anioLectivoEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        periodoEntity= new PeriodoEntity();
        periodoEntity.setName("I");
        periodoEntity.setCode("PER001");
        periodoEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        periodoEntity.setCreateAt(LocalDateTime.now());

        anioLectivoEntity= new AnioLectivoEntity();
        anioLectivoEntity.setName("2020");
        anioLectivoEntity.setCode("ANIO001");
        anioLectivoEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        anioLectivoEntity.setCreateAt(LocalDateTime.now());

        periodoEntity.setAnio_lectivoEntity(anioLectivoEntity);
    }

    @DisplayName("Test para listar los periodos")
    @Test
    void list() {
        // Given
        String filter = "";
        int page = 1;
        int size = 10;

        Pagination<PeriodoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(2);
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        List<PeriodoEntity> periodoEntities = new ArrayList<>();
        periodoEntities.add(periodoEntity);
        PeriodoEntity periodoEntity2 = new PeriodoEntity();
        periodoEntity2.setName("II");
        periodoEntity2.setCode("PER002");
        periodoEntity2.setUniqueIdentifier(UUID.randomUUID().toString());
        periodoEntity2.setCreateAt(LocalDateTime.now());
        periodoEntity2.setAnio_lectivoEntity(anioLectivoEntity);
        periodoEntities.add(periodoEntity2);
        pagination.setList(periodoEntities.stream().map(PeriodoEntity::getPeriodoDTO).collect(Collectors.toList()));

        ApiResponse<Pagination<PeriodoDTO>> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(pagination);

        when(periodoService.getList(filter, page, size)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<Pagination<PeriodoDTO>> actualApiResponse = periodoRESTController.list(filter, page, size);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertEquals(expectedApiResponse.getData().getCountFilter(), actualApiResponse.getData().getCountFilter());
        assertThat(actualApiResponse.getMessage()).isEqualTo(expectedApiResponse.getMessage());
        assertThat(actualApiResponse.getData()).isEqualTo(expectedApiResponse.getData());

        verify(periodoService).getList(filter, page, size);
    }

    @DisplayName("Test para obtener un periodo por id")
    @Test
    void one() throws ResourceNotFoundException {
        //given
        ApiResponse<PeriodoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(periodoEntity.getPeriodoDTO());

        when(periodoService.one(periodoEntity.getUniqueIdentifier())).thenReturn(expectedApiResponse);

        // When
        ApiResponse<PeriodoDTO> actualApiResponse = periodoRESTController.one(periodoEntity.getUniqueIdentifier());

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());

        verify(periodoService, times(1)).one(periodoEntity.getUniqueIdentifier());
    }

    @DisplayName("Test para agregar un periodo")
    @Test
    void add() throws AttributeException, ResourceNotFoundException {
        // given
        ApiResponse<PeriodoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(periodoEntity.getPeriodoDTO());
        when(periodoService.add(periodoEntity.getPeriodoDTO())).thenReturn(expectedApiResponse);

        //when
        ApiResponse<PeriodoDTO> actualApiResponse = periodoRESTController.add(periodoEntity.getPeriodoDTO());

        // then
        assertThat(actualApiResponse).isNotNull();
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());

        verify(periodoService).add(this.periodoEntity.getPeriodoDTO());
    }


    @DisplayName("Test para actualizar un periodo")
    @Test
    void update() throws ResourceNotFoundException, AttributeException {
        //given
        PeriodoDTO periodoDTO2 = new PeriodoDTO();
        periodoDTO2.setName("II");
        periodoDTO2.setCode("PER001");
        periodoDTO2.setUpdateAt(LocalDateTime.now());

        ApiResponse<PeriodoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(periodoDTO2);

        when(periodoService.one(periodoEntity.getUniqueIdentifier())).thenReturn(expectedApiResponse);
        when(periodoService.update(periodoDTO2)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<PeriodoDTO> actualApiResponse = periodoRESTController.update(periodoDTO2);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getUpdateAt()).isEqualTo(expectedApiResponse.getData().getUpdateAt());

        verify(periodoService).update(periodoDTO2);
    }


    @DisplayName("Test para eliminar un periodo")
    @Test
    void delete() throws ResourceNotFoundException {
        // given
        ApiResponse<PeriodoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(periodoEntity.getPeriodoDTO());

        when(periodoService.delete(periodoEntity.getUniqueIdentifier())).thenReturn(expectedApiResponse);

        // when
        ApiResponse<PeriodoDTO> actualApiResponse = periodoRESTController.delete(periodoEntity.getUniqueIdentifier());

        // then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(periodoService, times(1)).delete(periodoEntity.getUniqueIdentifier());
    }
}