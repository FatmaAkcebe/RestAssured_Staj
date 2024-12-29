package Campus;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class US108 extends Parent{
    String schoolId="6576fd8f8af7ce488ac69b89";
    String eduStdName="";
    String eduStdDescription="";
    String eduStdID="";

    @Test
    public void ListEducationStandart(){


        Map<String, String> ListEduStds = new HashMap<>();
        ListEduStds.put("schoolId", schoolId);


        given()
                .spec(reqSpec)
                .body(ListEduStds)
                .log().uri()
                //.param("page",0)
                //.param("size",10)

                .when()
                .post("school-service/api/education-standard/search?page=0&size=10")


                .then()
                //.log().body()
                .statusCode(200)
                .body("size",equalTo(10))
                //.body("content.subjectName",containsStringIgnoringCase("Mathematics"))
                ;
    }
    @Test(dependsOnMethods = "ListEducationStandart")
    public void CreateEducationStandart(){
        eduStdName="biyolojiGrubu"+randomUreteci.number().digits(5);
        eduStdDescription="biyolojiGrubu_Açıklama"+randomUreteci.number().digits(5);


        Map<String, String> CreateEduStds = new HashMap<>();
        CreateEduStds.put("name", eduStdName);
        CreateEduStds.put("description", eduStdDescription);
        CreateEduStds.put("schoolId", schoolId);
        CreateEduStds.put("gradeLevelId", "654898fae70d9e34a8331e51");
        CreateEduStds.put("subjectId", "657704ff8af7ce488ac69b9e");
        CreateEduStds.put("packageId", "6761dcbca7945338bd1ff0a7");

        eduStdID=
        given()
                .spec(reqSpec)
                .body(CreateEduStds)
                .log().uri()

                .when()
                .post("school-service/api/education-standard")


                .then()
                .log().body()
                .statusCode(201)
                .body("name",equalTo(eduStdName))
                .body("description",equalTo(eduStdDescription))
                .extract().path("id")
        ;
        System.out.println("eduStdID = " + eduStdID);
    }

    @Test(dependsOnMethods = "CreateEducationStandart")
    public void CreateEducationStandartNegative(){


        Map<String, String> CreateEduStds = new HashMap<>();
        CreateEduStds.put("id", eduStdID );
        CreateEduStds.put("name", eduStdName);
        CreateEduStds.put("description", eduStdDescription);

        given()
                .spec(reqSpec)
                .body(CreateEduStds)
                .log().uri()

                .when()
                .post("school-service/api/education-standard")

                .then()
                .log().body()
                .statusCode(400)
                .body("message",containsStringIgnoringCase("already"))
        ;
    }
    @Test(dependsOnMethods = "CreateEducationStandartNegative")
    public void EditEducationStandart(){

        eduStdName="ummet_egitim_grubu"+randomUreteci.number().digits(5);
        eduStdDescription="ummet_egitim_grubu_aciklama"+randomUreteci.number().digits(5);

        Map<String, String> CreateEduStds = new HashMap<>();
        CreateEduStds.put("id", eduStdID );
        CreateEduStds.put("name", eduStdName);
        CreateEduStds.put("description", eduStdDescription);
        CreateEduStds.put("schoolId", schoolId);
        CreateEduStds.put("gradeLevelId", "654898fae70d9e34a8331e51");
        CreateEduStds.put("subjectId", "657704ff8af7ce488ac69b9e");
        CreateEduStds.put("packageId", "6761dcbca7945338bd1ff0a7");

        given()
                .spec(reqSpec)
                .body(CreateEduStds)
                .log().uri()

                .when()
                .put("school-service/api/education-standard")

                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(eduStdName))
                .body("description",equalTo(eduStdDescription))
        ;
    }
    @Test(dependsOnMethods = "EditEducationStandart")
    public void DeleteEducationStandart(){


        Map<String, String> deleteEduStds = new HashMap<>();

        deleteEduStds.put("name", eduStdName);
        deleteEduStds.put("description", eduStdDescription);

        given()
                .spec(reqSpec)
                .body(deleteEduStds)
                .log().uri()

                .when()
                .delete("school-service/api/education-standard/"+eduStdID)

                .then()
                .log().body()
                .statusCode(204)

        ;
    }
    @Test(dependsOnMethods = "DeleteEducationStandart")
    public void DeleteEducationStandartNegative(){


        Map<String, String> deleteEduStds = new HashMap<>();
        deleteEduStds.put("name", eduStdName);
        deleteEduStds.put("description", eduStdDescription);

        given()
                .spec(reqSpec)
                .body(deleteEduStds)
                .log().uri()

                .when()
                .delete("school-service/api/education-standard/"+eduStdID)

                .then()
                .log().body()
                .statusCode(400)
                .body("message",containsStringIgnoringCase("not found"))

        ;
    }






}
