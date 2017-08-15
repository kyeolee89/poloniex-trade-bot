package util;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.HashMap;
import java.util.Map;

public class CommonUtil {
    public static void sendTelegram(String text) throws UnirestException {
        try {
            Map map = new HashMap();
            map.put("key1", text);

            HttpResponse<String> response = Unirest.post("telegramUrl")
                    .header("content-type", "application/json")
                    .body(new Gson().toJson(map))
                    .asString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

