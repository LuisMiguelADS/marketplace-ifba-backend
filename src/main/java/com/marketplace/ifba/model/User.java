package com.marketplace.ifba.model;

import com.marketplace.ifba.model.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
/*import java.util.List;*/

@Entity(name = "users")
@Data
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idUsuario;

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private LocalDateTime dataRegistro;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false)
    private String biografia;

    @Column(nullable = false)
    private String fotoPerfilURL;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String instituicao;

    @Column(nullable = false)
    private String organizacao;

    public User(String email, String password, UserRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
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

    /*@Column(nullable = false)
    private List<Conexao> conexoes;*/
}
