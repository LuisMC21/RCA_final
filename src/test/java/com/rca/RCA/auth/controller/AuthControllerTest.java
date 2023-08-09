package com.rca.RCA.auth.controller;

import com.rca.RCA.auth.dto.JwtDto;
import com.rca.RCA.auth.dto.LoginUsuario;
import com.rca.RCA.auth.service.LoginService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.UsuarioDTO;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private AuthController authController;

    private JwtDto jwtDto;

    private LoginUsuario loginUsuario;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtDto = new JwtDto();
        jwtDto.setToken("epmasdlasñdsapd781283u12jio3lkn12bkj2b131920");

        loginUsuario = new LoginUsuario();
        loginUsuario.setNombreUsuario("user@gmail.com");
        loginUsuario.setPassword("user");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setCreateAt(LocalDateTime.now());
        usuarioDTO.setId(UUID.randomUUID().toString());
        usuarioDTO.setStatus("CREATED");
        usuarioDTO.setName("user");
        usuarioDTO.setPa_surname("user");
        usuarioDTO.setMa_surname("user");
        usuarioDTO.setBirthdate(Date.valueOf("2020-10-10"));
        usuarioDTO.setNumdoc("71987578");
        usuarioDTO.setType_doc("DNI");
        usuarioDTO.setEmail("user@gmail.com");
        usuarioDTO.setPassword("user");
        usuarioDTO.setGra_inst("Bachiller");
        usuarioDTO.setTel("983719301");
        usuarioDTO.setRol("TEACHER");
    }


    @DisplayName("Test para agregar un usuario")
    @Test
    void add() throws AttributeException, ResourceNotFoundException {
        // given
        ApiResponse<UsuarioDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(usuarioDTO);
        when(loginService.add(usuarioDTO)).thenReturn(expectedApiResponse);

        //when
        ApiResponse<UsuarioDTO> actualApiResponse = authController.add(usuarioDTO);

        // then
        assertThat(actualApiResponse).isNotNull();
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getName()).isEqualTo(usuarioDTO.getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(usuarioDTO.getCode());
        assertThat(actualApiResponse.getData().getCreateAt()).isEqualTo(usuarioDTO.getCreateAt());
        assertThat(actualApiResponse.getData().getId()).isEqualTo(usuarioDTO.getId());
        verify(loginService).add(this.usuarioDTO);
    }


    @DisplayName("Test para login")
    @Test
    void login() throws AttributeException, ResourceNotFoundException {
        //given
        ApiResponse<JwtDto> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(jwtDto);

        when(loginService.login(loginUsuario)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<JwtDto> actualApiResponse = authController.login(loginUsuario);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getToken()).isEqualTo(expectedApiResponse.getData().getToken());

        verify(loginService, times(1)).login(loginUsuario);
    }
}