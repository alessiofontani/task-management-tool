package com.alessiofontani.taskmanagementtool.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes= MessageConfig.class)
public class MessageConfigTest {

    @Autowired
    private MessageSource messageSource;


    @Test
    void testMessageSource() {

        String message = messageSource.getMessage("no.locale.properties.test.message",null, Locale.ENGLISH);
        assertEquals(message, "Default test message");

        String messageIT = messageSource.getMessage("language.specific.test.message",null, Locale.ITALIAN);

        assertEquals(messageIT, "Messaggio di test specifico per la lingua italiana");

        String messageEN = messageSource.getMessage("language.specific.test.message",null, Locale.ENGLISH);

        assertEquals(messageEN, "Specific test message for english language");
    }


}
