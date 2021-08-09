package com.telegram_bot.inlineKeyboard;

import com.telegram_bot.Service.ActionService;
import com.telegram_bot.bot.BotContext;
import com.telegram_bot.bot.BotState;
import com.telegram_bot.model.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyboardAction implements InlineKeyboardInterfase {

    private ActionService actionService;

    @Autowired
    public void setActionService(ActionService actionService) {
        this.actionService = actionService;
    }

    @Override
    public SendMessage createInlineKeyBoard(Long chatId, BotContext context) {
        InlineKeyboardMarkup inlineKeyboardMarkup;

        List<Action> actions = actionService.getList();

        List<List<InlineKeyboardButton>> listRow = new ArrayList<>();

        for (Action action : actions) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(InlineKeyboardButton.builder()
                    .text(action.getName_action())
                    .callbackData(action.getState())
                    .build());
            listRow.add(row);
        }

        inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(listRow)
                .build();

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("Будь-ласка, " + context.getUser().getName() +
                        " оберіть питання : \r\n")
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }

    @Override
    public void handlerInlineKeyboard(Update update, BotContext context, ReplyKeyboardMarkup replyKeyboardMarkup) throws TelegramApiException {

        SendMessage message;
        BotState state = BotState.ACTION;
        String string = "Я ваш помічник та допоможу вам корисною інформацією !\r\n";
        message = SendMessage.builder()
                .chatId(String.valueOf(context.getUser().getChatId()))
                .text(string)
                .replyMarkup(replyKeyboardMarkup)
                .build();
        context.getUser().setStateId(state.ordinal());
        context.getBot().execute(message);
    }
}
