package integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.restassured.RestAssured;
import models.County;
import org.junit.Assert;
import org.junit.Test;
import org.myweb.db.TestHelper;
import play.libs.Json;
import play.mvc.Http;

import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public class CountyServiceITest extends IntegrationTestConfig {

    @Test
    public void testCan_GET_County() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                String body = RestAssured.given()
                        .contentType("application/json")
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .get(BASE_URL + "/counties/1")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsCounty = Json.parse(body);
                Assert.assertNotNull(jsCounty);

                County county = Json.fromJson(jsCounty, County.class);
                Assert.assertNotNull(county);
                Assert.assertEquals(county.getName(), "Ain");
            }
        });
    }

    @Test
    public void testCan_QUERY_Counties() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                String body = RestAssured.given()
                        .contentType("application/json")
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .get(BASE_URL + "/counties")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsCountyList = Json.parse(body);
                Assert.assertNotNull(jsCountyList);
                Assert.assertTrue(jsCountyList.isArray());
                Assert.assertTrue(jsCountyList.size() >= 99);

                for(JsonNode jsCounty : jsCountyList) {
                    County c = Json.fromJson(jsCounty, County.class);
                    if(c.getId() == 1l) {
                        Assert.assertEquals("Ain", c.getName());
                    }
                    if(c.getId() == 2l) {
                        Assert.assertEquals("Aisne", c.getName());
                    }
                }
            }
        });
    }

    @Test
    public void testCan_POST_County() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                County newCounty = TestHelper.countyFactory(null, "newCountyCode", "newCountyName");

                String body = RestAssured.given()
                        .contentType("application/json")
                        .body(newCounty)
                        .expect()
                        .statusCode(Http.Status.CREATED)
                        .when()
                        .post(BASE_URL + "/counties")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsCounty = Json.parse(body);
                Assert.assertNotNull(jsCounty);

                County county = Json.fromJson(jsCounty, County.class);
                Assert.assertNotNull(county);
                Assert.assertEquals(county.getName(), newCounty.getName());
                Assert.assertNotNull(county.getId());
                Assert.assertTrue(county.getId() > 0);

                RestAssured.given()
                        .contentType("application/json")
                        .body(county)
                        .expect()
                        .statusCode(Http.Status.NO_CONTENT)
                        .when()
                        .delete(BASE_URL + "/counties/" + county.getId())
                        .andReturn().body().asString();
            }
        });
    }

    @Test
    public void testCan_PUT_County() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                County newCounty = TestHelper.countyFactory(null, "testCountyCodeForPut_Post", "testCountyNameForPut_Post");

                String body = RestAssured.given()
                        .contentType("application/json")
                        .body(newCounty)
                        .expect()
                        .statusCode(Http.Status.CREATED)
                        .when()
                        .post(BASE_URL + "/counties")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsCounty = Json.parse(body);
                Assert.assertNotNull(jsCounty);

                County county = Json.fromJson(jsCounty, County.class);
                Assert.assertNotNull(county);

                county.setName("testCountyNameForPut_Put");

                RestAssured.given()
                        .contentType("application/json")
                        .body(county)
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .put(BASE_URL + "/counties/" + county.getId())
                        .andReturn().body().asString();

                RestAssured.given()
                        .contentType("application/json")
                        .body(county)
                        .expect()
                        .statusCode(Http.Status.NO_CONTENT)
                        .when()
                        .delete(BASE_URL + "/counties/" + county.getId())
                        .andReturn().body().asString();
            }
        });
    }

    @Test
    public void testCan_DELETE_County() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                County newCounty = TestHelper.countyFactory(null, "testCategCodeForDelete", "testCategNameForDelete");

                String body = RestAssured.given()
                        .contentType("application/json")
                        .body(newCounty)
                        .expect()
                        .statusCode(Http.Status.CREATED)
                        .when()
                        .post(BASE_URL + "/counties")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsCounty = Json.parse(body);
                Assert.assertNotNull(jsCounty);

                County county = Json.fromJson(jsCounty, County.class);
                Assert.assertNotNull(county);

                RestAssured.given()
                        .contentType("application/json")
                        .body(county)
                        .expect()
                        .statusCode(Http.Status.NO_CONTENT)
                        .when()
                        .delete(BASE_URL + "/counties/" + county.getId())
                        .andReturn().body().asString();
            }
        });
    }

}
