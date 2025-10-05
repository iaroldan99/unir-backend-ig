package com.uade.seminario.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Modelo para mensajes entrantes desde Instagram
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IncomingMessage {

    @JsonProperty("object")
    private String object;

    @JsonProperty("entry")
    private List<Entry> entry;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Entry {
        private String id;
        private Long time;
        private List<Messaging> messaging;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Messaging {
        private Sender sender;
        private Recipient recipient;
        private Long timestamp;
        private Message message;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sender {
        private String id;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Recipient {
        private String id;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        private String mid;
        private String text;
        private List<Attachment> attachments;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attachment {
        private String type;
        private Payload payload;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Payload {
        private String url;
    }
}

