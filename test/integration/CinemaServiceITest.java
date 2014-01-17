package integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.restassured.RestAssured;
import models.Cinema;
import models.County;
import org.junit.Assert;
import org.junit.Test;
import org.myweb.utils.test.TestHelper;
import play.libs.Json;
import play.mvc.Http;

import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public class CinemaServiceITest extends IntegrationTestConfig {

    @Test
    public void testCan_GET_Cinema() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                String body = RestAssured.given()
                        .contentType("application/json")
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .get(BASE_URL + "/cinemas/1")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsCounty = Json.parse(body);
                Assert.assertNotNull(jsCounty);

                Cinema cinema = Json.fromJson(jsCounty, Cinema.class);
                Assert.assertNotNull(cinema);
                Assert.assertEquals(cinema.getName(), "GEORGE V 1");
            }
        });
    }

    @Test
    public void testCan_QUERY_Cinemas() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                String body = RestAssured.given()
                        .contentType("application/json")
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .get(BASE_URL + "/cinemas")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsCinemaList = Json.parse(body);
                Assert.assertNotNull(jsCinemaList);
                Assert.assertTrue(jsCinemaList.isArray());
                Assert.assertTrue(jsCinemaList.size() >= 2035);

                for(JsonNode jsCinema : jsCinemaList) {
                    Cinema c = Json.fromJson(jsCinema, Cinema.class);
                    if(c.getId() == 1l) {
                        Assert.assertEquals("GEORGE V 1", c.getName());
                    }
                    if(c.getId() == 2l) {
                        Assert.assertEquals("UGC NORMANDIE 1", c.getName());
                    }
                }
            }
        });
    }

    @Test
    public void testCan_POST_Cinema() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                County countyAin = TestHelper.countyFactory(1l, "1", "Ain");
                Cinema newCinema = TestHelper.cinemaFactory(null, "testCinemaName_Post", 1, 100, countyAin);

                String body = RestAssured.given()
                        .contentType("application/json")
                        .body(newCinema)
                        .expect()
                        .statusCode(Http.Status.CREATED)
                        .when()
                        .post(BASE_URL + "/cinemas")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsCinema = Json.parse(body);
                Assert.assertNotNull(jsCinema);

                Cinema cinema = Json.fromJson(jsCinema, Cinema.class);
                Assert.assertNotNull(cinema);
                Assert.assertEquals(cinema.getName(), cinema.getName());
                Assert.assertNotNull(cinema.getId());
                Assert.assertTrue(cinema.getId() > 0);

                RestAssured.given()
                        .contentType("application/json")
                        .body(cinema)
                        .expect()
                        .statusCode(Http.Status.NO_CONTENT)
                        .when()
                        .delete(BASE_URL + "/cinemas/" + cinema.getId())
                        .andReturn().body().asString();
            }
        });
    }

    @Test
    public void testCan_PUT_Cinema() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                County countyAin = TestHelper.countyFactory(1l, "1", "Ain");
                Cinema newCinema = TestHelper.cinemaFactory(null, "testCinemaNameForPut_Post", 1, 100, countyAin);

                String body = RestAssured.given()
                        .contentType("application/json")
                        .body(newCinema)
                        .expect()
                        .statusCode(Http.Status.CREATED)
                        .when()
                        .post(BASE_URL + "/cinemas")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsCinema = Json.parse(body);
                Assert.assertNotNull(jsCinema);

                Cinema cinema = Json.fromJson(jsCinema, Cinema.class);
                Assert.assertNotNull(cinema);

                cinema.setName("testCinemaNameForPut_Put");

                RestAssured.given()
                        .contentType("application/json")
                        .body(cinema)
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .put(BASE_URL + "/cinemas/" + cinema.getId())
                        .andReturn().body().asString();

                RestAssured.given()
                        .contentType("application/json")
                        .body(cinema)
                        .expect()
                        .statusCode(Http.Status.NO_CONTENT)
                        .when()
                        .delete(BASE_URL + "/cinemas/" + cinema.getId())
                        .andReturn().body().asString();
            }
        });
    }

    @Test
    public void testCan_DELETE_Cinema() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                County countyAin = TestHelper.countyFactory(1l, "1", "Ain");
                Cinema newCinema = TestHelper.cinemaFactory(null, "testCinemaNameForDelete", 1, 100, countyAin);

                String body = RestAssured.given()
                        .contentType("application/json")
                        .body(newCinema)
                        .expect()
                        .statusCode(Http.Status.CREATED)
                        .when()
                        .post(BASE_URL + "/cinemas")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsCinema = Json.parse(body);
                Assert.assertNotNull(jsCinema);

                Cinema cinema = Json.fromJson(jsCinema, Cinema.class);
                Assert.assertNotNull(cinema);

                RestAssured.given()
                        .contentType("application/json")
                        .body(cinema)
                        .expect()
                        .statusCode(Http.Status.NO_CONTENT)
                        .when()
                        .delete(BASE_URL + "/cinemas/" + cinema.getId())
                        .andReturn().body().asString();
            }
        });
    }

}
