package Campus;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class US102 extends Parent{

    Faker randomUreteci = new Faker();
    String cityName = "";
    String countryCode = "";
    String citiyId = "";
    String StateId= "";
    String countryName= "";
    String countryId= "";


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

    @Test(dependsOnMethods = "CreateCountry")
    public void AddCities() {
        cityName = randomUreteci.address().cityName();

        Map<String, Object> createCities = new HashMap<>();
        createCities.put("name", cityName);
        createCities.put("country", Map.of("id",countryId));
        createCities.put("translateName", new Object[]{});


        Response response = given()
                .spec(reqSpec)
                .body(createCities)
                .when()
                .post("school-service/api/cities")
                .then()
                .log().body()
                .statusCode(201)
                .extract().response();

        citiyId = response.path("id");
        cityName = response.path("name");
        ;
    }

    @Test(dependsOnMethods = "AddCities")
    public void EditCities() {

        Map<String, Object> countryData = new HashMap<>();
        countryData.put("id", countryId);

        Map<String, Object> updateCities= new HashMap<>();
       updateCities.put("id", citiyId);
       updateCities.put("name", randomUreteci.address().firstName());
       updateCities.put("country", countryData);
       updateCities.put("stateId", StateId);
       updateCities.put("translateName", new Object[]{});


        given()
                .spec(reqSpec)
                .body(updateCities)
                .when()
                .put("school-service/api/states/")
                .then()
                .log().body()
                .statusCode(200);
    }



    @Test(dependsOnMethods = "EditCities")
    public void deleteCities() {
        given()
                .spec(reqSpec)
                .pathParam("citiyId", citiyId)
                .log().uri()

                .when()
                .delete("school-service/api/states/{citiyId}")

                .then()
                .log().body()
                .statusCode(200);

        System.out.println("Şehir silindi. ID: " + citiyId);
    }

}
