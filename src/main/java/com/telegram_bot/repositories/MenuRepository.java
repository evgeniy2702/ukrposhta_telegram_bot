package com.telegram_bot.repositories;

import com.telegram_bot.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository  extends JpaRepository<Menu, Long> {


}
