package com.rca.RCA.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rca.RCA.auth.entity.Rol;
import com.rca.RCA.type.UsuarioDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Table(name = "user")
@Entity
public class UsuarioEntity extends AuditoryEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", length = 15)
    private String code;
    @NotNull
    private String name;

    @Column(name = "pa_surname")
    private String pa_surname;
    @Column(name = "ma_surname")
    private String ma_surname;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthdate")
    private Date birthdate;
    @Column(name = "type_doc")
    private String type_doc;
    @Column(name = "numdoc", unique = true)
    private String numdoc;
    @Column(name = "tel", unique = true)
    private String tel;
    @Column(name = "gra_inst")
    private String gra_inst;
    private String nombreUsuario;
    private String email;
    private String password;
    private String tokenPassword;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_rol", joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> roles = new HashSet<>();

    @OneToOne(mappedBy = "usuarioEntity")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private DocenteEntity docenteEntity;

    @OneToOne(mappedBy = "usuarioEntity")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AlumnoEntity alumnoEntity;

    @OneToMany(mappedBy = "usuarioEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<ImagenEntity> imagenEntities = new HashSet<>();

    @OneToMany(mappedBy = "usuarioEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<NoticiaEntity> noticiaEntities = new HashSet<>();

    public UsuarioEntity() {
    }

    public UsuarioEntity(String code, String name, String pa_surname, String ma_surname, Date birthdate, String type_doc, String numdoc, String tel, String gra_inst, String nombreUsuario, String email, String password, String tokenPassword, Set<Rol> roles, DocenteEntity docenteEntity, AlumnoEntity alumnoEntity, ApoderadoEntity apoderadoEntity, Set<ImagenEntity> imagenEntities, Set<NoticiaEntity> noticiaEntities) {
        this.code = code;
        this.name = name;
        this.pa_surname = pa_surname;
        this.ma_surname = ma_surname;
        this.birthdate = birthdate;
        this.type_doc = type_doc;
        this.numdoc = numdoc;
        this.tel = tel;
        this.gra_inst = gra_inst;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
        this.tokenPassword = tokenPassword;
        this.roles = roles;
        this.docenteEntity = docenteEntity;
        this.alumnoEntity = alumnoEntity;
        this.imagenEntities = imagenEntities;
        this.noticiaEntities = noticiaEntities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPa_surname() {
        return pa_surname;
    }

    public void setPa_surname(String pa_surname) {
        this.pa_surname = pa_surname;
    }

    public String getMa_surname() {
        return ma_surname;
    }

    public void setMa_surname(String ma_surname) {
        this.ma_surname = ma_surname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getType_doc() {
        return type_doc;
    }

    public void setType_doc(String type_doc) {
        this.type_doc = type_doc;
    }

    public String getNumdoc() {
        return numdoc;
    }

    public void setNumdoc(String numdoc) {
        this.numdoc = numdoc;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getGra_inst() {
        return gra_inst;
    }

    public void setGra_inst(String gra_inst) {
        this.gra_inst = gra_inst;
    }

    public DocenteEntity getDocenteEntity() {
        return docenteEntity;
    }

    public void setDocenteEntity(DocenteEntity docenteEntity) {
        this.docenteEntity = docenteEntity;
    }

    public AlumnoEntity getAlumnoEntity() {
        return alumnoEntity;
    }

    public void setAlumnoEntity(AlumnoEntity alumnoEntity) {
        this.alumnoEntity = alumnoEntity;
    }

    public Set<ImagenEntity> getImagenEntities() {
        return imagenEntities;
    }

    public void setImagenEntities(Set<ImagenEntity> imagenEntities) {
        this.imagenEntities = imagenEntities;
    }

    public Set<NoticiaEntity> getNoticiaEntities() {
        return noticiaEntities;
    }

    public void setNoticiaEntities(Set<NoticiaEntity> noticiaEntities) {
        this.noticiaEntities = noticiaEntities;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokenPassword() {
        return tokenPassword;
    }

    public void setTokenPassword(String tokenPassword) {
        this.tokenPassword = tokenPassword;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    public UsuarioDTO getUsuarioDTO(){
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(this.getUniqueIdentifier());
        usuarioDTO.setCode(this.code);
        usuarioDTO.setName(this.name);
        usuarioDTO.setNombreUsuario(this.nombreUsuario);
        usuarioDTO.setPa_surname(this.pa_surname);
        usuarioDTO.setMa_surname(this.ma_surname);
        usuarioDTO.setBirthdate(this.birthdate);
        usuarioDTO.setType_doc(this.type_doc);
        usuarioDTO.setNumdoc(this.numdoc);
        usuarioDTO.setTel(this.tel);
        usuarioDTO.setGra_inst(this.gra_inst);
        usuarioDTO.setEmail(this.email);
        usuarioDTO.setPassword(this.password);
        usuarioDTO.setStatus(this.getStatus());
        usuarioDTO.setCreateAt(this.getCreateAt());
        usuarioDTO.setUpdateAt(this.getUpdateAt());
        usuarioDTO.setDeleteAt(this.getDeleteAt());
        return usuarioDTO;
    }
    public void setUsuarioDTO(UsuarioDTO UsuarioDTO){
        this.setUniqueIdentifier(UsuarioDTO.getId());
        this.code = UsuarioDTO.getCode();
        this.nombreUsuario = UsuarioDTO.getNombreUsuario();
        this.name = UsuarioDTO.getName();
        this.pa_surname = UsuarioDTO.getPa_surname();
        this.ma_surname = UsuarioDTO.getMa_surname();
        this.birthdate = UsuarioDTO.getBirthdate();
        this.type_doc = UsuarioDTO.getType_doc();
        this.numdoc = UsuarioDTO.getNumdoc();
        this.tel = UsuarioDTO.getTel();
        this.gra_inst = UsuarioDTO.getGra_inst();
        this.email = UsuarioDTO.getEmail();
        this.password = UsuarioDTO.getPassword();
        this.setStatus(UsuarioDTO.getStatus());
        this.setCreateAt(UsuarioDTO.getCreateAt());
        this.setUpdateAt(UsuarioDTO.getUpdateAt());
        this.setDeleteAt(UsuarioDTO.getDeleteAt());
    }

    public String getNameCompleto(){
        return this.pa_surname + " "+ ma_surname + " "+ this.name;
    }

}