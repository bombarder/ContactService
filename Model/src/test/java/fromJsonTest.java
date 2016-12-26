import entity.TestCustomer;
import org.junit.Test;
import parserService.JsonParser;

import static org.junit.Assert.assertEquals;

public class fromJsonTest {

    @Test
    public void byteFieldTest() throws Exception {

        TestCustomer expectedCustomer = new TestCustomer();
        expectedCustomer.setAge(30);

        //language=JSON
        String testMessage = "{\n" +
                "\"firstName\": \"John\",\n" +
                "\"hobby\": \"golf\",\n" +
                "\"birthDate\": \"01-01-2017\", \n" +
                "\"age\": \"30\",\n" +
                "\"byteNumber\": \"2\",\n" +
                "\"shortNumber\": \"4\",\n" +
                "\"longNumber\": \"220\",\n" +
                "\"floatNumber\":\"1000f\",\n" +
                "\"doubleNumber\":\"2000d\"\n" +
                "}";

        JsonParser jsonParser = new JsonParser();
        TestCustomer testCustomer = jsonParser.fromJson(testMessage, TestCustomer.class);
        assertEquals(expectedCustomer ,testCustomer);
    }
}
