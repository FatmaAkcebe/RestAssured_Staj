package Campus;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class US101 extends Parent {

    Faker randomUreteci = new Faker();
    String countryName = "";
    String countryCode = "";
    String countryId = "";
    String stateName = "";
    String stateShortName = "";
    String StateId = "";

    @Test
    public void CreateCountry() {
        countryName = randomUreteci.address().country() + randomUreteci.number().digits(5);
        countryCode = randomUreteci.address().countryCode() + randomUreteci.number().digits(5);

        Map<String, String> createCountry = new HashMap<>();
        createCountry.put("name", countryName);
        createCountry.put("code", countryCode);
        createCountry.put("hasState", "true");
        createCountry.put("translateName", null);


        countryId =
                given()
                        .spec(reqSpec)
                        .body(createCountry)

                        .when()
                        .post("school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
        ;
    }


    @Test
    public void ListState() {
        given()
                .spec(reqSpec)

                .when()
                .get("school-service/api/states")

                .then()
                .log().all()
                .statusCode(200)
                .time(lessThan(2000L))
        ;
    }


    @Test(dependsOnMethods = "CreateCountry")
    public void AddState() {
        stateName = randomUreteci.address().streetName();
        stateShortName = randomUreteci.address().firstName();

        Map<String, Object> createState = new HashMap<>();
        createState.put("name", stateName);
        createState.put("shortName", stateShortName);
        createState.put("country", Map.of("id", countryId));
        createState.put("translateName", new Object[]{});

        Response response = given()
                .spec(reqSpec)
                .body(createState)
                .when()
                .post("school-service/api/states")
                .then()
                .log().body()
                .statusCode(201)
                .extract().response();

        StateId = response.path("id");
        stateName = response.path("name");
        ;
    }

    @Test(dependsOnMethods = "AddState")
    public void EditState() {


        Map<String, Object> countryData = new HashMap<>();
        countryData.put("id", countryId);

        Map<String, Object> updateState = new HashMap<>();
       updateState.put("id", StateId);
       updateState.put("name", stateName);
       updateState.put("shortName", randomUreteci.address().firstName());
       updateState.put("country", countryData);
       updateState.put("translateName", new Object[]{});


        given()
                .spec(reqSpec)
                .body(updateState)
                .when()
                .put("school-service/api/states/")
                .then()
                .log().body()
                .statusCode(200);
    }


    @Test(dependsOnMethods = "EditState")
    public void deleteState() {
        given()
                .spec(reqSpec)
                .pathParam("StateId", StateId)
                .log().uri()

                .when()
                .delete("school-service/api/states/{StateId}")

                .then()
                .log().body()
                .statusCode(200);

        System.out.println("Eyalet silindi.ID: " + StateId);
    }


}
