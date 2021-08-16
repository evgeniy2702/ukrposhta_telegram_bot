package com.telegram_bot.Service;

import com.telegram_bot.bot.BotContext;
import com.telegram_bot.bot.BotState;
import com.telegram_bot.inlineKeyboard.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service

public class SendStateMessageService {


    private InlineKeyboardStart inlineKeyboardStart;
    private InlineKeyboardCallName inlineKeyboardCallName;
    private InlineKeyboardApprove inlineKeyboardApprove;
    private InlineKeyboardEnd inlineKeyboardEnd;
    private InlineKeyboardStart2 inlineKeyboardStart2;
    private InlineKeyboardDiraction inlineKeyboardDiraction;
    private InlineKeyboardAction inlineKeyboardAction;
    private InlineKeyboardOffice inlineKeyboardOffice;
    private InlineKeyboardAddress inlineKeyboardAddress;
    private InlineKeyboardRecruter inlineKeyboardRecruter;
    private ReplyKeyBoardMarkupService replyKeyBoardMarkupService;

    public boolean keyBoardBoolean = false;

    @Autowired
    public void setInlineKeyboardStart(InlineKeyboardStart inlineKeyboardStart) {
        this.inlineKeyboardStart = inlineKeyboardStart;
    }

    @Autowired
    public void setInlineKeyboardCallName(InlineKeyboardCallName inlineKeyboardCallName) {
        this.inlineKeyboardCallName = inlineKeyboardCallName;
    }

    @Autowired
    public void setInlineKeyboardApprove(InlineKeyboardApprove inlineKeyboardApprove) {
        this.inlineKeyboardApprove = inlineKeyboardApprove;
    }

    @Autowired
    public void setInlineKeyboardEnd(InlineKeyboardEnd inlineKeyboardEnd) {
        this.inlineKeyboardEnd = inlineKeyboardEnd;
    }

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

    @Autowired
    public void setInlineKeyboardRecruter(InlineKeyboardRecruter inlineKeyboardRecruter) {
        this.inlineKeyboardRecruter = inlineKeyboardRecruter;
    }

    @Autowired
    public void setReplyKeyBoardMarkupService(ReplyKeyBoardMarkupService replyKeyBoardMarkupService) {
        this.replyKeyBoardMarkupService = replyKeyBoardMarkupService;
    }

    public BotState sendStateMessage(BotState state,
                                     BotContext context,
                                     Update update) throws TelegramApiException {
        ReplyKeyboardMarkup replyKeyboardMarkup = replyKeyBoardMarkupService.replyButtons();

        if (state.equals(BotState.START)) {
            inlineKeyboardStart.handlerInlineKeyboard(update, context, replyKeyboardMarkup);

            state = context.getState();
        }
        if (state.equals(BotState.CALLNAME)) {
            inlineKeyboardCallName.handlerInlineKeyboard(update, context, replyKeyboardMarkup);
        }
        if (state.equals(BotState.APPROVED)) {
            inlineKeyboardApprove.handlerInlineKeyboard(update, context, replyKeyboardMarkup);

            state = context.getState();
        }
        if (state.equals(BotState.END)) {
            inlineKeyboardEnd.handlerInlineKeyboard(update, context, replyKeyboardMarkup);
        }
        if (state.equals(BotState.START2)) {
            keyBoardBoolean = true;
            inlineKeyboardStart2.handlerInlineKeyboard(update, context, replyKeyboardMarkup);
        }
        if (state.equals(BotState.DIRACTION)) {
            keyBoardBoolean = true;
            inlineKeyboardDiraction.handlerInlineKeyboard(update, context, replyKeyboardMarkup);
        }
        if (state.equals(BotState.ACTION)) {
            keyBoardBoolean = true;
            inlineKeyboardAction.handlerInlineKeyboard(update, context, replyKeyboardMarkup);
        }
        if(state.equals(BotState.OFFICE)){
            keyBoardBoolean = true;
            inlineKeyboardOffice.handlerInlineKeyboard(update,context, replyKeyboardMarkup);
        }
        if (state.equals(BotState.ADDRESS)) {
            keyBoardBoolean = true;
            inlineKeyboardAddress.handlerInlineKeyboard(update, context, replyKeyboardMarkup);
        }
        if(state.equals(BotState.RECRUTER)){
            inlineKeyboardRecruter.handlerInlineKeyboard(update, context, replyKeyboardMarkup);
        }
        return state;
    }
}
