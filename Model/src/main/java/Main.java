import java.lang.reflect.Field;
import java.time.LocalDate;

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
            boolean annotationPresent = field.isAnnotationPresent(JsonValue.class);
            if (annotationPresent) {
                field.set(object, field.getAnnotation(JsonValue.class).name());
            }
            stringBuilder.append("\""+ field.getName() +"\""+":"+ "\""+field.get(object)+"\",");
        }
        stringBuilder.setLength(stringBuilder.length()-1);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
