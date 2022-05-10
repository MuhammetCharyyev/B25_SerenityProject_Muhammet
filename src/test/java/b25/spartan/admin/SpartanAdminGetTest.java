package b25.spartan.admin;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@SerenityTest//Serenity annotation
public class SpartanAdminGetTest {

    //beforeAll is the same thing with beforeClass in testng
    @BeforeAll
    public static void init(){
        RestAssured.baseURI ="http://44.201.121.105:7000";
    }

    @Test
    public void getAllSpartan(){
        given()
                .accept(ContentType.JSON)
                .and()
                .auth().basic("admin","admin")
                .when()
                .get("/api/spartans")
                .then()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON);
    }

    @Test
    public void getOneSpartan() {

        given()
                .accept(ContentType.JSON)
                .and()
                .auth().basic("admin","admin")
                .pathParam("id", 8)
                .when()
                .get("/api/spartans/{id}");

        //if you send a request using serenityRest, the response object
        //can be obtained from the method called lastResponse() without being
        //saved separately same with Response response object

        System.out.println("lastResponse().statusCode() = " + lastResponse().statusCode());
        //lastResponse of Serenity is the same as Response from RestAssured
        //we dont need to assign Response for above coz Serenity save this

        System.out.println("lastResponse().path(\"id\") = " + lastResponse().path("id"));

        System.out.println(lastResponse().jsonPath().getString("name"));

        Ensure.that("status code is 200", vRes -> vRes.statusCode(200));
        //Ensure.that is for assertion, we use lambda here to put assertion condition
         //Ensure is soft assertion and to take report into Serenity
        Ensure.that("content type is Jason", vRes -> vRes.contentType(ContentType.JSON));

        Ensure.that("ID is 8", vRes -> vRes.body("id", is(8)));
        //to verify we use also 'is' from RestAssured

    }
}
