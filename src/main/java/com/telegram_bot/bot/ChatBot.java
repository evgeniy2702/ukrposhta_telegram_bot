package com.telegram_bot.bot;


import com.telegram_bot.Service.ReplyKeyBoardMarkupService;
import com.telegram_bot.Service.SendInlineKeyBoardMessageService;
import com.telegram_bot.Service.SendStateMessageService;
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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
@PropertySource("classpath:telegram.properties")
public class ChatBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private SendStateMessageService sendStateMessageService;
    private SendInlineKeyBoardMessageService sendInlineKeyBoardMessageService;
    private ReplyKeyBoardMarkupService replyKeyBoardMarkupService;
    private InlineKeyboardEnd inlineKeyboardEnd;
    private InlineKeyboardDiraction inlineKeyboardDiraction;
    private InlineKeyboardAction inlineKeyboardAction;
    private InlineKeyboardOffice inlineKeyboardOffice;
    private InlineKeyboardAddress inlineKeyboardAddress;
    private InlineKeyboardRecruter inlineKeyboardRecruter;
    private UserService userService;



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
    public void setSendStateMessageService(SendStateMessageService sendStateMessageService) {
        this.sendStateMessageService = sendStateMessageService;
    }

    @Autowired
    public void setInlineKeyboardEnd(InlineKeyboardEnd inlineKeyboardEnd) {
        this.inlineKeyboardEnd = inlineKeyboardEnd;
    }

    @Autowired
    public void setSendInlineKeyBoardMessageService(SendInlineKeyBoardMessageService sendInlineKeyBoardMessageService) {
        this.sendInlineKeyBoardMessageService = sendInlineKeyBoardMessageService;
    }

    @Autowired
    public void setReplyKeyBoardMarkupService(ReplyKeyBoardMarkupService replyKeyBoardMarkupService) {
        this.replyKeyBoardMarkupService = replyKeyBoardMarkupService;
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
                    state = sendStateMessageService.sendStateMessage(state, context, update);

                    if (sendStateMessageService.keyBoardBoolean) {
                        execute(sendInlineKeyBoardMessageService
                                .sendInlineKeyBoardMessage(update.getMessage().getChatId(), state, context));
                        sendStateMessageService.keyBoardBoolean = false;
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
            inlineKeyboardDiraction.handlerInlineKeyboard(update, context, replyKeyBoardMarkupService.replyButtons());
            execute(sendInlineKeyBoardMessageService.sendInlineKeyBoardMessage(chatId, state, context));
        }
        if (state == BotState.ACTION) {
            inlineKeyboardAction.handlerInlineKeyboard(update, context, replyKeyBoardMarkupService.replyButtons());
            execute(sendInlineKeyBoardMessageService.sendInlineKeyBoardMessage(chatId, state, context));
        }
        if(state == BotState.OFFICE){
            inlineKeyboardOffice.handlerInlineKeyboard(update, context, replyKeyBoardMarkupService.replyButtons());
            execute(sendInlineKeyBoardMessageService.sendInlineKeyBoardMessage(chatId, state, context));
        }
        if(state == BotState.ADDRESS){
            inlineKeyboardAddress.handlerInlineKeyboard(update, context, replyKeyBoardMarkupService.replyButtons());
            execute(sendInlineKeyBoardMessageService.sendInlineKeyBoardMessage(chatId, state, context));
        }
        if(state == BotState.RECRUTER){
            inlineKeyboardRecruter.handlerInlineKeyboard(update, context, replyKeyBoardMarkupService.replyButtons());
        }
        if(state == BotState.END){
            inlineKeyboardEnd.handlerInlineKeyboard(update,context,replyKeyBoardMarkupService.replyButtons());
        }
    }
}

