package com.gstech.PlayCommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_biblioteca")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Library {

    @EmbeddedId
    private LibraryId libraryId =  new LibraryId();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    @MapsId("clientId")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jogo")
    @MapsId("gameId")
    private Game game;
}
