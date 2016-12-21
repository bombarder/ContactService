import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        Customer customer = new Customer();
        customer.setLastName("Petrov");
        customer.setFirstName("Ivan");
        customer.setHobby("hockey");
        customer.setBirthDate(LocalDate.of(2000, 9, 12));

        String s = toJson(customer);
        System.out.println(s);

        //language=JSON
        String message = "{\n" +
                "\"firstName\": \"Vova\",\n" +
                "\"lastName\": \"Stepanov\",\n" +
                "\"hobby\": \"polo\",\n" +
                "\"birthDate\": \"28-04-1996\"\n" +
                "}";
        fromJson(message, Customer.class);
    }

    private static <T> void fromJson(String json, Class<T> clazz) throws Exception {
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
                //formatter = formatter.withLocale(Locale.US);
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

    private static void firstLastSymbolRemove(Map<String, String> map, String[] element, int i) {
        for (int j = 0; j < element.length; j++) {
            String keyString = element[i].replaceAll("[^\\w\\s]", "");
            String valueString = element[i + 1].replaceAll("[^\\w\\s]", "");
            map.put(keyString, valueString);
        }
    }

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
}
