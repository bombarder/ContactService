package parserService;

import annotations.CustomDateFormat;
import annotations.JsonValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.Map;

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

        Map<String, String> map = new HashMap<>();
        jsonToMapConverter(json, (HashMap) map);

        T obj = clazz.newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String objectFieldName = field.getName();
            String value = map.get(objectFieldName);

            if (value != null) {
                Class<?> fieldType = field.getType();
                if (fieldType.isAssignableFrom(String.class)) {
                    field.set(obj, value);
                } else if (fieldType.isAssignableFrom(int.class) || fieldType.isAssignableFrom(Integer.class)) {
                    int integerValue = Integer.parseInt(value);
                    field.set(obj, integerValue);
                } else if (fieldType.isAssignableFrom(byte.class) || fieldType.isAssignableFrom(Byte.class)) {
                    byte byteValue = Byte.parseByte(value);
                    field.set(obj, byteValue);
                } else if (fieldType.isAssignableFrom(short.class) || fieldType.isAssignableFrom(Short.class)) {
                    short shortValue = Short.parseShort(value);
                    field.set(obj, shortValue);
                } else if (fieldType.isAssignableFrom(long.class) || fieldType.isAssignableFrom(Long.class)) {
                    long longValue = Long.parseLong(value);
                    field.set(obj, longValue);
                } else if (fieldType.isAssignableFrom(float.class) || fieldType.isAssignableFrom(Float.class)) {
                    float floatValue = Float.parseFloat(value);
                    field.set(obj, floatValue);
                } else if (fieldType.isAssignableFrom(double.class) || fieldType.isAssignableFrom(Double.class)) {
                    double doubleValue = Double.parseDouble(value);
                    field.set(obj, doubleValue);
                } else if (fieldType.isAssignableFrom(LocalDate.class)) {
                    String pattern = field.getAnnotation(CustomDateFormat.class).format();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
                    LocalDate parse = LocalDate.parse(value, dateTimeFormatter);
                    field.set(obj, parse);
                } else {
                    field.set(obj, fromJson(value, fieldType));
                }
            }
        }
        return obj;
    }

    private static String jsonToMapConverter(String json, HashMap map) {

        if (json.startsWith("{")&&json.endsWith("}")){
            return jsonToMapConverter(json.substring(1, json.length()-1), map);
        } else {
            int keyPos = 0;
            while (keyPos < json.length() - 1){
                if (!isColon(json.charAt(keyPos)) && !isComa(json.charAt(keyPos))){
                    keyPos++;
                } else {
                    String key = json.substring(1, keyPos - 1);
                    String value = jsonToMapConverter(json.substring(keyPos + 1), map);
                    map.put(key,value);
                }
            }
        }

//        String jsonWithoutBrackets = json.substring(1, json.length() - 1);
//
//        if (json.contains("{")){
//            String objectString = inputJsonInternalBracketsChecking(jsonWithoutBrackets);
//            String[] dataFromJson = objectString.split(",");
//            for (String key : dataFromJson) {
//                String[] split = key.split(":");
//                map.put(trimString(split[0]), trimString(split[1]));
//            }
//        }
//
//        String[] dataFromJson = jsonWithoutBrackets.split(",");
//        for (String key : dataFromJson) {
//            String[] split = key.split(":");
//            map.put(trimString(split[0]), trimString(split[1]));
//        }
        return json;
    }

    private static boolean isComa(char c) {
        if (c == ','){
            return true;
        } else {
            return false;
        }
    }

    private static boolean isColon(char c) {
        if (c == ':'){
            return true;
        } else {
            return false;
        }
    }

    private static String inputJsonInternalBracketsChecking(String rowJson) {
        int startBracketIndex = rowJson.indexOf('{');
        int closeBracketIndex = rowJson.indexOf('}');
        String targetString = rowJson.substring(startBracketIndex,closeBracketIndex);
        return targetString;
    }

    private static String trimString(String string) {
        String result = string.trim();
        result = result.substring(1, result.length() - 1);
        return result;
    }
}
