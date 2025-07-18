// src/main/java/biblioteca/util/RuntimeTypeAdapterFactory.java
package biblioteca.util; // Ou biblioteca.dao, se preferir

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.$Gson$Preconditions; // Este import pode ser necessário para algumas versões.
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Adapts JSON objects that contain a specific "type" field to concrete class
 * objects. Register a type adapter for a base type and provide a field name for
 * that type. Then register an adapter for each subtype and its label.
 *
 * <h3>Example</h3>
 * <pre>
 * RuntimeTypeAdapterFactory&lt;Shape&gt; shapeAdapter = RuntimeTypeAdapterFactory.of(Shape.class, "type")
 * .registerSubtype(Rectangle.class, "rectangle")
 * .registerSubtype(Circle.class, "circle");
 * Gson gson = new GsonBuilder().registerTypeAdapterFactory(shapeAdapter).create();
 * </pre>
 * This adapter is useful when the type field is present and you want to use
 * it to map to a concrete class.
 *
 * Based on code from Gson's github samples (commit af8b731e84).
 */
public final class RuntimeTypeAdapterFactory<T> implements TypeAdapterFactory {
    private final Class<?> baseType;
    private final String typeFieldName;
    private final Map<String, Class<?>> labelToSubtype = new LinkedHashMap<String, Class<?>>();
    private final Map<Class<?>, String> subtypeToLabel = new LinkedHashMap<Class<?>, String>();

    private RuntimeTypeAdapterFactory(Class<?> baseType, String typeFieldName) {
        if (typeFieldName == null || baseType == null) {
            throw new NullPointerException("baseType and typeFieldName must be non-null");
        }
        this.baseType = baseType;
        this.typeFieldName = typeFieldName;
    }

    public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType, String typeFieldName) {
        return new RuntimeTypeAdapterFactory<T>(baseType, typeFieldName);
    }

    public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> subtype, String label) {
        if (subtype == null || label == null) {
            throw new NullPointerException("subtype and label must be non-null");
        }
        if (labelToSubtype.containsKey(label) || subtypeToLabel.containsKey(subtype)) {
            throw new IllegalArgumentException("types and labels must be unique");
        }
        labelToSubtype.put(label, subtype);
        subtypeToLabel.put(subtype, label);
        return this;
    }

    @Override
    public <R> TypeAdapter<R> create(Gson gson, TypeToken<R> type) {
        if (type.getRawType() != baseType && !baseType.isAssignableFrom(type.getRawType())) {
            return null; // This factory only handles the base type and its registered subtypes
        }

        final Map<String, TypeAdapter<?>> labelToDelegate = new LinkedHashMap<String, TypeAdapter<?>>();
        final Map<Class<?>, TypeAdapter<?>> subtypeToDelegate = new LinkedHashMap<Class<?>, TypeAdapter<?>>();

        for (Map.Entry<String, Class<?>> entry : labelToSubtype.entrySet()) {
            TypeAdapter<?> delegate = gson.getDelegateAdapter(this, TypeToken.get(entry.getValue()));
            labelToDelegate.put(entry.getKey(), delegate);
            subtypeToDelegate.put(entry.getValue(), delegate);
        }

        return new TypeAdapter<R>() {
            @Override public void write(JsonWriter out, R value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                Class<?> srcType = value.getClass();
                String label = subtypeToLabel.get(srcType);
                if (label == null) {
                    throw new JsonParseException("cannot serialize " + srcType.getName()
                            + "; did you forget to register a subtype for it?");
                }
                JsonObject jsonObject = ((TypeAdapter<R>) subtypeToDelegate.get(srcType)).toJsonTree(value).getAsJsonObject();
                jsonObject.addProperty(typeFieldName, label);
                gson.toJson(jsonObject, out);
            }

            @Override public R read(JsonReader in) throws IOException {
                JsonElement jsonElement = gson.fromJson(in, JsonElement.class);
                JsonElement labelJsonElement = jsonElement.getAsJsonObject().get(typeFieldName);
                if (labelJsonElement == null) {
                    throw new JsonParseException("cannot deserialize " + baseType.getName()
                            + " because it does not define a field named " + typeFieldName);
                }
                String label = labelJsonElement.getAsString();
                Class<?> subtype = labelToSubtype.get(label);
                if (subtype == null) {
                    throw new JsonParseException("cannot deserialize " + baseType.getName() + " subtype named "
                            + label + "; did you forget to register a subtype?");
                }
                return (R) labelToDelegate.get(label).fromJsonTree(jsonElement);
            }
        }.nullSafe();
    }
}