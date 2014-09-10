package macbury.enklawa.extensions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by macbury on 10.09.14.
 */
public class DateDeserializer implements JsonDeserializer<Date> {
  @Override
  public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    return new Date(json.getAsInt() * 1000);
  }
}
