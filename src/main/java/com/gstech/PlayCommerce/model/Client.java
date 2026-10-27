package com.gstech.PlayCommerce.model;

import com.gstech.PlayCommerce.dto.ClientRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_client")
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

    public Client(ClientRequestDTO request) {
        this.name = request.name();
        this.cpf = request.cpf();
        this.email = request.email();
        this.phone = request.phone();
        this.dateRegister = LocalDateTime.now();
    }
}
