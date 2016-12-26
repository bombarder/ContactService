import entity.TestCustomer;
import org.junit.Test;
import parserService.JsonParser;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class fromJsonTest {

    @Test
    public void byteFieldTest() throws Exception {

        TestCustomer expectedCustomer = new TestCustomer();
        expectedCustomer.setAge(30);
        expectedCustomer.setFirstName("John");
        expectedCustomer.setHobby("golf");
        expectedCustomer.setBirthDate(LocalDate.of(2017, 01, 01));
        expectedCustomer.setByteNumber((byte) 2);
        expectedCustomer.setShortNumber((short) 4);
        expectedCustomer.setLongNumber(220);
        expectedCustomer.setFloatNumber(1000f);
        expectedCustomer.setDoubleNumber(2000);

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
