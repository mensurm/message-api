package com.skf.messaging.controller.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@NoArgsConstructor
@Data
public class MessageRequest implements Serializable {

    private static final long serialVersionUID = 88969322327841944L;
    private Map<String, String> content;
}
