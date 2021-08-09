package com.telegram_bot.inlineKeyboard;

import com.telegram_bot.bot.BotContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyboardEnd implements InlineKeyboardInterfase{
    @Override
    public SendMessage createInlineKeyBoard(Long chatId, BotContext context) {
        return null;
    }

    @Override
    public void handlerInlineKeyboard(Update update, BotContext context, ReplyKeyboardMarkup replyKeyboardMarkup) throws TelegramApiException {

        KeyboardButton buttonStart = KeyboardButton.builder()
                .text("START")
                .build();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(buttonStart);

        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(keyboardRow);

        replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .selective(true)
                .keyboard(rowList)
                .build();

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(context.getUser().getChatId()))
                .text("На все добре, " + context.getUser().getName() + ". До нових зустрічей.")
                .replyMarkup(replyKeyboardMarkup)
                .build();
        context.getUser().setStateId(0);
        context.getUser().setRegistration(false);
        context.getBot().execute(message);
    }
}
