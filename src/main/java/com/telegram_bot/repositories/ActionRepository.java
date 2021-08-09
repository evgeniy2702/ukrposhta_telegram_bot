package com.telegram_bot.repositories;

import com.telegram_bot.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action, Long> {

}
