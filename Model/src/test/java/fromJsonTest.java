import entity.TestCustomer;
import org.junit.Test;
import parserService.JsonParser;

import static org.junit.Assert.assertEquals;

public class fromJsonTest {

    @Test
    public void byteFieldTest() throws Exception {

        TestCustomer expectedCustomer = new TestCustomer();
        expectedCustomer.setAge(30);

        String byteMessage = "{\n" +
                "  \"age\": \"30\"\n" +
                "}";

        JsonParser jsonParser = new JsonParser();
        TestCustomer testCustomer = jsonParser.fromJson(byteMessage, TestCustomer.class);
        assertEquals(expectedCustomer ,testCustomer);
    }
}
