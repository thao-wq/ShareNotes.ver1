package common;

import java.io.Serializable;

public class NoteCommand implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String UPDATE = "UPDATE";
    public static final String UNDO = "UNDO";

    private String type;
    private String content;

    public NoteCommand(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}