package com.telegram_bot.inlineKeyboard;


import com.telegram_bot.Service.DocumentHRService;
import com.telegram_bot.Service.RegionalDirectorateService;
import com.telegram_bot.bot.BotContext;
import com.telegram_bot.bot.BotState;
import com.telegram_bot.model.DocumentHR;
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
public class InlineKeyboardOffice implements HandlerInlineKeyboard, CreateInlineKeyBoard{

    private RegionalDirectorateService regionalDirectorateService;
    private DocumentHRService documentHRService;

    @Autowired
    public void setDocumentHRService(DocumentHRService documentHRService) {
        this.documentHRService = documentHRService;
    }

    @Autowired
    public void setRegionalDirectorateService(RegionalDirectorateService regionalDirectorateService) {
        this.regionalDirectorateService = regionalDirectorateService;
    }

    @Override
    public SendMessage createInlineKeyBoard(Long chatId, BotContext context) {
        InlineKeyboardMarkup inlineKeyboardMarkup;

        List<RegionalDirectorate> directorates = regionalDirectorateService.getList();

        List<InlineKeyboardButton> row = new ArrayList<>();
        List<List<InlineKeyboardButton>> listRow = new ArrayList<>();

        for (int i = 0; i < directorates.size(); i++) {
            if(directorates.get(i).getId() < 3){

                row.add(InlineKeyboardButton.builder()
                        .text(directorates.get(i).getName())
                        .callbackData(directorates.get(i).getState() + ":" + directorates.get(i).getId())
                        .build());
            }
        }

        listRow.add(row);

        inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(listRow)
                .build();

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("Зробiть вибір офісу, будь-ласка.\r\n")
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }

    @Override
    public void handlerInlineKeyboard(Update update, BotContext context, ReplyKeyboardMarkup replyKeyboardMarkup) throws TelegramApiException {

        List<DocumentHR> documentHRList = documentHRService.getList();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Тож, тобі необхідно обов'язково взяти з собою оригінали \r\n" +
                "наступних документів у відділ кадрів: \r\n");
        documentHRList.forEach(documentHR -> stringBuilder.append(documentHR.getId() + ". ")
                                                          .append(documentHR.getDocument_name() + "\r\n")
        );

        SendMessage message;
        BotState state = BotState.OFFICE;

        message = SendMessage.builder()
                .chatId(String.valueOf(context.getUser().getChatId()))
                .text(stringBuilder.toString())
                .replyMarkup(replyKeyboardMarkup)
                .build();
        context.getUser().setStateId(state.ordinal());
        context.getBot().execute(message);
    }
}
