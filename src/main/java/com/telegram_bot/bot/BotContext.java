package com.telegram_bot.bot;

import com.telegram_bot.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BotContext {

    private ChatBot bot;
    private User user;
    private String input;
    private BotState state;

    public static BotContext of(ChatBot bot, User user, String input, BotState state){
        return new BotContext(bot, user, input, state);
    }

    public BotContext(ChatBot bot, User user, String input, BotState state) {
        this.bot = bot;
        this.user = user;
        this.input = input;
        this.state = state;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
