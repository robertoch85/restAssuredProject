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
import java.time.LocalDate;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StoreImplementation {

    private Response getInventory = null;
    private Response getInventoryPage = null;
    private Response addOrder= null;
    private Response getOrderID= null;
    private Response deleteOrder = null;

    @Before
    public void before(){
        RestAssured.baseURI = "https://petstore.swagger.io/";
    }

    @Given("we get the inventory of the store")
    public Response weGetTheInventoryOfTheStore() {

        Response responseInventory = given().baseUri("https://petstore.swagger.io/v2/store/inventory").get();

        //File getInventoryTotal = new File ("src/main/resources/data/getInventoryTotal.json");
        //getInventory = given().contentType(ContentType.JSON).body(getInventoryTotal).put("/v2/store/inventory");
        //getInventoryPage = given().contentType(ContentType.JSON).get("/v2/store/inventory");
        return  responseInventory;
    }

    @Then("the store response code is 200")
    public void theStoreResponseCodeIs() {
        assertTrue("The response is not 200", weGetTheInventoryOfTheStore().statusCode()==200);
    }

    @Given("we have the details of the order")
    public void weHaveTheDetailsOfTheOrder() {

        HashMap<String, String> bodyRequestMap = new HashMap<>();
        bodyRequestMap.put("id", "708585");
        bodyRequestMap.put("petId", "555588");
        bodyRequestMap.put("quantity", "1");
        bodyRequestMap.put("shipDate", LocalDate.now().toString());
        bodyRequestMap.put("status", "placed");
        bodyRequestMap.put("complete", "true");
        addOrder = given().contentType(ContentType.JSON).body(bodyRequestMap).post("/v2/store/order");
    }

    @Then("the body response contains the {string} of the order")
    public void theBodyResponseContainsTheIDOfTheOrder(String id) {

        JsonPath jsonPathPets = new JsonPath(addOrder.body().asString());
        String jsonPets=jsonPathPets.getString("id");
        assertEquals("The value of the name is not the same", id, jsonPets);
    }

    @Given("we have the ID of the order")
    public void weHaveTheIDOfTheOrder() {

        weHaveTheDetailsOfTheOrder();
        JsonPath jsonPathOrder = new JsonPath(addOrder.body().asString());
        String jsonIdCreate=jsonPathOrder.getString("id");

        getOrderID = given().contentType(ContentType.JSON).get("/v2/store/order" +jsonIdCreate);
    }

    @And("the store response code for the order placed is 200")
    public void theStoreResponseCodeForTheOrderPlacedIs() {
        assertTrue("The response is not 200", addOrder.statusCode()==200);
    }

    @And("the store response code for the order we need is 200")
    public void theStoreResponseCodeForTheOrderWeNeedIs() {
        assertTrue("The response is not 200", addOrder.statusCode()==200);
    }

    @Then("the response code for the order to be deleted is 200")
    public void theResponseCodeForTheOrderToBeDeletedIs() {
        assertTrue("The response is not 200", deleteOrder.statusCode()==200);

    }

    @Given("we have the ID of the order to be deleted")
    public void weHaveTheIDOfTheOrderToBeDeleted() {
        weHaveTheDetailsOfTheOrder();
        JsonPath jsonPathUsers = new JsonPath(addOrder.body().asString());
        String jsonIdCreate=jsonPathUsers.getString("id");
        deleteOrder = given().accept(ContentType.JSON).delete("/v2/store/order" +jsonIdCreate);
    }
}
