package com.telegram_bot.repositories;


import com.telegram_bot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {


    @Query("SELECT u FROM User u WHERE u.notified=false AND u.name IS NOT NUll")
    List<User> findNewUsers();

    User findByChatId(long id);
}
