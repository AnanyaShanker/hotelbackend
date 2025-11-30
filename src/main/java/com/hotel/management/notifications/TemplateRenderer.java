package com.hotel.management.notifications;



import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TemplateRenderer {

    public String render(String templateFileName, Map<String, Object> values) throws Exception {
        ClassPathResource res = new ClassPathResource("email-templates/" + templateFileName);
        if (!res.exists()) throw new IllegalArgumentException("Template not found: " + templateFileName);

        String raw;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8))) {
            raw = br.lines().collect(Collectors.joining("\n"));
        }

        String out = raw;
        if (values != null) {
            for (Map.Entry<String, Object> e : values.entrySet()) {
                String placeholder = "{{" + e.getKey() + "}}";
                String val = e.getValue() == null ? "" : e.getValue().toString();
                out = out.replace(placeholder, escapeHtml(val));
            }
        }
        return out;
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}

