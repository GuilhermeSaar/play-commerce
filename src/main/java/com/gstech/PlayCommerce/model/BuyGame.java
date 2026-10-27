package com.gstech.PlayCommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "compra_jogo")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BuyGame {

    @EmbeddedId
    private BuyGameId buyGameId = new BuyGameId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("buyId")
    @JoinColumn(name = "id_compra")
    private Buy buy;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("gameId")
    @JoinColumn(name = "id_jogo")
    private Game game;

    @Column(name = "quantidade", nullable = false)
    private Integer quantity;

    @Column(name = "preco", nullable = false)
    private BigDecimal price;
}
