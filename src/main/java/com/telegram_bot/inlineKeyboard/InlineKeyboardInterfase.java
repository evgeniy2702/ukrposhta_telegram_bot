package com.telegram_bot.inlineKeyboard;


import com.telegram_bot.bot.BotContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public interface InlineKeyboardInterfase {

    SendMessage createInlineKeyBoard(Long chatId, BotContext context);

    void handlerInlineKeyboard(Update update, BotContext context, ReplyKeyboardMarkup replyKeyboardMarkup) throws TelegramApiException;

//    BotState nextState(BotContext context) throws TelegramApiException;
}
