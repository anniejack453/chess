package handler;
import com.google.gson.Gson;
import spark.*;

public class EncoderDecoder<T> {
    public T decode(Request req, Class<T> tClass){
        return new Gson().fromJson(req.body(), tClass);
    }

    public String decodeHeader(Request req, Class<T> tClass){
        return req.headers("authorization");
    }

    public String encode(T obj){
        return new Gson().toJson(obj);
    }
}
