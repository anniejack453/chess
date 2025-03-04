package handler;
import com.google.gson.Gson;
import model.UserData;
import spark.*;

import java.util.Map;

public class encoderDecoder<T> {
    public T decode(Request req, Class<T> tClass){
        return new Gson().fromJson(req.body(), tClass);
    }

    public String decodeHeader(Request req, Class<T> tClass){
        String authToken = req.headers("authorization");
        return authToken;
    }

    public String encode(T obj){
        return new Gson().toJson(obj);
    }
}
