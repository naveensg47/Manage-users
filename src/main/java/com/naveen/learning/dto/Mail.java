package com.naveen.learning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mail {
    private String to;
    private String from;
    private String subject;
    private String content;
    private Map<String, String> model;
}
