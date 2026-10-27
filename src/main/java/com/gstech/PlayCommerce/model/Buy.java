package com.gstech.PlayCommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_compra")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Buy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime date;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "buy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BuyGame> buyGames = new ArrayList<>();

    @OneToOne(mappedBy = "buy", cascade = CascadeType.ALL)
    private Payment payment;
}
