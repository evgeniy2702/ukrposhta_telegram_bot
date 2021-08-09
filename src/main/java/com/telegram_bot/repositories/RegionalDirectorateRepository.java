package com.telegram_bot.repositories;

import com.telegram_bot.model.RegionalDirectorate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionalDirectorateRepository extends JpaRepository<RegionalDirectorate, Long> {
}
