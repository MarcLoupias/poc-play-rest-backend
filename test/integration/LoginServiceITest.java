package integration;

import com.jayway.restassured.RestAssured;
import org.junit.Test;
import org.myweb.utils.test.TestHelper;
import org.myweb.services.user.login.UserLoginAttempt;
import play.mvc.Http;

import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public class LoginServiceITest extends IntegrationTestConfig {

    @Test
    public void testCan_LoginAdminUser() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {
                UserLoginAttempt loginAttempt = TestHelper
                        .userLoginAttemptFactory("", "admin", "@dm1nPwd");

                RestAssured.given()
                        .contentType("application/json")
                        .body(loginAttempt)
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .post(BASE_URL + "/users/login")
                        .andReturn().body().asString();

                // logout is managed by Play!, simply close the browser.
            }
        });
    }
}
