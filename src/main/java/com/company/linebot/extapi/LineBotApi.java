package com.company.linebot.extapi;

import org.springframework.web.bind.annotation.ResponseBody;

import com.linecorp.bot.model.ReplyMessage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LineBotApi {

    @Headers("Content-Type: application/json")
    @POST("v2/bot/message/reply")
    Call<Object> replyMessage(@Header("Authorization") String authorization, @Body ReplyMessage replyMessage);
}
