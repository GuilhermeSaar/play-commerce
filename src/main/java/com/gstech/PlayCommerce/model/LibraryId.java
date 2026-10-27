package com.gstech.PlayCommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LibraryId implements Serializable {

    @Column(name = "id_cliente", nullable = false)
    private Long clientId;

    @Column(name = "id_jogo", nullable = false)
    private Long gameId;
}
