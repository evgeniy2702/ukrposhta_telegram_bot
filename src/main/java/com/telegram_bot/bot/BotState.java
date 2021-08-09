package com.telegram_bot.bot;


public enum  BotState {

    START("START"), START2("START2"), CALLNAME("CALLNAME"),
    DIRACTION("DIRACTION"), ACTION("ACTION"), OFFICE("OFFICE"),
    ADDRESS("ADDRESS"), RECRUTER("RECRUTER"), APPROVED("APPROVED"), END("END");

    private static BotState[] states;
    private final String name;

    BotState(){
        this.name = "START";
    }


    BotState(String  name){
        this.name = name;
    }

    public static  BotState getInitialState(){
        return byId(0);
    }

    public static BotState byId(int id) {

        if(states == null){
            states = BotState.values();
        }
        return states[id];
    }
}
