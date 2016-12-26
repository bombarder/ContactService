import entity.Customer;
import org.junit.Test;
import parserService.JsonParser;

import static org.junit.Assert.assertEquals;

public class fromJsonTest {

    @Test
    public void byteFieldTest() throws Exception {

        Customer expectedCustomer = new Customer();
        expectedCustomer.setAge(30);

        String byteMessage = "{\n" +
                "  \"age\": \"30\"\n" +
                "}";

        JsonParser jsonParser = new JsonParser();
        Customer customer = jsonParser.fromJson(byteMessage, Customer.class);
        assertEquals(expectedCustomer ,customer);
    }
}
