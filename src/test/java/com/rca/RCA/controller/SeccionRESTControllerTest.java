package com.rca.RCA.controller;

import com.rca.RCA.entity.SeccionEntity;
import com.rca.RCA.service.SeccionService;
import com.rca.RCA.type.ApiResponse;
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

class SeccionRESTControllerTest {

    @Mock
    private SeccionService seccionService;

    @InjectMocks
    private SeccionRESTController seccionRESTController;

    private SeccionEntity seccionEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        seccionEntity= new SeccionEntity();
        seccionEntity.setName('A');
        seccionEntity.setCode("SEC001");
        seccionEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        seccionEntity.setCreateAt(LocalDateTime.now());
    }
    @DisplayName("Test para listar las secciones")
    @Test
    void list() {
        // Given
        String filter = "";
        int page = 1;
        int size = 10;

        Pagination<SeccionDTO> pagination = new Pagination<>();
        pagination.setCountFilter(2);
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        List<SeccionEntity> seccionEntities = new ArrayList<>();
        seccionEntities.add(seccionEntity);
        SeccionEntity seccionEntity2 = new SeccionEntity();
        seccionEntity2.setName('B');
        seccionEntities.add(seccionEntity2);
        pagination.setList(seccionEntities.stream().map(SeccionEntity::getSeccionDTO).collect(Collectors.toList()));

        ApiResponse<Pagination<SeccionDTO>> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(pagination);

        when(seccionService.getList(filter, page, size)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<Pagination<SeccionDTO>> actualApiResponse = seccionRESTController.list(filter, page, size);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertEquals(expectedApiResponse.getData().getCountFilter(), actualApiResponse.getData().getCountFilter());
        assertThat(actualApiResponse.getMessage()).isEqualTo(expectedApiResponse.getMessage());
        assertThat(actualApiResponse.getData()).isEqualTo(expectedApiResponse.getData());

        verify(seccionService).getList(filter, page, size);
    }

    @DisplayName("Test para obtener una sección por id")
    @Test
    void one() throws ResourceNotFoundException {
        //given
        ApiResponse<SeccionDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(seccionEntity.getSeccionDTO());

        when(seccionService.one(seccionEntity.getUniqueIdentifier())).thenReturn(expectedApiResponse);

        // When
        ApiResponse<SeccionDTO> actualApiResponse = seccionRESTController.one(seccionEntity.getUniqueIdentifier());

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());

        verify(seccionService, times(1)).one(seccionEntity.getUniqueIdentifier());
    }

    @DisplayName("Test para agregar una sección")
    @Test
    void add() throws AttributeException {
        // given
        ApiResponse<SeccionDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(seccionEntity.getSeccionDTO());
        when(seccionService.add(seccionEntity.getSeccionDTO())).thenReturn(expectedApiResponse);

        //when
        ApiResponse<SeccionDTO> actualApiResponse = seccionRESTController.add(seccionEntity.getSeccionDTO());

        // then
        assertThat(actualApiResponse).isNotNull();
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());

        verify(seccionService).add(this.seccionEntity.getSeccionDTO());
    }

    @DisplayName("Test para actualizar una sección")
    @Test
    void update() throws ResourceNotFoundException, AttributeException {
        //given
        SeccionDTO seccionDTO2 = new SeccionDTO();
        seccionDTO2.setName('B');
        seccionDTO2.setCode("GR001");
        seccionDTO2.setUpdateAt(LocalDateTime.now());

        ApiResponse<SeccionDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(seccionDTO2);

        when(seccionService.one(seccionEntity.getUniqueIdentifier())).thenReturn(expectedApiResponse);
        when(seccionService.update(seccionDTO2)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<SeccionDTO> actualApiResponse = seccionRESTController.update(seccionDTO2);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getUpdateAt()).isEqualTo(expectedApiResponse.getData().getUpdateAt());

        verify(seccionService).update(seccionDTO2);
    }

    @DisplayName("Test para eliminar una sección")
    @Test
    void delete() throws ResourceNotFoundException, AttributeException {
        // given
        ApiResponse<SeccionDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(seccionEntity.getSeccionDTO());

        when(seccionService.delete(seccionEntity.getUniqueIdentifier())).thenReturn(expectedApiResponse);

        // when
        ApiResponse<SeccionDTO> actualApiResponse = seccionRESTController.delete(seccionEntity.getUniqueIdentifier());

        // then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(seccionService, times(1)).delete(seccionEntity.getUniqueIdentifier());
    }
}