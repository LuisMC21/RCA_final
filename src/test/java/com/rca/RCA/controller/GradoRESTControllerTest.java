package com.rca.RCA.controller;

import com.rca.RCA.entity.GradoEntity;
import com.rca.RCA.service.GradoService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.GradoDTO;
import com.rca.RCA.type.Pagination;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class GradoRESTControllerTest {
    @Mock
    private GradoService gradoService;

    @InjectMocks
    private GradoRESTController gradoRESTController;

    private GradoEntity gradoEntity;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gradoEntity= new GradoEntity();
        gradoEntity.setName('1');
        gradoEntity.setCode("GR001");
        gradoEntity.setUniqueIdentifier(UUID.randomUUID().toString());
    }
    @DisplayName("Test para agregar un grado")
    @Test
    void add() throws AttributeException {
        // given
        ApiResponse<GradoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(gradoEntity.getGradoDTO());
        when(gradoService.add(gradoEntity.getGradoDTO())).thenReturn(expectedApiResponse);

        //when
        ApiResponse<GradoDTO> actualApiResponse = gradoRESTController.add(gradoEntity.getGradoDTO());

        // then
        assertThat(actualApiResponse).isNotNull();
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getName()).isEqualTo(gradoEntity.getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(gradoEntity.getCode());
        assertThat(actualApiResponse.getData().getCreateAt()).isEqualTo(gradoEntity.getCreateAt());
        assertThat(actualApiResponse.getData().getId()).isEqualTo(gradoEntity.getUniqueIdentifier());
        verify(gradoService).add(this.gradoEntity.getGradoDTO());

    }
    @DisplayName("Test para listar los grados")
    @Test
    void list() {
        // Given
        String filter = "1";
        int page = 1;
        int size = 10;

        Pagination<GradoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(1);
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        List<GradoEntity> gradoEntities = new ArrayList<>();
        gradoEntities.add(gradoEntity);
        GradoEntity gradoEntity2 = new GradoEntity();
        gradoEntity2.setName('2');
        gradoEntities.add(gradoEntity2);
        pagination.setList(gradoEntities.stream().map(GradoEntity::getGradoDTO).collect(Collectors.toList()));

        ApiResponse<Pagination<GradoDTO>> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(pagination);

        when(gradoService.getList(filter, page, size)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<Pagination<GradoDTO>> actualApiResponse = gradoRESTController.list(filter, page, size);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals("ok", actualApiResponse.getMessage());
        assertEquals(pagination.getCountFilter(), actualApiResponse.getData().getCountFilter());
        assertThat(actualApiResponse.getMessage()).isEqualTo(expectedApiResponse.getMessage());

    }

    @DisplayName("Test para actualizar un grado")
    @Test
    void update() throws AttributeException, ResourceNotFoundException {
        //given
        GradoDTO gradoDTO2 = new GradoDTO();
        gradoDTO2.setName('2');
        gradoDTO2.setCode("GR001");
        gradoDTO2.setUpdateAt(LocalDateTime.now());

        ApiResponse<GradoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(gradoDTO2);

        when(gradoService.one(gradoEntity.getGradoDTO().getId())).thenReturn(expectedApiResponse);
        when(gradoService.update(gradoDTO2)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<GradoDTO> actualApiResponse = gradoRESTController.update(gradoDTO2);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals("ok", actualApiResponse.getMessage());
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getUpdateAt()).isEqualTo(expectedApiResponse.getData().getUpdateAt());
    }

    @DisplayName("Test para eliminar un grado")
    @Test
    void delete() throws ResourceNotFoundException, AttributeException {
        // given
        ApiResponse<GradoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        gradoEntity.setDeleteAt(LocalDateTime.now());
        expectedApiResponse.setData(gradoEntity.getGradoDTO());

        when(gradoService.delete(gradoEntity.getUniqueIdentifier())).thenReturn(expectedApiResponse);

        // when
        ApiResponse<GradoDTO> actualApiResponse = gradoRESTController.delete(gradoEntity.getUniqueIdentifier());

        // then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals("ok", actualApiResponse.getMessage());
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        assertThat(actualApiResponse.getData().getDeleteAt()).isEqualTo(expectedApiResponse.getData().getDeleteAt());

        verify(gradoService, times(1)).delete(gradoEntity.getUniqueIdentifier());
    }


    @DisplayName("Test para obtener un grado por id")
    @Test
    void one() throws ResourceNotFoundException {
        //given
        ApiResponse<GradoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(gradoEntity.getGradoDTO());

        when(gradoService.one(gradoEntity.getUniqueIdentifier())).thenReturn(expectedApiResponse);

        // When
        ApiResponse<GradoDTO> actualApiResponse = gradoRESTController.one(gradoEntity.getUniqueIdentifier());

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals("ok", actualApiResponse.getMessage());
        assertThat(actualApiResponse.getData().getId()).isEqualTo(gradoEntity.getUniqueIdentifier());
        assertThat(actualApiResponse.getData().getName()).isEqualTo(gradoEntity.getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(gradoEntity.getCode());

        verify(gradoService, times(1)).one(gradoEntity.getUniqueIdentifier());
    }

}