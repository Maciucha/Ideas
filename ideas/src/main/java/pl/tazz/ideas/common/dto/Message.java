package pl.tazz.ideas.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private String title;
    private String content;
    private String type;

    public static Message info(String msg) {
        return new Message("Info", msg, "info");
    }

    public static Message error(String msg) {
        return new Message("Error", msg, "error");
    }
}
