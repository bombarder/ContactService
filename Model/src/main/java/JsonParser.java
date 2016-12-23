import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.Map;

public class JsonParser {
    public static String toJson(Object object) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();

        Class<?> aClass = object.getClass();
        Field[] fields = aClass.getDeclaredFields();
        stringBuilder.append("{");

        for (Field field : fields) {
            field.setAccessible(true);
            boolean firstAnnotationPresent = field.isAnnotationPresent(JsonValue.class);
            boolean secondAnnotationPresent = field.isAnnotationPresent(CustomDateFormat.class);

            if (field.get(object) == null) {
                continue;
            } else if (firstAnnotationPresent) {
                stringBuilder.append("\"" + field.getAnnotation(JsonValue.class).name()
                        + "\"" + ":" + "\"" + field.get(object) + "\",");
            } else if (secondAnnotationPresent) {
                String pattern = field.getAnnotation(CustomDateFormat.class).format();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
                String dateAsString = dateTimeFormatter.format((TemporalAccessor) field.get(object));

                stringBuilder.append("\"" + field.getName()
                        + "\"" + ":" + "\"" + dateAsString + "\",");
            } else {
                stringBuilder.append("\"" + field.getName()
                        + "\"" + ":" + "\"" + field.get(object) + "\",");
            }
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    static <T> void fromJson(String json, Class<T> clazz) throws Exception {
        Map<String, String> map = new HashMap<>();
        String rowJson = json.substring(1, json.length() - 1);
        String[] dataFromJson = rowJson.split(",");
        for (String key : dataFromJson) {
            String[] split = key.split(":");
            map.put(trimString(split[0]), trimString(split[1]));
        }
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String name = field.getName();
            String value = map.get(name);
            if (field.getType() == String.class) {
                field.set(clazz.newInstance(), value);
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate parse = LocalDate.parse(value, formatter);
                field.set(clazz.newInstance(), parse);
            }
        }
    }

    private static String trimString(String string) {
        String result = string.trim();
        result = result.substring(1, result.length() - 1);
        return result;
    }

}
