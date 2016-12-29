package parserService;

import annotations.CustomDateFormat;
import annotations.JsonValue;
import org.apache.commons.lang3.mutable.MutableInt;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;

public class JsonParser {

    public static String toJson(Object object) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");

        Class<?> aClass = object.getClass();
        Field[] fields = aClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            if (field.get(object) != null) {
                Annotation[] annotations = field.getDeclaredAnnotations();
                if (annotations.length > 0) {
                    for (Annotation annotation : annotations) {
                        boolean jsonAnnotation = annotation.annotationType().equals(JsonValue.class);
                        boolean customDateFormatAnnotation = annotation.annotationType().equals(CustomDateFormat.class);
                        if (jsonAnnotation) {
                            stringBuilder.append("\"" + field.getAnnotation(JsonValue.class).name()
                                    + "\"" + ":" + "\"" + field.get(object) + "\",");
                        } else {
                            if (customDateFormatAnnotation) {
                                String pattern = field.getAnnotation(CustomDateFormat.class).format();
                                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
                                String dateAsString = dateTimeFormatter.format((TemporalAccessor) field.get(object));
                                stringBuilder.append("\"" + field.getName() + "\"" + ":" + "\"" + dateAsString + "\",");
                            }
                        }
                    }
                } else {
                    stringBuilder.append("\"" + field.getName() + "\"" + ":" + "\"" + field.get(object) + "\",");
                }
            }
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static <T> T fromJson(String json, Class<T> clazz) throws Exception {

        HashMap map = jsonToMapConverter(json);

        T externalClassInstance = clazz.newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String objectFieldName = field.getName();
            String value = (String) map.get(objectFieldName);

            if (value != null) {
                Class<?> fieldType = field.getType();
                if (fieldType.isAssignableFrom(String.class)) {
                    field.set(externalClassInstance, value);
                } else if (fieldType.isAssignableFrom(int.class) || fieldType.isAssignableFrom(Integer.class)) {
                    int integerValue = Integer.parseInt(value);
                    field.set(externalClassInstance, integerValue);
                } else if (fieldType.isAssignableFrom(byte.class) || fieldType.isAssignableFrom(Byte.class)) {
                    byte byteValue = Byte.parseByte(value);
                    field.set(externalClassInstance, byteValue);
                } else if (fieldType.isAssignableFrom(short.class) || fieldType.isAssignableFrom(Short.class)) {
                    short shortValue = Short.parseShort(value);
                    field.set(externalClassInstance, shortValue);
                } else if (fieldType.isAssignableFrom(long.class) || fieldType.isAssignableFrom(Long.class)) {
                    long longValue = Long.parseLong(value);
                    field.set(externalClassInstance, longValue);
                } else if (fieldType.isAssignableFrom(float.class) || fieldType.isAssignableFrom(Float.class)) {
                    float floatValue = Float.parseFloat(value);
                    field.set(externalClassInstance, floatValue);
                } else if (fieldType.isAssignableFrom(double.class) || fieldType.isAssignableFrom(Double.class)) {
                    double doubleValue = Double.parseDouble(value);
                    field.set(externalClassInstance, doubleValue);
                } else if (fieldType.isAssignableFrom(LocalDate.class)) {
                    String pattern = field.getAnnotation(CustomDateFormat.class).format();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
                    LocalDate parse = LocalDate.parse(value, dateTimeFormatter);
                    field.set(externalClassInstance, parse);
                } else {
                    field.set(externalClassInstance, fromJson(value, fieldType));
                }
            }
        }
        return externalClassInstance;
    }

    private static HashMap jsonToMapConverter(String json) {

        HashMap<String, Object> map = new HashMap<>();

        String key = "";
        String value = "";
        boolean inKey = false;
        boolean inValue = false;

        MutableInt counter = new MutableInt();

        while (counter.getValue() < json.length()) {
            if (json.charAt(counter.getValue()) == '{') {
                counter.increment();
            } else if (json.charAt(counter.getValue()) == ':') {
                counter.increment();
                if (json.charAt(counter.getValue()) == '\"') {
                    inValue = !inValue;
                    counter.increment();
                    while (inValue) {
                        value += json.charAt(counter.getValue());
                        counter.increment();
                        if (json.charAt(counter.getValue()) == '\"') {
                            break;
                        }
                    }
                }
                counter.increment();
            } else if (json.charAt(counter.getValue()) == '\"') {
                inKey = !inKey;
                counter.increment();
                while (inKey) {
                    key += json.charAt(counter.getValue());
                    counter.increment();
                    if (json.charAt(counter.getValue()) == '\"') {
                        break;
                    }
                }
            } else {
                counter.increment();
            }
        }
        map.put(key,value);
        return map;
    }
}

