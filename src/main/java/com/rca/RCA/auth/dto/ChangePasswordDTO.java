package com.rca.RCA.auth.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String idUser;

    private String newPassword;
}
