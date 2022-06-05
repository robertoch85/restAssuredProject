package com.cucumber.stepdefs;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.File;
import java.util.HashMap;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class PetImplementation {

    private Response addPet = null;
    private Response updatePet= null;
    private Response deletePet= null;
    private Response getPetID = null;

    @Before
    public void before(){
        RestAssured.baseURI = "https://petstore.swagger.io/";
    }

    @Given("that the user is in the pet page")
    public Response getPetPage(){

        Response responseGetPetPage = given().log().all().get("/v2/pet") ;

        return responseGetPetPage;
    }

    @And("the details of the pet has been added")
    public void theDetailsOfThePetHasBeenAdded() {

        File addPetFile = new File ("src/main/resources/data/addPet.json");

        addPet = given().contentType(ContentType.JSON).body(addPetFile).post("/v2/pet/");
    }


    @Then("the body response contains the key id")
    public void theBodyResponseContainsTheIdOfThePetCreated() {

        addPet.then().body("$",hasKey("id"));
    }

    @And("the details of the pet has been updated")
    public void theDetailsOfThePetHasBeenUpdated() {
        theDetailsOfThePetHasBeenAdded();

        File updatePetFile = new File ("src/main/resources/data/updatePet.json");

        updatePet = given().contentType(ContentType.JSON).body(updatePetFile).put("/v2/pet/");
    }

    @Then("the body response contains the update {string}")
    public void theBodyResponseContainsTheUpdate(String name) {
        JsonPath jsonPathPets = new JsonPath(updatePet.body().asString());
        String jsonPets = jsonPathPets.getString("name");
        assertEquals("The value of the name is not the same", name, jsonPets);
    }

    @And("we have the ID of the pet")
    public void weHaveTheIDOfThePet() {
        theDetailsOfThePetHasBeenAdded();
        JsonPath jsonPathPets = new JsonPath(addPet.body().asString());
        String jsonIdCreate=jsonPathPets.getString("id");

        getPetID = given().contentType(ContentType.JSON).get("/v2/pet/"+jsonIdCreate);
    }

    @Then("the body response contains the {string} of the pet")
    public void theBodyResponseContainsTheIDOfThePet(String ID) {
        JsonPath jsonPathPets = new JsonPath(getPetID.body().asString());
        String jsonPets = jsonPathPets.getString("id");
        assertEquals("The value of the name is not the same", ID, jsonPets);
    }

    @And("the pet is deleted by ID")
    public void thePetIsDeletedByID() {
        theDetailsOfThePetHasBeenAdded();
        JsonPath jsonPathPets = new JsonPath(addPet.body().asString());
        String jsonIdCreate=jsonPathPets.getString("id");
        deletePet = given().accept(ContentType.JSON).delete("/v2/pet/"+jsonIdCreate);
    }

    @And("the status of the request is status")
    public Response theStatusOfTheRequestIsPending() {

        given().log().uri().get("https://petstore.swagger.io/v2/pet/findByStatus?status=pending").getBody().asString();
        Response responseGetPets = given().log().all().param("status", "pending").get("/v2/pet/findByStatus") ;

        return responseGetPets;

    }

    @Then("the body response contains the following ids")
    public void theBodyResponseContainsTheFollowingIds() {
        theStatusOfTheRequestIsPending().then().body("id", hasItems(555666));
    }

    @And("the response code for the delete is 200")
    public void theResponseCodeForTheDeleteIs() {
        assertTrue("The response is not 200", deletePet.statusCode()==200);
    }


    @And("the response code for the update is 200")
    public void theResponseCodeForTheUpdateIs() {
        assertTrue("The response is not 200", updatePet.statusCode()==200);
    }

    @And("the response code for the pet is 200")
    public void theResponseCodeForThePetIs() {
        assertTrue("The response is not 200", getPetID.statusCode()==200);
    }
}