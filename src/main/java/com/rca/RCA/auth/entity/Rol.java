package com.rca.RCA.auth.entity;

import com.rca.RCA.auth.enums.RolNombre;
import com.rca.RCA.type.RolDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "role")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "tx_uniqueIdentifier", length = 40)
    private String uniqueIdentifier;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RolNombre rolNombre;

    public Rol() {
    }

    public Rol(@NotNull RolNombre rolNombre) {
        this.rolNombre = rolNombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RolNombre getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(RolNombre rolNombre) {
        this.rolNombre = rolNombre;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }


    public RolDTO getRolDTO(){
        RolDTO RolDTO = new RolDTO();
        RolDTO.setId(this.uniqueIdentifier);
        RolDTO.setRolNombre(this.rolNombre.toString());
        return RolDTO;
    }

    public void setRolDTO(RolDTO rolDTO){
        this.uniqueIdentifier = rolDTO.getId();
        this.rolNombre= RolNombre.valueOf(rolDTO.getRolNombre());
    }
}
