package com.telegram_bot.Service;

import com.telegram_bot.bot.BotContext;
import com.telegram_bot.bot.BotState;
import com.telegram_bot.inlineKeyboard.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class SendInlineKeyBoardMessageService {

    private InlineKeyboardStart2 inlineKeyboardStart2;
    private InlineKeyboardDiraction inlineKeyboardDiraction;
    private InlineKeyboardAction inlineKeyboardAction;
    private InlineKeyboardOffice inlineKeyboardOffice;
    private InlineKeyboardAddress inlineKeyboardAddress;

    @Autowired
    public void setInlineKeyboardStart2(InlineKeyboardStart2 inlineKeyboardStart2) {
        this.inlineKeyboardStart2 = inlineKeyboardStart2;
    }

    @Autowired
    public void setInlineKeyboardAction(InlineKeyboardAction inlineKeyboardAction) {
        this.inlineKeyboardAction = inlineKeyboardAction;
    }

    @Autowired
    public void setInlineKeyboardDiraction(InlineKeyboardDiraction inlineKeyboardDiraction) {
        this.inlineKeyboardDiraction = inlineKeyboardDiraction;
    }

    @Autowired
    public void setInlineKeyboardOffice(InlineKeyboardOffice inlineKeyboardOffice) {
        this.inlineKeyboardOffice = inlineKeyboardOffice;
    }

    @Autowired
    public void setInlineKeyboardAddress(InlineKeyboardAddress inlineKeyboardAddress) {
        this.inlineKeyboardAddress = inlineKeyboardAddress;
    }

    public SendMessage sendInlineKeyBoardMessage(long chatId, BotState state, BotContext context) {

        SendMessage message = null;

        if (state.equals(BotState.START2)) {

            message = inlineKeyboardStart2.createInlineKeyBoard(chatId, context);
        }
        if (state.equals(BotState.DIRACTION)) {

            message = inlineKeyboardDiraction.createInlineKeyBoard(chatId, context);
        }
        if (state.equals(BotState.ACTION)) {

            message = inlineKeyboardAction.createInlineKeyBoard(chatId, context);
        }
        if(state.equals((BotState.OFFICE))){

            message = inlineKeyboardOffice.createInlineKeyBoard(chatId, context);
        }
        if(state.equals(BotState.ADDRESS)){

            message = inlineKeyboardAddress.createInlineKeyBoard(chatId, context);
        }
        return message;
    }
}
