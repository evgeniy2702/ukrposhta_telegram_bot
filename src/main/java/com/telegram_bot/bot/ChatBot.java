package com.telegram_bot.bot;


import com.telegram_bot.Service.UserService;
import com.telegram_bot.inlineKeyboard.*;
import com.telegram_bot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


@Component
@PropertySource("classpath:telegram.properties")
public class ChatBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private InlineKeyboardStart inlineKeyboardStart;
    private InlineKeyboardCallName inlineKeyboardCallName;
    private InlineKeyboardApprove inlineKeyboardApprove;
    private InlineKeyboardStart2 inlineKeyboardStart2;
    private InlineKeyboardEnd inlineKeyboardEnd;
    private InlineKeyboardDiraction inlineKeyboardDiraction;
    private InlineKeyboardAction inlineKeyboardAction;
    private InlineKeyboardOffice inlineKeyboardOffice;
    private InlineKeyboardAddress inlineKeyboardAddress;
    private InlineKeyboardRecruter inlineKeyboardRecruter;

    private boolean keyBoardBoolean = false;
    private UserService userService;

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
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    //turn on bot by name and token
    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                final String text = update.getMessage().getText();
                final long chatId = update.getMessage().getChatId();

                User user = userService.findUserByChatId(chatId);

                BotState state;
                BotContext context;
                if (user == null) {
                    state = BotState.getInitialState();

                    user = new User(chatId, state.ordinal());
                    userService.addUser(user);

                    context = BotContext.of(this, user, text, state);

                } else {
                    state = BotState.byId(user.getStateId());

                    context = BotContext.of(this, user, text, state);
                }

                state = ifReplyKey(update, state);

                try {
                    state = sendStateMessage(state, context, update);

                    if (keyBoardBoolean) {
                        execute(sendInlineKeyBoardMessage(update.getMessage().getChatId(), state, context));
                        keyBoardBoolean = false;
                    }

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                userService.updateUser(user);
            }
        } else if (update.hasCallbackQuery()) {

            User user = userService.findUserByChatId(update.getCallbackQuery().getMessage().getChatId());

            BotState state = BotState.byId(user.getStateId());

            BotContext context = BotContext.of(this, user, update.getCallbackQuery().getMessage().getText(), state);

            try {
                handlerCallBackQuery(update, state, context);

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            userService.updateUser(user);
        }
    }

    private ReplyKeyboardMarkup replyButtons() {
        ReplyKeyboardMarkup replyKeyboardMarkup;

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Завершить работу с ботом"));
        keyboardRow.add(new KeyboardButton("Меню"));

        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(keyboardRow);

        replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .keyboard(rowList)
                .build();

        return replyKeyboardMarkup;
    }

    private BotState ifReplyKey(Update update, BotState state) {

        if (!update.hasCallbackQuery()) {
            if (update.getMessage().getText().equals("Завершить работу с ботом")) {
                state = BotState.END;
            }
            if (update.getMessage().getText().equals("Меню")) {
                state = BotState.START2;
            }
            if (update.getMessage().getText().equals("START")){
                state = BotState.START;
            }
        }
        return state;
    }

    private BotState sendStateMessage(BotState state,
                                      BotContext context,
                                      Update update) throws TelegramApiException {
        ReplyKeyboardMarkup replyKeyboardMarkup = replyButtons();

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

    private SendMessage sendInlineKeyBoardMessage(long chatId, BotState state, BotContext context) {

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

    private void handlerCallBackQuery(Update update, BotState state, BotContext context) throws TelegramApiException {


        CallbackQuery callbackQuery = update.getCallbackQuery();
        Message message = callbackQuery.getMessage();
        String data = callbackQuery.getData().split(":")[0];

        Long chatId = message.getChatId();

        if(data.equals("INPROCESS")){
            execute(SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .text("Поки що, " + context.getUser().getName() + ", цей розділ " +
                            "знаходиться у стадії розробки. Прошу вибачення.")
                    .build());
            state = BotState.ACTION;
        }

        if(data.equals("ТАК")){
            execute(SendMessage.builder().chatId(String.valueOf(chatId))
                    .text("Тепер залишилось тільки під'їхати в відділ кадрів з необхідними документами\r\n" +
                            "Рекрутер повідомить Вам про ваш перший робочий день!\r\n" +
                            "А я підкажу Вам швидче зорієнтуватись в задачах.\r\nТож до зустрічи.")
                    .build());
            state = BotState.RECRUTER;
        }

        if(data.equals("НІ")){
            execute(SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .text("Ми поважаємо ваш вибір та бажаємо успіхів !")
                    .build());
            state = BotState.END;
        }


        for (BotState botState : BotState.values()) {
            if (botState.name().equals(data)) {
                state = botState;
                break;
            }
        }

        if (state == BotState.DIRACTION) {
            inlineKeyboardDiraction.handlerInlineKeyboard(update, context, replyButtons());
            execute(sendInlineKeyBoardMessage(chatId, state, context));
        }
        if (state == BotState.ACTION) {
            inlineKeyboardAction.handlerInlineKeyboard(update, context, replyButtons());
            execute(sendInlineKeyBoardMessage(chatId, state, context));
        }
        if(state == BotState.OFFICE){
            inlineKeyboardOffice.handlerInlineKeyboard(update, context, replyButtons());
            execute(sendInlineKeyBoardMessage(chatId, state, context));
        }
        if(state == BotState.ADDRESS){
            inlineKeyboardAddress.handlerInlineKeyboard(update, context, replyButtons());
            execute(sendInlineKeyBoardMessage(chatId, state, context));
        }
        if(state == BotState.RECRUTER){
            inlineKeyboardRecruter.handlerInlineKeyboard(update, context, replyButtons());
        }
        if(state == BotState.END){
            inlineKeyboardEnd.handlerInlineKeyboard(update,context,replyButtons());
        }
    }
}

