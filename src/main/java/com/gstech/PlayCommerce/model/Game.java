package com.gstech.PlayCommerce.model;

import com.gstech.PlayCommerce.dto.GameRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tb_game")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String developer;
    private String publisher;
    private LocalDate releaseDate;
    private BigDecimal price;
    private String classification;
    private String linkDownload;
    private boolean available = true;

    public Game(GameRequestDTO request, Category category) {
        this.name = request.name();
        this.description = request.description();
        this.developer = request.developer();
        this.publisher = request.publisher();
        this.releaseDate = request.releaseDate();
        this.price = request.price();
        this.classification = request.classification();
        this.linkDownload = request.linkDownload();
        this.available = request.available() != null ? request.available() : true;
        this.category = category;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(mappedBy = "games")
    private Set<Client> clients = new HashSet<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<BuyGame> buyGames = new ArrayList<>();

}
