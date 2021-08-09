package com.telegram_bot.Service;

import com.telegram_bot.model.Menu;
import com.telegram_bot.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    private MenuRepository menuRepository;

    @Autowired
    public void setMenuRepository(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Menu> getListMenu(){
        return menuRepository.findAll();
    }
}
