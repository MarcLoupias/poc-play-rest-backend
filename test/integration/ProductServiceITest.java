package integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.restassured.RestAssured;
import models.Category;
import models.Product;
import org.junit.Assert;
import org.junit.Test;
import org.myweb.db.TestHelper;
import play.libs.Json;
import play.mvc.Http;

import java.util.Date;

import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public class ProductServiceITest extends IntegrationTestConfig {

    @Test
    public void testCan_GET_Product() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                String body = RestAssured.given()
                        .contentType("application/json")
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .get(BASE_URL + "/products/1")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsProduct = Json.parse(body);
                Assert.assertNotNull(jsProduct);

                Product product = Json.fromJson(jsProduct, Product.class);
                Assert.assertNotNull(product);
                Assert.assertEquals(product.getName(), "Product AAA");
            }
        });
    }

    @Test
    public void testCan_QUERY_Products() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                String body = RestAssured.given()
                        .contentType("application/json")
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .get(BASE_URL + "/products")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsProductList = Json.parse(body);
                Assert.assertNotNull(jsProductList);
                Assert.assertTrue(jsProductList.isArray());
                Assert.assertTrue(jsProductList.size() >= 2);

                for(JsonNode jsProduct : jsProductList) {
                    Product p = Json.fromJson(jsProduct, Product.class);
                    if(p.getId() == 1l) {
                        Assert.assertEquals("Product AAA", p.getName());
                    }
                    if(p.getId() == 2l) {
                        Assert.assertEquals("Product BBB", p.getName());
                    }
                }
            }
        });
    }

    @Test
    public void testCan_POST_Product() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                Category c = TestHelper
                        .categoryFactory(1l, "category A");
                Product newProduct = TestHelper
                        .productFactory(null, "Product ZZZZZ", new Date(), 10, null, c);

                String body = RestAssured.given()
                        .contentType("application/json")
                        .body(newProduct).then()
                        .expect()
                        .statusCode(Http.Status.CREATED)
                        .when()
                        .post(BASE_URL + "/products")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsProduct = Json.parse(body);
                Assert.assertNotNull(jsProduct);

                Product product = Json.fromJson(jsProduct, Product.class);
                Assert.assertNotNull(product);
                Assert.assertEquals(product.getName(), newProduct.getName());
                Assert.assertNotNull(product.getId());
                Assert.assertTrue(product.getId() > 0);

                RestAssured.given()
                        .contentType("application/json")
                        .body(product)
                        .expect()
                        .statusCode(Http.Status.NO_CONTENT)
                        .when()
                        .delete(BASE_URL + "/products/" + product.getId())
                        .andReturn().body().asString();
            }
        });
    }

    @Test
    public void testCan_PUT_Product() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                Category c = TestHelper
                        .categoryFactory(1l, "category A");
                Product newProduct = TestHelper
                        .productFactory(null, "testProdForPut_Post", new Date(), 10, null, c);

                String body = RestAssured.given()
                        .contentType("application/json")
                        .body(newProduct)
                        .expect()
                        .statusCode(Http.Status.CREATED)
                        .when()
                        .post(BASE_URL + "/products")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsProduct = Json.parse(body);
                Assert.assertNotNull(jsProduct);

                Product product = Json.fromJson(jsProduct, Product.class);
                Assert.assertNotNull(product);

                product.setName("testProdForPut_Put");

                body = RestAssured.given()
                        .contentType("application/json")
                        .body(product)
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .put(BASE_URL + "/products/" + product.getId())
                        .andReturn().body().asString();

                RestAssured.given()
                        .contentType("application/json")
                        .body(product)
                        .expect()
                        .statusCode(Http.Status.NO_CONTENT)
                        .when()
                        .delete(BASE_URL + "/products/" + product.getId())
                        .andReturn().body().asString();            }
        });
    }

    @Test
    public void testCan_DELETE_Product() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                Category c = TestHelper
                        .categoryFactory(1l, "category A");
                Product newProduct = TestHelper
                        .productFactory(null, "testProductForDelete", null, 10, null, c);

                String body = RestAssured.given()
                        .contentType("application/json")
                        .body(newProduct)
                        .expect()
                        .statusCode(Http.Status.CREATED)
                        .when()
                        .post(BASE_URL + "/products")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsProduct = Json.parse(body);
                Assert.assertNotNull(jsProduct);

                Product product = Json.fromJson(jsProduct, Product.class);
                Assert.assertNotNull(product);



                body = RestAssured.given()
                        .contentType("application/json")
                        .body(product)
                        .expect()
                        .statusCode(Http.Status.NO_CONTENT)
                        .when()
                        .delete(BASE_URL + "/products/" + product.getId())
                        .andReturn().body().asString();
            }
        });
    }

}
