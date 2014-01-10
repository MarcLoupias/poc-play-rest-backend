package integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.restassured.RestAssured;
import models.Category;
import org.junit.Assert;
import org.junit.Test;
import org.myweb.db.TestHelper;
import play.libs.Json;
import play.mvc.Http;

import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public class CategoryServiceITest extends IntegrationTestConfig {

    @Test
    public void testCan_GET_Category() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                String body = RestAssured.given()
                        .contentType("application/json")
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .get(BASE_URL + "/categories/1")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsCategory = Json.parse(body);
                Assert.assertNotNull(jsCategory);

                Category category = Json.fromJson(jsCategory, Category.class);
                Assert.assertNotNull(category);
                Assert.assertEquals(category.getName(), "category A");
            }
        });
    }

    @Test
    public void testCan_QUERY_Categories() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                String body = RestAssured.given()
                        .contentType("application/json")
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .get(BASE_URL + "/categories")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsCategoryList = Json.parse(body);
                Assert.assertNotNull(jsCategoryList);
                Assert.assertTrue(jsCategoryList.isArray());
                Assert.assertTrue(jsCategoryList.size() >= 2);

                for(JsonNode jsCategory : jsCategoryList) {
                    Category c = Json.fromJson(jsCategory, Category.class);
                    if(c.getId() == 1l) {
                        Assert.assertEquals("category A", c.getName());
                    }
                    if(c.getId() == 2l) {
                        Assert.assertEquals("category B", c.getName());
                    }
                }
            }
        });
    }

    @Test
    public void testCan_POST_Category() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                Category newCategory = TestHelper.categoryFactory(null, "newCategory");

                String body = RestAssured.given()
                        .contentType("application/json")
                        .body(newCategory)
                        .expect()
                        .statusCode(Http.Status.CREATED)
                        .when()
                        .post(BASE_URL + "/categories")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsCategory = Json.parse(body);
                Assert.assertNotNull(jsCategory);

                Category category = Json.fromJson(jsCategory, Category.class);
                Assert.assertNotNull(category);
                Assert.assertEquals(category.getName(), newCategory.getName());
                Assert.assertNotNull(category.getId());
                Assert.assertTrue(category.getId() > 0);

                RestAssured.given()
                        .contentType("application/json")
                        .body(category)
                        .expect()
                        .statusCode(Http.Status.NO_CONTENT)
                        .when()
                        .delete(BASE_URL + "/categories/" + category.getId())
                        .andReturn().body().asString();
            }
        });
    }

    @Test
    public void testCan_PUT_Category() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                Category newCategory = TestHelper.categoryFactory(null, "testCategForPut_Post");

                String body = RestAssured.given()
                        .contentType("application/json")
                        .body(newCategory)
                        .expect()
                        .statusCode(Http.Status.CREATED)
                        .when()
                        .post(BASE_URL + "/categories")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsCategory = Json.parse(body);
                Assert.assertNotNull(jsCategory);

                Category category = Json.fromJson(jsCategory, Category.class);
                Assert.assertNotNull(category);

                category.setName("testCategForPut_Put");

                RestAssured.given()
                        .contentType("application/json")
                        .body(category)
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .put(BASE_URL + "/categories/" + category.getId())
                        .andReturn().body().asString();

                RestAssured.given()
                        .contentType("application/json")
                        .body(category)
                        .expect()
                        .statusCode(Http.Status.NO_CONTENT)
                        .when()
                        .delete(BASE_URL + "/categories/" + category.getId())
                        .andReturn().body().asString();
            }
        });
    }

    @Test
    public void testCan_DELETE_Category() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                Category newCategory = TestHelper.categoryFactory(null, "testCategForDelete");

                String body = RestAssured.given()
                        .contentType("application/json")
                        .body(newCategory)
                        .expect()
                        .statusCode(Http.Status.CREATED)
                        .when()
                        .post(BASE_URL + "/categories")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsCategory = Json.parse(body);
                Assert.assertNotNull(jsCategory);

                Category category = Json.fromJson(jsCategory, Category.class);
                Assert.assertNotNull(category);

                RestAssured.given()
                        .contentType("application/json")
                        .body(category)
                        .expect()
                        .statusCode(Http.Status.NO_CONTENT)
                        .when()
                        .delete(BASE_URL + "/categories/" + category.getId())
                        .andReturn().body().asString();
            }
        });
    }

}
