package macbury.pod.extensions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by macbury on 10.09.14.
 */
public class DateDeserializer implements JsonDeserializer<Date> {
  private final SimpleDateFormat dateFormat;

  public DateDeserializer() {
    dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
  }
  @Override
  public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    try {
      return dateFormat.parse(json.getAsString());
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }
}
