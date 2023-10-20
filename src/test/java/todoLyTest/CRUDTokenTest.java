package todoLyTest;

import config.Configuration;
import factoryRequest.FactoryRequest;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class CRUDTokenTest extends TestBaseToken {

    @Test
    public void createUpdateDeleteProject() {
        JSONObject body = new JSONObject();
        body.put("Content", "Refactor");

        this.createProject(Configuration.host + "/api/projects.json", body, post);

        this.deleteToken(Configuration.host+"/api/authentication/token.json",delete);


        JSONObject body1 = new JSONObject();
        body1.put("Content", "Refactor");

        this.createProject(Configuration.host + "/api/projects.json", body1, post);


    }
    private void createProject(String host, JSONObject body, String post) {
        requestInfo.setUrl(host)
                .setBody(body.toString());
        response = FactoryRequest.make(post).send(requestInfo);
        response.then().statusCode(200).
                body("Content", equalTo(body.get("Content")));
    }

    private void deleteToken(String host, String delete) {
        requestInfo.setUrl(host);
        response = FactoryRequest.make(delete).send(requestInfo);
    }








}