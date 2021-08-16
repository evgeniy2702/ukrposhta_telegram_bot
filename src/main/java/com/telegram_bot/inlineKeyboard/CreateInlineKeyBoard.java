package com.telegram_bot.inlineKeyboard;

import com.telegram_bot.bot.BotContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public interface CreateInlineKeyBoard {

    SendMessage createInlineKeyBoard(Long chatId, BotContext context);
}
