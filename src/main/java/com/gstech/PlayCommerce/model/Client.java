package com.gstech.PlayCommerce.model;

import com.gstech.PlayCommerce.model.enums.UserRole;
import com.gstech.PlayCommerce.dto.ClientRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tb_client")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateRegister;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Buy> buyList = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Library> libraries = new ArrayList<>();

    public Client(ClientRequestDTO request, String password) {
        this.name = request.name();
        this.cpf = request.cpf();
        this.email = request.email();
        this.phone = request.phone();
        this.dateRegister = LocalDateTime.now();
        this.password = password;
        this.role = UserRole.USER;
    }
}
