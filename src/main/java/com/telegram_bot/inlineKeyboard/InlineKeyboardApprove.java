package com.telegram_bot.inlineKeyboard;

import com.telegram_bot.bot.BotContext;
import com.telegram_bot.bot.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
public class InlineKeyboardApprove implements HandlerInlineKeyboard{

    @Override
    public void handlerInlineKeyboard(Update update,
                                      BotContext context,
                                      ReplyKeyboardMarkup replyKeyboardMarkup) throws TelegramApiException {
        if (!context.getUser().getNotified()) {
            BotState state = BotState.ACTION;
            context.getUser().setStateId(state.ordinal());
            context.getUser().setNotified(true);
            context.getUser().setName(context.getInput());
            context.getBot().execute(SendMessage.builder()
                    .chatId(String.valueOf(context.getUser().getChatId()))
                    .text("Дякую вам за звернення, " + context.getUser().getName() + " !")
                    .replyMarkup(replyKeyboardMarkup)
                    .build());
            context.setState(nextState());
        }

    }

    private BotState nextState(){
        return BotState.ACTION;
    }
}
