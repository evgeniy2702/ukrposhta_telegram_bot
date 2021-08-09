package com.telegram_bot.Service;

import com.telegram_bot.model.RegionalDirectorate;
import com.telegram_bot.repositories.RegionalDirectorateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionalDirectorateService  {

    private RegionalDirectorateRepository repository;

    @Autowired
    public void setRepository(RegionalDirectorateRepository repository) {
        this.repository = repository;
    }


    public List<RegionalDirectorate> getList() {
        return repository.findAll();
    }
}
