import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
        fromJson(s,Customer.class);
    }

    private static <T> void fromJson(String json, Class<T> clazz) throws Exception {
        Map<String, String> map = new HashMap<>();
        String rowJson = json.substring(1, json.length()-1);
        String[] dataFromJson = rowJson.split(",");
        for (String key:dataFromJson) {
            String[]element = key.split(":");
            for (int i = 0; i < element.length; i = i + 2) {
                String keyString = element[i].substring(1, element[i].length() - 1);
                String valueString = element[i + 1].substring(1, element[i + 1].length() - 1);
                map.put(keyString, valueString);
            }
        }
            for (Field field:clazz.getDeclaredFields()){
                field.setAccessible(true);
                String value = map.get(field.getName());
                if (field.getType() == String.class){
                    field.set(clazz.newInstance(), value);
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    formatter = formatter.withLocale(Locale.US );
                    field.set(clazz.newInstance(), LocalDate.parse(value, formatter));
                }
            }
        }

    public static String toJson(Object object) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();

        Class<?> aClass = object.getClass();
        Field[] fields = aClass.getDeclaredFields();
        stringBuilder.append("{");

        for (Field field: fields) {
            field.setAccessible(true);
            boolean firstAnnotationPresent = field.isAnnotationPresent(JsonValue.class);
            boolean secondAnnotationPresent = field.isAnnotationPresent(CustomDateFormat.class);

            if (field.get(object) == null){
                continue;
            } else if (firstAnnotationPresent) {
                stringBuilder.append("\"" + field.getAnnotation(JsonValue.class).name()
                        + "\"" + ":" + "\"" + field.get(object) + "\",");
            } else if (secondAnnotationPresent){
                SimpleDateFormat sdf = new SimpleDateFormat(field.getAnnotation(CustomDateFormat.class).format());
                Date c = sdf.parse(field.get(object).toString());
                String date = sdf.format(c);
                stringBuilder.append("\"" + field.getName()
                        + "\"" + ":" + "\"" + date + "\",");
            } else {
                stringBuilder.append("\"" + field.getName()
                        + "\"" + ":" + "\"" + field.get(object) + "\",");
            }
        }
        stringBuilder.setLength(stringBuilder.length()-1);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
