package com.marketplace.ifba.model;

import com.marketplace.ifba.model.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
/*import java.util.List;*/

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idUsuario;

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @Column(name = "role", nullable = false, updatable = false)
    private UserRole role;

    @Column(name = "email", unique = true, nullable = false, updatable = false)
    private String email;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "cpf", unique = true, nullable = false, updatable = false)
    private String cpf;

    @Column(name = "data_registro", nullable = false, updatable = false)
    private LocalDate dataRegistro;

    @Column(name = "data_nascimento", nullable = false, updatable = false)
    private LocalDate dataNascimento;

    @Column(name = "biografia", nullable = false)
    private String biografia;

//    @Column(name = "foto_perfil_URL", nullable = false)
//    private String fotoPerfilURL;
//
//    @Column(name = "endereco", nullable = false)
//    private String endereco;

    @Column(name = "instituicao")
    private String instituicao;

    @Column(name = "organizacao")
    private String organizacao;

    /*@Column(nullable = false)
    private List<Conexao> conexoes;*/

    public User(String nomeCompleto, UserRole role, String email, String telefone, String password,String cpf, LocalDate dataNascimento,
                String biografia, String instituicao, String organizacao) {
        this.nomeCompleto = nomeCompleto;
        this.role = role;
        this.email = email;
        this.telefone = telefone;
        this.password = password;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.biografia = biografia;
        this.instituicao = instituicao;
        this.organizacao = organizacao;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
