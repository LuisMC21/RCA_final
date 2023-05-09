package com.rca.RCA.controller;

import com.rca.RCA.entity.AnioLectivoEntity;
import com.rca.RCA.service.AnioLectivoService;
import com.rca.RCA.type.AnioLectivoDTO;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cglib.core.Local;

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

class AnioLectivoRESTControllerTest {

    @Mock
    private AnioLectivoService anioLectivoService;

    @InjectMocks
    private AnioLectivoRESTController anioLectivoRESTController;

    private AnioLectivoEntity anioLectivoEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        anioLectivoEntity= new AnioLectivoEntity();
        anioLectivoEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        anioLectivoEntity.setCode("ANIO001");
        anioLectivoEntity.setCreateAt(LocalDateTime.now());
        anioLectivoEntity.setName("2020");
    }

    @DisplayName("Test para listar los años lectivos")
    @Test
    void list() {
        // Given
        String filter = "";
        int page = 1;
        int size = 10;

        Pagination<AnioLectivoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(2);
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        List<AnioLectivoEntity> anioLectivoEntities = new ArrayList<>();
        anioLectivoEntities.add(anioLectivoEntity);
        AnioLectivoEntity anioLectivoEntity2 = new AnioLectivoEntity();
        anioLectivoEntity2.setName("2021");
        anioLectivoEntity2.setUniqueIdentifier(UUID.randomUUID().toString());
        anioLectivoEntity2.setCreateAt(LocalDateTime.now());
        anioLectivoEntities.add(anioLectivoEntity2);
        pagination.setList(anioLectivoEntities.stream().map(AnioLectivoEntity::getAnioLectivoDTO).collect(Collectors.toList()));

        ApiResponse<Pagination<AnioLectivoDTO>> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(pagination);

        when(anioLectivoService.getList(filter, page, size)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<Pagination<AnioLectivoDTO>> actualApiResponse = anioLectivoRESTController.list(filter, page, size);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertEquals(pagination.getCountFilter(), actualApiResponse.getData().getCountFilter());
        assertThat(actualApiResponse.getMessage()).isEqualTo(expectedApiResponse.getMessage());
        assertThat(actualApiResponse.getData()).isEqualTo(expectedApiResponse.getData());

        verify(anioLectivoService).getList(filter, page, size);
    }

    @DisplayName("Test para obtener un año lectivo por id")
    @Test
    void one() throws ResourceNotFoundException {
        //given
        ApiResponse<AnioLectivoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(anioLectivoEntity.getAnioLectivoDTO());

        when(anioLectivoService.one(anioLectivoEntity.getUniqueIdentifier())).thenReturn(expectedApiResponse);

        // When
        ApiResponse<AnioLectivoDTO> actualApiResponse = anioLectivoRESTController.one(anioLectivoEntity.getUniqueIdentifier());

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());

        verify(anioLectivoService, times(1)).one(anioLectivoEntity.getUniqueIdentifier());
    }

    @DisplayName("Test para agregar un año lectivo")
    @Test
    void add() throws ResourceNotFoundException, AttributeException {
        // given
        ApiResponse<AnioLectivoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(anioLectivoEntity.getAnioLectivoDTO());
        when(anioLectivoService.add(anioLectivoEntity.getAnioLectivoDTO())).thenReturn(expectedApiResponse);

        //when
        ApiResponse<AnioLectivoDTO> actualApiResponse = anioLectivoRESTController.add(anioLectivoEntity.getAnioLectivoDTO());

        // then
        assertThat(actualApiResponse).isNotNull();
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());

        verify(anioLectivoService).add(this.anioLectivoEntity.getAnioLectivoDTO());
    }


    @DisplayName("Test para actualizar un año lectivo")
    @Test
    void update() throws ResourceNotFoundException, AttributeException {
        //given
        AnioLectivoDTO anioLectivoDTO2 = new AnioLectivoDTO();
        anioLectivoDTO2.setName("2022");
        anioLectivoDTO2.setCode("ANI002");
        anioLectivoDTO2.setUpdateAt(LocalDateTime.now());

        ApiResponse<AnioLectivoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(anioLectivoDTO2);

        when(anioLectivoService.one(anioLectivoEntity.getUniqueIdentifier())).thenReturn(expectedApiResponse);
        when(anioLectivoService.update(anioLectivoDTO2)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<AnioLectivoDTO> actualApiResponse = anioLectivoRESTController.update(anioLectivoDTO2);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getUpdateAt()).isEqualTo(anioLectivoDTO2.getUpdateAt());

        verify(anioLectivoService).update(anioLectivoDTO2);
    }

    @DisplayName("Test para eliminar un año lectivo")
    @Test
    void delete() throws ResourceNotFoundException {
        // given
        ApiResponse<AnioLectivoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(anioLectivoEntity.getAnioLectivoDTO());

        when(anioLectivoService.delete(anioLectivoEntity.getUniqueIdentifier())).thenReturn(expectedApiResponse);

        // when
        ApiResponse<AnioLectivoDTO> actualApiResponse = anioLectivoRESTController.delete(anioLectivoEntity.getUniqueIdentifier());

        // then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(anioLectivoService, times(1)).delete(anioLectivoEntity.getUniqueIdentifier());
    }
}