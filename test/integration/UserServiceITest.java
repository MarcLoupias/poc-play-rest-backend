package integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.restassured.RestAssured;
import models.user.User;
import org.junit.Assert;
import org.junit.Test;
import org.myweb.utils.test.TestHelper;
import play.Logger;
import play.libs.Json;
import play.mvc.Http;

import static play.test.Helpers.*;

public class UserServiceITest extends IntegrationTestConfig {

    @Test
    public void testCan_GET_User() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                User newUser = TestHelper.userFactory(null, "userLoginForGetIT", "l3g@lPwd", "l3g@lPwd", "toto@forgetit.fr");

                String body = RestAssured.given()
                        .contentType("application/json")
                        .body(newUser)
                        .expect()
                        .statusCode(Http.Status.CREATED)
                        .when()
                        .post(BASE_URL + "/users")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsUser = Json.parse(body);
                Assert.assertNotNull(jsUser);

                User postedUser = Json.fromJson(jsUser, User.class);

                body = RestAssured.given()
                        .contentType("application/json")
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .get(BASE_URL + "/users/" + postedUser.getId())
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                jsUser = Json.parse(body);
                Assert.assertNotNull(jsUser);

                User user = Json.fromJson(jsUser, User.class);
                Assert.assertNotNull(user);
                Assert.assertEquals(postedUser.getLogin(), user.getLogin());

                RestAssured.given()
                        .contentType("application/json")
                        .body(postedUser)
                        .expect()
                        .statusCode(Http.Status.NO_CONTENT)
                        .when()
                        .delete(BASE_URL + "/users/" + postedUser.getId())
                        .andReturn().body().asString();
            }
        });
    }

    @Test
    public void testCan_QUERY_Users() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                User newUser = TestHelper.userFactory(null, "userLoginForQueryIT", "l3g@lPwd", "l3g@lPwd", "toto@forqueryit.fr");

                String body = RestAssured.given()
                        .contentType("application/json")
                        .body(newUser)
                        .expect()
                        .statusCode(Http.Status.CREATED)
                        .when()
                        .post(BASE_URL + "/users")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsPostedUser = Json.parse(body);
                Assert.assertNotNull(jsPostedUser);

                User postedUser = Json.fromJson(jsPostedUser, User.class);

                body = RestAssured.given()
                        .contentType("application/json")
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .get(BASE_URL + "/users")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsUserList = Json.parse(body);
                Assert.assertNotNull(jsUserList);
                Assert.assertTrue(jsUserList.isArray());
                Assert.assertTrue(jsUserList.size() >= 1);

                for(JsonNode jsUser : jsUserList) {
                    User u = Json.fromJson(jsUser, User.class);
                    if(u.getId().longValue() == postedUser.getId().longValue()) {
                        Assert.assertEquals(postedUser.getLogin(), u.getLogin());
                    }
                }

                RestAssured.given()
                        .contentType("application/json")
                        .body(postedUser)
                        .expect()
                        .statusCode(Http.Status.NO_CONTENT)
                        .when()
                        .delete(BASE_URL + "/users/" + postedUser.getId())
                        .andReturn().body().asString();
            }
        });
    }

    @Test
    public void testCan_CREATE_User() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {
                User newUser = TestHelper.userFactory(null, "userLoginForPostIT", "l3g@lPwd", "l3g@lPwd", "toto@forpostit.fr");

                String body = RestAssured.given()
                        .contentType("application/json")
                        .body(newUser)
                        .expect()
                        .statusCode(Http.Status.CREATED)
                        .when()
                        .post(BASE_URL + "/users")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsUser = Json.parse(body);
                Assert.assertNotNull(jsUser);

                User user = Json.fromJson(jsUser, User.class);
                Assert.assertNotNull(user);
                Assert.assertEquals(user.getLogin(), "userLoginForPostIT");

                RestAssured.given()
                        .contentType("application/json")
                        .body(user)
                        .expect()
                        .statusCode(Http.Status.NO_CONTENT)
                        .when()
                        .delete(BASE_URL + "/users/" + user.getId())
                        .andReturn().body().asString();
            }
        });
    }

    @Test
    public void testCan_UPDATE_User() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {
                User newUser = TestHelper.userFactory(null, "userLoginForPutIT", "l3g@lPwd", "l3g@lPwd", "toto@forputit.fr");

                String body = RestAssured.given()
                        .contentType("application/json")
                        .body(newUser)
                        .expect()
                        .statusCode(Http.Status.CREATED)
                        .when()
                        .post(BASE_URL + "/users")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsUser = Json.parse(body);
                Assert.assertNotNull(jsUser);

                User user = Json.fromJson(jsUser, User.class);
                Assert.assertNotNull(user);
                Assert.assertEquals(user.getLogin(), "userLoginForPutIT");
                Logger.info("*** User id = " + user.getId());

                user.setNewPassword("l3g@lPwdUpdated");
                user.setConfirmPassword("l3g@lPwdUpdated");
                user.setLogin("userLoginForPutITupdated");
                user.setEmail("totoupdated@forputit.fr");

                RestAssured.given()
                        .contentType("application/json")
                        .body(user)
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .put(BASE_URL + "/users/" + user.getId())
                        .andReturn().body().asString();

                RestAssured.given()
                        .contentType("application/json")
                        .body(user)
                        .expect()
                        .statusCode(Http.Status.NO_CONTENT)
                        .when()
                        .delete(BASE_URL + "/users/" + user.getId())
                        .andReturn().body().asString();
            }
        });
    }

    @Test
    public void testCan_DELETE_User() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                User newUser = TestHelper.userFactory(null, "userLoginForDeleteIT", "l3g@lPwd", "l3g@lPwd", "toto@fordeleteit.fr");

                String body = RestAssured.given()
                        .contentType("application/json")
                        .body(newUser)
                        .expect()
                        .statusCode(Http.Status.CREATED)
                        .when()
                        .post(BASE_URL + "/users")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsUser = Json.parse(body);
                Assert.assertNotNull(jsUser);

                User postedUser = Json.fromJson(jsUser, User.class);

                RestAssured.given()
                        .contentType("application/json")
                        .body(postedUser)
                        .expect()
                        .statusCode(Http.Status.NO_CONTENT)
                        .when()
                        .delete(BASE_URL + "/users/" + postedUser.getId())
                        .andReturn().body().asString();
            }
        });
    }
}
