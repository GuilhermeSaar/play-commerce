package com.gstech.PlayCommerce.model;

import com.gstech.PlayCommerce.dto.GameRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_jogo")
@NoArgsConstructor
@Getter
@Setter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, unique = true)
    private String name;

    @Column(name = "descricao", nullable = false)
    private String description;

    @Column(name = "desenvolvedor")
    private String developer;

    @Column(name = "publicadora", nullable = false)
    private String publisher;

    @Column(name = "data_lancamento", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "preco", nullable = false)
    private BigDecimal price;

    @Column(name = "classificacao", nullable = false)
    private int classification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Category category;

    public Game(GameRequestDTO request, Category category) {
        this.name = request.name();
        this.description = request.description();
        this.developer = request.developer();
        this.publisher = request.publisher();
        this.releaseDate = request.releaseDate();
        this.price = request.price();
        this.classification = request.classification();
        this.category = category;
    }

}
