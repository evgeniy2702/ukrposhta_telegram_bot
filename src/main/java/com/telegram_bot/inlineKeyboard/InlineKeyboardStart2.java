package com.telegram_bot.inlineKeyboard;

import com.telegram_bot.Service.MenuService;
import com.telegram_bot.bot.BotContext;
import com.telegram_bot.bot.BotState;
import com.telegram_bot.model.Menu;
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
public class InlineKeyboardStart2 implements HandlerInlineKeyboard, CreateInlineKeyBoard{

    private MenuService menuService;

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    public SendMessage createInlineKeyBoard(Long chatId, BotContext context) {

        InlineKeyboardMarkup inlineKeyboardMarkup;

        List<Menu> actions = menuService.getListMenu();
        List<InlineKeyboardButton> row = new ArrayList<>();

        actions.forEach(action ->
                row.add(InlineKeyboardButton.builder()
                        .text(action.getName_action())
                        .callbackData(action.getState())
                        .build()
                )
        );

        List<List<InlineKeyboardButton>> listRow = new ArrayList<>();
        listRow.add(row);

        inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(listRow)
                .build();

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("Що будемо робити ?\r\n")
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }

    @Override
    public void handlerInlineKeyboard(Update update, BotContext context, ReplyKeyboardMarkup replyKeyboardMarkup) throws TelegramApiException {
        SendMessage message;
        BotState state = BotState.START2;
        if(context.getUser().getNotified()) {
            message = SendMessage.builder()
                    .chatId(String.valueOf(context.getUser().getChatId()))
                    .text("Радий тебе знову бачити !\r\n")
                    .replyMarkup(replyKeyboardMarkup)
                    .build();
            context.getUser().setStateId(state.ordinal());
            context.getUser().setRegistration(true);
            context.getBot().execute(message);
        }
    }

}
