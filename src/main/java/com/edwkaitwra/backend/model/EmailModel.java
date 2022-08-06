package com.edwkaitwra.backend.model;

import lombok.Data;

import java.util.Map;

@Data
public class EmailModel {
    private String to;
    private String from;
    private String subject;
    private String template;
    private Map<String, Object> properties;
}
