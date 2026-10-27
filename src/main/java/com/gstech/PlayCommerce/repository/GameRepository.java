package com.gstech.PlayCommerce.repository;

import com.gstech.PlayCommerce.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
