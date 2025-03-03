package handler;
import com.google.gson.Gson;
import model.UserData;
import spark.*;

public class encoderDecoder<T> {
    public T decode(Request req, Class<T> tClass){
        return new Gson().fromJson(req.body(), tClass);
    }

    public T decodeHeader(Request req, Class<T> tClass){
        return new Gson().fromJson(req.headers("/Authorization"), tClass);
    }

    public String encode(T obj){
        return new Gson().toJson(obj);
    }
}
