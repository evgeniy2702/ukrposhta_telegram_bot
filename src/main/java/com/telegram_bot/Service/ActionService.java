package com.telegram_bot.Service;

import com.telegram_bot.model.Action;
import com.telegram_bot.repositories.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionService {

    private ActionRepository actionRepository;

    @Autowired
    public void setActionRepository(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public List<Action> getList() {
        return actionRepository.findAll();
    }
}
