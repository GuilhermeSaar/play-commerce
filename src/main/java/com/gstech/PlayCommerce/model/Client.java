package com.gstech.PlayCommerce.model;

import com.gstech.PlayCommerce.UserRole;
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

    private String name;
    private String cpf;
    private String email;
    private String phone;
    private LocalDateTime dateRegister;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "client")
    private List<Buy> buyList = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "library",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "jogo_id")
    )
    private Set<Game> games = new HashSet<>();


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
