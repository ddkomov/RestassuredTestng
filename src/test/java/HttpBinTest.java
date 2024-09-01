import io.qameta.allure.Attachment;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import spec.Specifications;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

//@Listeners(LogListener.class)
public class HttpBinTest {
    private static final String BASE_URL = "https://httpbin.org/";

    @Test
    public void getTest() {
        Specifications.installSpecification(Specifications.requestSpec(BASE_URL), Specifications.responseSpecOK200());
        Response response = given()
                .filter(new AllureRestAssured())
                .when()
                .get("/get")
                .then().log().all()
                .extract().response();
    }

    @Test
    public void successBasicAuthTest() {
        Specifications.installSpecification(Specifications.requestSpec(BASE_URL), Specifications.responseSpecOK200());
        String user = "user";
        String passwd = "passwd";
        Response res = given()
                .filter(new AllureRestAssured())
                .auth().preemptive().basic(user, passwd)
                .when()
                .get("basic-auth/user/passwd")
                .then().log().all()
                .extract().response();
        JsonPath jsonPath = new JsonPath(res.asString());
        Assert.assertEquals(jsonPath.get("authenticated"), true);
        Assert.assertEquals(jsonPath.get("user"), user);
    }

    @Test
    public void errorBasicAuthTest() {
        Specifications.installSpecification(Specifications.requestSpec(BASE_URL), Specifications.responseSpec(401));
        String user = "userr";
        String passwd = "passwd";
        Response res = given()
                .filter(new AllureRestAssured())
                .auth().preemptive().basic(user, passwd)
                .when()
                .get("basic-auth/user/passwd")
                .then().log().all()
                .extract().response();
    }

    @Test
    public void successBearerAuthTest() {
        Specifications.installSpecification(Specifications.requestSpec(BASE_URL), Specifications.responseSpecOK200());
        String token = "token";
        Response res = given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + token)
                .when()
                .get("bearer")
                .then().log().all()
                .extract().response();
        JsonPath jsonPath = new JsonPath(res.asString());
        Assert.assertEquals(jsonPath.get("token"), token);
    }

    @Test
    public void errorBearerAuthTest() {
        Specifications.installSpecification(Specifications.requestSpec(BASE_URL), Specifications.responseSpec(401));
        String token = "token";
        Response res = given()
                .filter(new AllureRestAssured())
                .header("Authorization", token)
                .when()
                .get("bearer")
                .then().log().all()
                .extract().response();
    }

    @Test
    public void successDigestAuthTest() {
        Specifications.installSpecification(Specifications.requestSpec(BASE_URL), Specifications.responseSpecOK200());
        String user = "user";
        String passwd = "passwd";
        Response res = given()
                .filter(new AllureRestAssured())
                .auth().digest(user, passwd)
                .when()
                .get("digest-auth/auth/user/passwd")
                .then().log().all()
                .extract().response();
        JsonPath jsonPath = new JsonPath(res.asString());
        Assert.assertEquals(jsonPath.get("authenticated"), true);
        Assert.assertEquals(jsonPath.get("user"), user);
    }

    @Test
    public void errorDigestAuthTest() {
        Specifications.installSpecification(Specifications.requestSpec(BASE_URL), Specifications.responseSpec(401));
        Response res = given()
                .filter(new AllureRestAssured())
                .auth().digest("", "")
                .when()
                .get("digest-auth/auth/user/passwd")
                .then().log().all()
                .extract().response();
    }

    @Test
    public void successHeadersTest() {
        Specifications.installSpecification(Specifications.requestSpec(BASE_URL), Specifications.responseSpecOK200());
        Response res = given()
                .filter(new AllureRestAssured())
                .when()
                .get("/headers")
                .then().log().all()
                .extract().response();

    }
    @Test
    public void successIPTest() {
        Specifications.installSpecification(Specifications.requestSpec(BASE_URL), Specifications.responseSpecOK200());
        Response res = given()
                .filter(new AllureRestAssured())
                .when()
                .get("/ip")
                .then().log().all()
                .extract().response();

    }
    @Test
    public void successUserAgentTest() {
        Specifications.installSpecification(Specifications.requestSpec(BASE_URL), Specifications.responseSpecOK200());
        Response res = given()
                .filter(new AllureRestAssured())
                .when()
                .get("/user-agent")
                .then().log().all()
                .extract().response();

    }
    @Test
    public void errorCacheTest() {
        Specifications.installSpecification(Specifications.requestSpec(BASE_URL), Specifications.responseSpec(304));

        Response res = given()
                .filter(new AllureRestAssured())
                .header("If-Modified-Since", "If-Modified-Since")
                .when()
                .get("cache")
                .then().log().all()
                .extract().response();

    }
    @Test
    public void succesResponseHeadersTest() {
        Specifications.installSpecification(Specifications.requestSpec(BASE_URL), Specifications.responseSpecOK200());
        String freeform = "123";
        int fflength = freeform.length();
        int cl = Integer.parseInt("87") + fflength;
        String clstring = String.valueOf(cl);
        String ffstringlength = String.valueOf(fflength);
        Response res = given()
                .filter(new AllureRestAssured())
                .when()
                .post("response-headers?freeform=" + freeform)
                .then().log().all()
                .extract().response();
        JsonPath jsonPath = new JsonPath(res.asString());
        Assert.assertEquals(jsonPath.get("freeform"), freeform);
        Assert.assertEquals(jsonPath.get("Content-Length"), clstring);
    }

}
