package com.telegram_bot.Service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReplyKeyBoardMarkupService {


    public ReplyKeyboardMarkup replyButtons() {
        ReplyKeyboardMarkup replyKeyboardMarkup;

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Завершить работу с ботом"));
        keyboardRow.add(new KeyboardButton("Меню"));

        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(keyboardRow);

        replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .keyboard(rowList)
                .build();

        return replyKeyboardMarkup;
    }
}
