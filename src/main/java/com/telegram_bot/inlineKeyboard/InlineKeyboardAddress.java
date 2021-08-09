package com.telegram_bot.inlineKeyboard;

import com.telegram_bot.Service.AddressService;
import com.telegram_bot.bot.BotContext;
import com.telegram_bot.bot.BotState;
import com.telegram_bot.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyboardAddress implements InlineKeyboardInterfase{

    private AddressService addressService;

    @Autowired
    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

    @Override
    public SendMessage createInlineKeyBoard(Long chatId, BotContext context) {
        InlineKeyboardMarkup inlineKeyboardMarkup;


        List<InlineKeyboardButton> row = new ArrayList<>();
        List<List<InlineKeyboardButton>> listRow = new ArrayList<>();

        row.add(InlineKeyboardButton.builder()
                .text("ТАК")
                .callbackData("ТАК")
                .build());
        row.add(InlineKeyboardButton.builder()
                .text("НІ")
                .callbackData("НІ")
                .build());

        listRow.add(row);

        inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(listRow)
                .build();

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("Чи плануєте під'їхати ?\r\n")
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }

    @Override
    public void handlerInlineKeyboard(Update update, BotContext context, ReplyKeyboardMarkup replyKeyboardMarkup) throws TelegramApiException {

        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long id = Long.valueOf(callbackQuery.getData().split(":")[1]);
        id--;

        List<Address> addressList = addressService.getAllAddress();
        String address = "Адреса : " + addressList.get(id.intValue()).getAddress() + "\r\n" +
                "Контактні данні : " + addressList.get(id.intValue()).getContact() + "\r\n";

        SendMessage message;
        BotState state = BotState.ADDRESS;
        String sendMessage = "Будь-ласка, адреса та контактні данні обраного вами офісу : \r\n" +
                address;
        message = SendMessage.builder()
                .chatId(String.valueOf(context.getUser().getChatId()))
                .text(sendMessage)
                .replyMarkup(replyKeyboardMarkup)
                .build();
        context.getUser().setStateId(state.ordinal());
        context.getBot().execute(message);
    }
}
