package com.advancedraidtracker.utility;

import com.google.gson.*;

import java.awt.*;
import java.lang.reflect.Type;

public class JsonTypeAdapters
{
	//java 17 and later does not allow gson to use reflection to access private member fields so custom serializers are used
    public static class ColorDeserializer implements JsonDeserializer<Color>
    {
        @Override
        public Color deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
        {
            JsonObject json = jsonElement.getAsJsonObject();
            int red = json.get("red").getAsInt();
            int green = json.get("green").getAsInt();
            int blue = json.get("blue").getAsInt();
            int opacity = json.get("opacity").getAsInt();
            return new Color(red, green, blue, opacity);
        }
    }

    public static class ColorSerializer implements JsonSerializer<Color>
    {
        @Override
        public JsonElement serialize(Color color, Type colorType, JsonSerializationContext context)
        {
            JsonObject json = new JsonObject();
            json.addProperty("red", color.getRed());
            json.addProperty("green", color.getGreen());
            json.addProperty("blue", color.getBlue());
            json.addProperty("opacity", color.getAlpha());
            return json;
        }
    }
}
