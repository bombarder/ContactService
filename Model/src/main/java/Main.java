import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws Exception {
        Customer customer = new Customer();
        customer.setLastName("Petrov");
        customer.setFirstName("Ivan");
        customer.setHobby("hockey");
        customer.setBirthDate(LocalDate.of(2000, 9, 12));

        String s = toJson(customer);
        System.out.println(s);
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

            if (firstAnnotationPresent) {
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
