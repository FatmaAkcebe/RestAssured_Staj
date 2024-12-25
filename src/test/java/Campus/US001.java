package Campus;

import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

public class US001 extends Parent {

    Faker randomUreteci = new Faker();
    String rndpassword="";

    @Test()
    public void InvalidLogin(){

        rndpassword = randomUreteci.number().digits(5);

        Map<String, String> userCredential = new HashMap<>();
        userCredential.put("username", "turkeyts");
        userCredential.put("password",rndpassword );

        given()
                .spec(reqSpec)
                .body(userCredential)

                .when()
                .post("auth/login")

                .then()
                .log().body()
                .statusCode(401)

        ;
    }

}
