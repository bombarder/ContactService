import entity.Customer;
import parserService.JsonParser;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws Exception {
        Customer customer = new Customer();
        customer.setLastName("Petrov");
        customer.setFirstName("Ivan");
        customer.setHobby("hockey");
        customer.setBirthDate(LocalDate.of(2000, 9, 12));
        customer.setAge(35);

//        String s = JsonParser.toJson(customer);
//        System.out.println(s);

        //language=JSON
        String message = "{\"firstName\":\"Vova\"}";
        JsonParser.fromJson(message, Customer.class);
    }
}
