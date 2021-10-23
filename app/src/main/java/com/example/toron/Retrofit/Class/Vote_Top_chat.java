package com.example.toron.Retrofit.Class;

public class Vote_Top_chat {
    String chat_side;
    String chat_idx;
    String chat_content;

    public String getChat_side() {
        return chat_side;
    }

    public void setChat_side(String chat_side) {
        this.chat_side = chat_side;
    }

    public String getChat_idx() {
        return chat_idx;
    }

    public void setChat_idx(String chat_idx) {
        this.chat_idx = chat_idx;
    }

    public String getChat_content() {
        return chat_content;
    }

    public void setChat_content(String chat_content) {
        this.chat_content = chat_content;
    }

    public Vote_Top_chat(String chat_side, String chat_idx, String chat_content) {
        this.chat_side = chat_side;
        this.chat_idx = chat_idx;
        this.chat_content = chat_content;
    }

    @Override
    public String toString() {
        return "Vote_Top_chat{" +
                "chat_side='" + chat_side + '\'' +
                ", chat_idx='" + chat_idx + '\'' +
                ", chat_content='" + chat_content + '\'' +
                '}';
    }
}
