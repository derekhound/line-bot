package com.company.linebot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.handler.MessageContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

@RestController
@RequestMapping("callback")
public class BotController {

    static final String X_LINE_SIGNATURE = "X-Line-Signature";
    static final String ACCESS_TOKEN = "");

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void callback(@RequestHeader(X_LINE_SIGNATURE) String signature,
                         @RequestBody String requestBody,
                         HttpServletResponse response) {

        // json to object
        CallbackRequest request = convert(requestBody);
        if (request == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


        List<Event> events = request.getEvents();
        for (Event event: events) {
            // message event
            if (event instanceof MessageEvent) {
                MessageContent messageContent = ((MessageEvent) event).getMessage();
                if (messageContent instanceof TextMessageContent) {
                    // get text
                    String text = ((TextMessageContent) messageContent).getText();
                    List<Message> messages = new ArrayList<Message>();
                    messages.add(new TextMessage(text));

                    // send a reply message
                    RestTemplate rt = new RestTemplate();

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.set("Authorization", "Bearer " + ACCESS_TOKEN);

                    ReplyMessage replyMsg = new ReplyMessage(((MessageEvent) event).getReplyToken(), messages);
                    HttpEntity<ReplyMessage> entity = new HttpEntity<ReplyMessage>(replyMsg, headers);

                    String url = "https://api.line.me/v2/bot/message/reply";
                    rt.postForObject(url, entity, String.class);
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }

    private CallbackRequest convert(String requestBody) {
        CallbackRequest request = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

            request  = objectMapper.readValue(requestBody, new TypeReference<CallbackRequest>() {});
        } catch (IOException e) {
            System.out.println(e);
        }
        return request;
    }

}
