import client.ScooterOrderSteps;
import model.CancelOrderModel;
import model.CreateOrderModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    ScooterOrderSteps orderSteps;

    String[] colors;
    List<String> tracks;

    public CreateOrderTest(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[][] getScooterColor() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {null},
        };
    }

    @Before
    public void setUp() {
        orderSteps = new ScooterOrderSteps();
        tracks = new ArrayList<>();
    }

    @After
    public void tearDown() {
        for (String track : tracks) {
            orderSteps.cancelOrder(new CancelOrderModel(track));
        }
    }

    @Test

    public void createNewOrderTest() {

        CreateOrderModel createOrderScooter = CreateOrderModel.getRandom(colors);
        String track = orderSteps.createNewOrder(createOrderScooter)
                .assertThat().body("track", notNullValue())
                .and()
                .statusCode(201)
                .extract().body().path("track").toString();
        tracks.add(track);
    }
}



