package com.challenge.jorgebarreto.notifications.core.application.template;

import java.util.Map;

public class MessageTemplateEngine {

    /**
     * Reemplaza variables del tipo {{key}} usando el mapa de atributos.
     */
    public String render(String template, Map<String, Object> attributes) {

        if (template == null || attributes == null || attributes.isEmpty()) {
            return template;
        }

        String result = template;

        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            result = result.replace(placeholder, String.valueOf(entry.getValue()));
        }

        return result;
    }
}