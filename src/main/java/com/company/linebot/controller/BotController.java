package com.company.linebot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import javax.xml.ws.handler.MessageContext;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.company.linebot.extapi.LineBotApi;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.ImagemapMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.VideoMessage;
import com.linecorp.bot.model.message.imagemap.ImagemapAction;
import com.linecorp.bot.model.message.imagemap.ImagemapArea;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.imagemap.MessageImagemapAction;
import com.linecorp.bot.model.message.imagemap.URIImagemapAction;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.message.template.Template;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@RestController
@RequestMapping("callback")
public class BotController {

    static final String X_LINE_SIGNATURE = "X-Line-Signature";

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

        // process each event
        List<Event> events = request.getEvents();
        for (Event event: events) {
            // message event
            if (event instanceof MessageEvent) {
                MessageContent messageContent = ((MessageEvent) event).getMessage();
                if (messageContent instanceof TextMessageContent) {
                    // get text
                    String text = ((TextMessageContent) messageContent).getText();
                    List<Message> messages = new ArrayList<Message>();

                    RestTemplate rt = new RestTemplate();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.set("Authorization", "Bearer " + ACCESS_TOKEN);

                    if (text.equals("image")) {
                        // send a reply image message
                        String url = "https://upload.wikimedia.org/wikipedia/commons/5/5e/Line_logo.png";
                        messages.add(new ImageMessage(url, url));

                    } else if (text.equals("sticker")) {
                        // send a reply image message
                        String packageId = "1";
                        String stickerId = "2";
                        messages.add(new StickerMessage(packageId, stickerId));

                    } else if (text.equals("video")) {
                        String originalContentUrl = "https://youtu.be/GCgvpwLNvtY";
                        String previewImageUrl = "https://i.ytimg.com/vi/GCgvpwLNvtY/hqdefault.jpg?sqp=-oaymwEZCNACELwBSFXyq4qpAwsIARUAAIhCGAFwAQ==&rs=AOn4CLAXDicinxI89b7ut41cg5Gw6b6-cA";
                        messages.add(new VideoMessage(originalContentUrl, previewImageUrl));

                    } else if (text.equals("imagemap")) {
                        String baseUrl = "https://s3-ap-southeast-1.amazonaws.com/derek-internal-demo/images/rich";
                        String altText = "This is an imagemap";
                        ImagemapBaseSize baseSize = new ImagemapBaseSize(1040, 1040);
                        List<ImagemapAction> actions = new ArrayList<ImagemapAction>();
                        actions.add(new URIImagemapAction("https://www.youtube.com", new ImagemapArea(0, 0,520, 520)));
                        actions.add(new URIImagemapAction("https://www.facebook.com", new ImagemapArea(520, 0,520, 520)));
                        actions.add(new MessageImagemapAction("Hello", new ImagemapArea(0, 520, 520, 520)));
                        actions.add(new MessageImagemapAction("Welcome", new ImagemapArea(520, 520, 520, 520)));
                        messages.add(new ImagemapMessage(baseUrl, altText, baseSize, actions));

                    } else if (text.equals("confirm")) {
                        String altText = "This is a confirm";
                        ConfirmTemplate template = new ConfirmTemplate(
                                "Do it?",
                                new MessageAction("Yes", "Yes!"),
                                new MessageAction("No", "No!")
                        );
                        messages.add(new TemplateMessage(altText, template));


                    } else if (text.equals("buttons")) {

                        String imageUrl = "https://s3-ap-southeast-1.amazonaws.com/derek-internal-demo/images/buttons/1040.jpg";
                        ButtonsTemplate buttonsTemplate = new ButtonsTemplate(
                                imageUrl,
                                "My button sample",
                                "Hello, my button",
                                Arrays.asList(
                                        new URIAction("Go to line.me",
                                                      "https://line.me"),
                                        new PostbackAction("Say hello1",
                                                           "hello こんにちは"),
                                        new PostbackAction("言 hello2",
                                                           "hello こんにちは",
                                                           "hello こんにちは"),
                                        new MessageAction("Say message",
                                                          "Rice=米")
                                ));
                        messages.add(new TemplateMessage("Button alt text", buttonsTemplate));

                    } else {
                        // send a reply text message
                        messages.add(new TextMessage("I don't understand \"" + text + "\""));
                    }

                    ReplyMessage replyMsg = new ReplyMessage(((MessageEvent) event).getReplyToken(), messages);
                    HttpEntity<ReplyMessage> entity = new HttpEntity<ReplyMessage>(replyMsg, headers);

                    String url = "https://api.line.me/v2/bot/message/reply";
                    rt.postForObject(url, entity, String.class);
                }
            }
            // postback event
            if (event instanceof PostbackEvent) {
                System.out.println("=== postback event ===");

            }
        }

        /*
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.line.me/")
                //.addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .client(httpClient.build())
                .build();

        LineBotApi lineBotApi = retrofit.create(LineBotApi.class);


        // process each event
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

                    System.out.println("=== 111 ===");
                    System.out.println(((MessageEvent) event).getReplyToken());

                    // send a reply message
                    ReplyMessage replyMessage = new ReplyMessage(((MessageEvent) event).getReplyToken(), messages);
                    String authorization = "Bearer " + ACCESS_TOKEN;
                    //Call call = lineBotApi.replyMessage(authorization, replyMessage);
                    Call call = lineBotApi.replyMessage2(replyMessage);
                    call.enqueue(new Callback() {
                        public void onResponse(Call call, retrofit2.Response response) {
                            System.out.println("=== success ===");
                            System.out.println(response);
                        }

                        public void onFailure(Call call, Throwable throwable) {
                            System.out.println("=== failure ===");

                        }
                    });

                }
            }
        }
        */

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
