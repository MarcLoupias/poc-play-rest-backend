package integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Test;
import org.myweb.services.version.VersionService;
import play.libs.Json;
import play.mvc.Http;
import play.test.Helpers;

import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public class VersionServiceITest extends IntegrationTestConfig {

    @Test
    public void testCan_GET_Version() {
        running(testServer(PORT), new Runnable() {
            @Override
            public void run() {

                String body = RestAssured.given()
                        .contentType("application/json")
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .get(BASE_URL + "/public/version")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsVersion = Json.parse(body);
                Assert.assertNotNull(jsVersion);

                VersionService.ShortVersion version = Json.fromJson(jsVersion, VersionService.ShortVersion.class);
                Assert.assertNotNull(version);
                Assert.assertEquals(version.appName, "poc-play-rest-backend");
                Assert.assertEquals(version.appVersion, "1.0-SNAPSHOT");
            }
        });
    }

    @Test
    public void testCan_GET_VersionFull() {
        running(testServer(PORT, Helpers.fakeApplication()), new Runnable() {
            @Override
            public void run() {

                String body = RestAssured.given()
                        .contentType("application/json")
                        .expect()
                        .statusCode(Http.Status.OK)
                        .when()
                        .get(BASE_URL + "/version-full")
                        .andReturn().body().asString();

                Assert.assertNotNull(body);

                JsonNode jsVersion = Json.parse(body);
                Assert.assertNotNull(jsVersion);

                VersionService.LongVersion version = Json.fromJson(jsVersion, VersionService.LongVersion.class);
                Assert.assertNotNull(version);
                Assert.assertEquals("poc-play-rest-backend", version.appName);
                Assert.assertEquals("1.0-SNAPSHOT", version.appVersion);
                //Assert.assertEquals("2.2.1", version.playVersion); don't work in play test mode ...
                Assert.assertEquals("1.7.0_25", version.javaVersion);
                Assert.assertEquals("Java HotSpot(TM) Server VM", version.jvmName);
                Assert.assertEquals("23.25-b01", version.jvmVersion);
                Assert.assertEquals("Europe/Paris", version.osTimezone);
                Assert.assertEquals("FR", version.osCountry);
                Assert.assertEquals("i386", version.osArch);
                Assert.assertEquals("Linux", version.osName);
                Assert.assertEquals("2.6.32-5-686", version.osVersion);
            }
        });
    }

}
