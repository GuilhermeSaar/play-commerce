package com.gstech.PlayCommerce.model;

import com.gstech.PlayCommerce.model.enums.BuyStatus;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BuyStatus status;

    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "buy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BuyGame> buyGames = new ArrayList<>();

    @OneToOne(mappedBy = "buy", cascade = CascadeType.ALL)
    private Payment payment;

    public Buy(Client client, BuyStatus status, List<BuyGame> items, Payment payment) {
        this.status = status;
        this.date = LocalDateTime.now();
        this.client = client;
        this.buyGames = new ArrayList<>(items);
        this.buyGames.forEach(item -> item.setBuy(this));
        this.payment = payment;
        this.payment.setBuy(this);
    }
}
