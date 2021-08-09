package com.telegram_bot.inlineKeyboard;

import com.telegram_bot.Service.RegionalDirectorateService;
import com.telegram_bot.bot.BotContext;
import com.telegram_bot.bot.BotState;
import com.telegram_bot.model.RegionalDirectorate;
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
public class InlineKeyboardDiraction implements InlineKeyboardInterfase {

    private RegionalDirectorateService regionalDirectorateService;

    @Autowired
    public void setRegionalDirectorate(RegionalDirectorateService regionalDirectorateService) {
        this.regionalDirectorateService = regionalDirectorateService;
    }

    @Override
    public SendMessage createInlineKeyBoard(Long chatId, BotContext context) {
        InlineKeyboardMarkup inlineKeyboardMarkup;

        List<RegionalDirectorate> directorates = regionalDirectorateService.getList();

        List<List<InlineKeyboardButton>> listRow = new ArrayList<>();

        for (int i = 0; i < directorates.size(); i++) {
            if(directorates.get(i).getId() >= 3){
                List<InlineKeyboardButton> row = new ArrayList<>();
                row.add(InlineKeyboardButton.builder()
                    .text(directorates.get(i).getName())
                    .callbackData(directorates.get(i).getState() + ":" + directorates.get(i).getId())
                    .build());
                listRow.add(row);
            }
        }

        inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(listRow)
                .build();

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("Перелік дирекцій : ")
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }

    @Override
    public void handlerInlineKeyboard(Update update, BotContext context, ReplyKeyboardMarkup replyKeyboardMarkup) throws TelegramApiException {
        SendMessage message;
        BotState state = BotState.DIRACTION;
        message = SendMessage.builder()
                .chatId(String.valueOf(context.getUser().getChatId()))
                .text("Оберіть будь-ласка регіональну дирекцію\r\n")
                .replyMarkup(replyKeyboardMarkup)
                .build();
        context.getUser().setStateId(state.ordinal());
        context.getBot().execute(message);
    }
}
