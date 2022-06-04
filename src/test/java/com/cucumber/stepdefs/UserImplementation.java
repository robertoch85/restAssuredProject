package com.cucumber.stepdefs;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

public class UserImplementation {

    private Response addUser = null;
    private Response getUser = null;
    private Response updateUser= null;
    private Response deleteUser= null;
    private Response loginUser = null;
    private Response logoutUser = null;

    @Before
    public void before(){
        RestAssured.baseURI = "https://petstore.swagger.io/";
    }

    @Given("that the user is in the user page")
    public Response getUserPage(){

        Response responseGetUserPage = given().log().all().get("/v2/user") ;

        return responseGetUserPage;
    }


    @Given("the details of user has been added")
    public void theDetailsOfUserHasBeenAdded() {
        File addUserFile = new File ("src/main/resources/data/addUser.json");

        addUser = given().contentType(ContentType.JSON).body(addUserFile).post("/v2/user");
    }


    @And("the response code for add user is 200")
    public void theResponseCodeForAddUserIs() {
        assertTrue("The response is not 200", addUser.statusCode()==200);
    }

    @Then("the body response contains the {string} of the user created")
    public void theBodyResponseContainsTheOfTheUserCreated(String id) {
        JsonPath jsonPathPets = new JsonPath(addUser.body().asString());
        String jsonPets = jsonPathPets.getString("message");
        assertEquals("The value of the name is not the same", id, jsonPets);
    }

    @Given("we have the name of the user")
    public void weHaveTheNameOfTheUser() {
        theDetailsOfUserHasBeenAdded();
        JsonPath jsonPathUsers = new JsonPath(addUser.body().asString());
        String jsonIdCreate=jsonPathUsers.getString("id");

        getUser = given().contentType(ContentType.JSON).get("/v2/user/"+ jsonIdCreate);
    }

    @And("the response code for get user is 200")
    public void theResponseCodeForGetUserIs() {
        assertTrue("The response is not 200", getUser.statusCode()==200);
    }

    @Then("the body response contains the {string} of the user")
    public void theBodyResponseContainsTheNameOfTheUser(String name) {
        JsonPath jsonPathUsers = new JsonPath(getUser.body().asString());
        String jsonUsers = jsonPathUsers.getString("username");
        assertEquals("The value of the name is not the same", name, jsonUsers);
    }

    @Given("the details of the user has been updated")
    public void theDetailsOfTheUserHasBeenUpdated() {
        theDetailsOfUserHasBeenAdded();

        JsonPath jsonPathUsers = new JsonPath(addUser.body().asString());
        String jsonUsername = jsonPathUsers.getString("username");
        File updateUserFile = new File ("src/main/resources/data/updateUser.json");

        updateUser = given().contentType(ContentType.JSON).body(updateUserFile).put("/v2/user/" + jsonUsername);
    }

    @And("the response code for update user is 200")
    public void theResponseCodeForUpdateUserIs() {
        assertTrue("The response is not 200", updateUser.statusCode()==200);

    }

    @Then("the body response contains the updated {string}")
    public void theBodyResponseContainsTheIdOfTheUpdatedUser(String phone) {
        JsonPath jsonPathUsers = new JsonPath(updateUser.body().asString());
        String jsonUsersPhone = jsonPathUsers.getString("phone");
        assertEquals("The value of the name is not the same", phone, jsonUsersPhone);
    }

    @Given("the user is deleted by username")
    public void theUserIsDeletedByUsername() {
        theDetailsOfUserHasBeenAdded();
        JsonPath jsonPathUsers = new JsonPath(addUser.body().asString());
        String jsonUsername =jsonPathUsers.getString("username");
        deleteUser = given().accept(ContentType.JSON).delete("/v2/user/" +jsonUsername);
    }

    @Then("the body response is empty")
    public void theResponseCodeIs() {
        assertTrue("The body response is not empty",
                deleteUser.body().asString().isEmpty());
    }

    @Given("the login details username and password")
    public void theLoginDetailsUsernameAndPassword() {
        theDetailsOfUserHasBeenAdded();
        JsonPath jsonPathUsers = new JsonPath(addUser.body().asString());
        String jsonUsername=jsonPathUsers.getString("username");
        String jsonpass= jsonPathUsers.get("password");

        loginUser = given().contentType(ContentType.JSON).get("/v2/user/login?username="+ jsonUsername + "&password" +jsonpass);
    }

    @Then("the response code for login user is 200")
    public void theResponseCodeForLoginUserIs() {
        JsonPath jsonPathUsers = new JsonPath(loginUser.body().asString());
        String jsonUsersCode = jsonPathUsers.getString("code");
        assertEquals("The value of the name is not the same", "200", jsonUsersCode);
    }

    @Given("the user logs out")
    public void theUserLogsOut() {
        //theDetailsOfUserHasBeenAdded();
        //logoutUser = given().contentType(ContentType.JSON).get("/v2/user/"+param);

        logoutUser = given().baseUri("https://petstore.swagger.io/v2/user/logout").get();

    }

    @Then("the response code for logout user is 200")
    public void theResponseCodeForLogoutUserIs() {
        assertTrue("The response is not 200", logoutUser.statusCode()==200);
    }
}
