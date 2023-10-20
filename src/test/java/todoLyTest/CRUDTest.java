package todoLyTest;

import config.Configuration;
import factoryRequest.FactoryRequest;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.util.LinkedList;
import static org.hamcrest.Matchers.equalTo;



public class CRUDTest extends TestBaseAuthBasic {

    LinkedList<Integer> listaty= new LinkedList<>();
    LinkedList<Integer> listaty1= new LinkedList<>();
    @Test
    public void createUpdateDeleteProject(){
        JSONObject body = new JSONObject();
        body.put("Content","Refactor");

        this.createProject(Configuration.host + "/api/projects.json", body, post);
        int idProject = response.then().extract().path("Id");
        this.readProject(idProject, get, body);
        body.put("Content","Refactor1");
        this.updateProject(Configuration.host + "/api/projects/" + idProject + ".json", body, put);
        this.deleteProject(idProject, delete, body);
    }

    private void deleteProject(int idProject, String delete, JSONObject body) {
        requestInfo.setUrl(Configuration.host + "/api/projects/" + idProject + ".json");
        response = FactoryRequest.make(delete).send(requestInfo);
    }

    private void updateProject(String host, JSONObject body, String put) {
        requestInfo.setUrl(host)
                .setBody(body.toString());
        response = FactoryRequest.make(put).send(requestInfo);
        response.then().statusCode(200).
                body("Content", equalTo(body.get("Content")));
    }

    private void readProject(int idProject, String get, JSONObject body) {
        requestInfo.setUrl(Configuration.host + "/api/projects/" + idProject + ".json");
        response = FactoryRequest.make(get).send(requestInfo);
        response.then().statusCode(200).
                body("Content", equalTo(body.get("Content")));
    }

    @Test
    public void createUserTest() {
        JSONObject userBody = new JSONObject();
        userBody.put("Email", Configuration.user1);
        userBody.put("FullName", "Luciano Vargas");
        userBody.put("Password", Configuration.password1);

        this.createUser(Configuration.host + "/api/user.json", userBody, post);
        int idUser = response.then().extract().path("Id");
        JSONObject body = new JSONObject();
        body.put("Content","Refactor");
        this.createProject(Configuration.host + "/api/projects.json", body, post);

      /*  this.deleteUser(Configuration.host + "/api/user/"+idUser+".json", delete);

        JSONObject projectBody = new JSONObject();
        projectBody.put("Content","Refactor");


        this.createProject(Configuration.host + "/api/projects.json", projectBody, post);
        response.then().statusCode(105);
        response.then().body("error", equalTo("User not found"));*/

    }

    @Test
    public void createAndDeleteAllProjects() {
        JSONObject projectBody = new JSONObject();

        for (int i = 1; i <= 4; i++) {
            projectBody.put("Content", "Proyecto" + i);
            this.createProject(Configuration.host + "/api/projects.json", projectBody, post);
            listaty.push(response.then().extract().path("Id"));
        }

        for (int i = 0; i < 4; i++) {
            this.deleteProject(listaty.pop(), delete,projectBody);
        }

    }
    @Test
    public void createAndDeleteUsers() {
        JSONObject userBody = new JSONObject();
        for (int i = 1; i <= 4; i++) {
            userBody.put("Email", "hola@hola1678"+i+i+".com");
            userBody.put("Password","hola1234");
            userBody.put("FullName", "Luciano Vargas");

            this.createUser(Configuration.host + "/api/user.json", userBody, post);
            listaty1.push(response.then().extract().path("Id"));
        }
        while (!listaty1.isEmpty()) {
                    this.deleteUser(Configuration.host + "/api/user/"+listaty1.pop()+".json", delete);
        }
    }

    private void createProject(String host, JSONObject body, String post) {
        requestInfo.setUrl(host)
                .setBody(body.toString());
        response = FactoryRequest.make(post).send(requestInfo);
        response.then().statusCode(200).
                body("Content", equalTo(body.get("Content")));
    }


    private void createUser(String host, JSONObject body, String post) {
        requestInfo.setUrl(host)
                .setBody(body.toString());
        response = FactoryRequest.make(post).send(requestInfo);
        response.then().statusCode(200);
        response.then().body("FullName", equalTo(body.get("FullName")));
    }

    private void deleteUser(String url, String delete) {
        requestInfo.setUrl(url);
        response = FactoryRequest.make(delete).send(requestInfo);
    }


}
