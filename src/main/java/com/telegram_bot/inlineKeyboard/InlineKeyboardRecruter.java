package com.telegram_bot.inlineKeyboard;


import com.telegram_bot.bot.BotContext;
import com.telegram_bot.bot.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class InlineKeyboardRecruter implements InlineKeyboardInterfase{


    @Override
    public SendMessage createInlineKeyBoard(Long chatId, BotContext context) {
        return null;
    }

    @Override
    public void handlerInlineKeyboard(Update update, BotContext context, ReplyKeyboardMarkup replyKeyboardMarkup) throws TelegramApiException {

        SendMessage message;
        BotState state = BotState.START2;
        String sendMessage = "Вітаю тебе з першим робочим днем!";
        message = SendMessage.builder()
                .chatId(String.valueOf(context.getUser().getChatId()))
                .text(sendMessage)
                .replyMarkup(replyKeyboardMarkup)
                .build();
        context.getUser().setStateId(state.ordinal());
        context.getBot().execute(message);
    }
}
