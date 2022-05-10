package b25.spartan.editor;

import io.restassured.http.ContentType;
import net.serenitybdd.rest.Ensure;
import utilities.SpartanNewBase;
import utilities.SpartanUtil;
import net.serenitybdd.junit5.SerenityTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SerenityTest
public class SpartanEditorPostTest extends SpartanNewBase {

   @DisplayName("Editor should be able to POST")
    @Test
    public void postSpartanAsEditor(){
       Map<String, Object> randomSpartanMap = SpartanUtil.getRandomSpartanMap();
       //create Map structure for random (fake) spartan from SpartanUtil package
       System.out.println(randomSpartanMap);

       //send a post request as editor
       given()
               .auth().basic("editor","editor")//authorize as editor
               .and()
               .accept(ContentType.JSON)
               .contentType(ContentType.JSON)
               .body(randomSpartanMap)
               //add our created Map with fake data
               .log().body()
               .when()
               .post("/spartans")//post to spartans
               .then().log().all();
    /*
                status code is 201
                content type is Json
                success message is A Spartan is Born!
                id is not null
                name is correct
                gender is correct
                phone is correct
                check location header ends with newly generated id
         */
       //status code is 201
       Ensure.that("status code is 201", p -> p.statusCode(201));
       //Ensure.that is for assertion, we use lambda here to put assertion condition
       //Ensure is soft assertion and to take report into Serenity

       //content type Json
       Ensure.that("content type is Jason", p -> p.contentType(ContentType.JSON));

       //"A Spartan is born"
       Ensure.that("success message is correct", p -> p.body("success",
               is("A Spartan is Born")));
       //to verify we use also 'is' from RestAssured

       //ID is not null
       Ensure.that("id is not null", p->p.body("data.id", notNullValue()));

       //name is correct
       Ensure.that("name is correct",p -> p.body
               ("data.name",is(randomSpartanMap.get("name"))));
       //randomSpartanMap is from above method

       //gender is correct
       Ensure.that("gender is correct",p -> p.body
               ("data.gender",is(randomSpartanMap.get("gender"))));

       //phone is correct
       Ensure.that("phone is correct",p -> p.body
               ("data.phone",is(randomSpartanMap.get("phone"))));

       //check location header ends with newly generated id
       String id = lastResponse().jsonPath().getString("data.id");
       //lastResponse is from Serenity library

       Ensure.that("check location header ends with newly generated id",
               p -> p.header("Location",endsWith(id)));//'id' is from above String
       //endWith is from Restassured Matchers library


   }

}
