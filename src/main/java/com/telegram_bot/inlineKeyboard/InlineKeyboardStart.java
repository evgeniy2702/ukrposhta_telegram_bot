package com.telegram_bot.inlineKeyboard;

import com.telegram_bot.bot.BotContext;
import com.telegram_bot.bot.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class InlineKeyboardStart implements HandlerInlineKeyboard {

    @Override
    public void handlerInlineKeyboard(Update update, BotContext context, ReplyKeyboardMarkup replyKeyboardMarkup) throws TelegramApiException {
        SendMessage message;
        BotState state = BotState.START;
        if(!context.getUser().getNotified()) {
            message = SendMessage.builder()
                    .chatId(String.valueOf(context.getUser().getChatId()))
                    .text("Вітаю тебе !")
                    .replyMarkup(replyKeyboardMarkup)
                    .build();
            context.getUser().setStateId(state.ordinal());
            context.getBot().execute(message);
            context.setState(nextState(context));
        } else {
            context.setState(nextState(context));
        }
    }

    private BotState nextState(BotContext context){
        if(context.getUser().getNotified())
            return  BotState.START2;
        else
            return BotState.CALLNAME;
    }
}
