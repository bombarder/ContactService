import java.lang.reflect.Field;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws Exception {
        Customer customer = new Customer();
        customer.setLastName("Petrov");
        customer.setFirstName("Ivan");
        customer.setHobby("hockey");
        customer.setBirthDate(LocalDate.of(2000, 9, 12));

        toJson(customer);
    }

    public static String toJson(Object object) throws Exception {
        Class<?> aClass = object.getClass();
        Field[] fields = aClass.getDeclaredFields();
        System.out.println("{");
        for (Field field: fields) {
            boolean annotationPresent = field.isAnnotationPresent(JsonValue.class);
            if (annotationPresent) {

            }

            field.setAccessible(true);
            System.out.println("\""+ field.getName() +"\""+":"+ "\""+field.get(object)+"\",");
        }
        System.out.println("}");
        return null;
    }
}
