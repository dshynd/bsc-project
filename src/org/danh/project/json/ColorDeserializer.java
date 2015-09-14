package org.danh.project.json;

import java.awt.Color;
import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class ColorDeserializer implements JsonDeserializer<Color> {
	public Color deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		if (json.isJsonArray()) {
			return convert(json.getAsJsonArray());
		}
		if ((json.isJsonObject()) && (json.getAsJsonObject().has("color")) && (json.getAsJsonObject().get("color").isJsonArray())) {
			JsonArray array = json.getAsJsonObject().getAsJsonArray("color");
			return convert(array);
		}
		return null;
	}

	Color convert(JsonArray array) {
		Color color;
		switch (array.size()) {
		case 3:
			color = new Color(array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt());
			break;
		case 4:
			color = new Color(array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt(), array.get(3).getAsInt());
			break;
		default:
			color = null;
		}
		return color;
	}
}