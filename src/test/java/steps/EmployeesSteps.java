package steps;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import util.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class EmployeesSteps {

    private Response response;
    private int noOfEmployeesOverThirty;
    private int newEmployeeId;
    private JSONObject newEmployeeAttributes;
    private int newEmployeeUpdatedSalary;
    private String newEmployeeUpdatedName;

    @Step
    public void getAllEmployees() {
        response = given().contentType(ContentType.JSON)
                .log().method().log().uri().log().body()
                .get("/employees");
        response.then().log().body().assertThat().body("status", is("success"));
    }

    @Step
    public void statusCodeShouldBe(int statusCode) {
        response.then().log().all()
                .statusCode(statusCode);
    }

    @Step
    public void getNoOfEmployeesOverThirty() {
        String responseString = response.asString();
        JsonPath jsonPath = new JsonPath(responseString);
        List<Object> list = jsonPath.getList("data.findAll { it.employee_age > 30 }");
        noOfEmployeesOverThirty = list.size();
        Serenity.recordReportData().withTitle("Number of employees over thirty")
                .andContents(String.valueOf(noOfEmployeesOverThirty));
    }

    @Step
    public void valueForFieldInPathShouldBe(String path, Object expectedValue) {
        response.then().body(path, equalTo(expectedValue));
    }

    @Step
    public void addNewEmployee() {
        String fileSeparator = "/";
        if (System.getProperty("file.separator").equals("\\")) {
            fileSeparator = "\\";
        }
        String employeeName = RandomStringUtils.randomAlphabetic(6) + " " + RandomStringUtils.randomAlphabetic(6);
        int employeeAge = (int) (31 + (65 - 31) * Math.random());
        int employeeSalary = (int) (1000 + (100000 - 1000) * Math.random());
        String jsonbody = null;
        try {
            jsonbody = Utils.generateStringFromResource("src" + fileSeparator + "test" + fileSeparator + "resources" +
                    fileSeparator + "postEmployee.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jObject  = new JSONObject(jsonbody);
        jObject.remove("employee_name");
        jObject.remove("employee_salary");
        jObject.remove("employee_age");
        jObject.put("employee_name", employeeName);
        jObject.put("employee_salary", employeeSalary);
        jObject.put("employee_age", employeeAge);
        response = given().contentType(ContentType.JSON)
                .body(jObject.toString())
                .log().method().log().uri().log().body()
                .post("/create");
        response.then().log().body().assertThat().body("status", is("success"));
        newEmployeeId = response.then().extract().path("data.id");
        HashMap<String, Object> map =  response.then().extract().path("data");
        newEmployeeAttributes = new JSONObject(map);
    }

    public void updateNewlyCreatedEmployee() {
        newEmployeeUpdatedName = RandomStringUtils.randomAlphabetic(6) + " " + RandomStringUtils.randomAlphabetic(6);
        newEmployeeUpdatedSalary = (int) (1000 + (100000 - 1000) * Math.random());
        newEmployeeAttributes.remove("employee_name");
        newEmployeeAttributes.remove("employee_salary");
        newEmployeeAttributes.remove("id");
        newEmployeeAttributes.put("employee_name", newEmployeeUpdatedName);
        newEmployeeAttributes.put("employee_salary", newEmployeeUpdatedSalary);
        response = given().contentType(ContentType.JSON)
                .body(newEmployeeAttributes.toString())
                .pathParam("id", newEmployeeId)
                .log().method().log().uri().log().body()
                .put("/update/{id}");
        response.then().assertThat().body("status", is("success"));
    }

    public int getNoOfEmployeesOver30() {
        return this.noOfEmployeesOverThirty;
    }

    public int getIdNewEmployee() {
        return newEmployeeId;
    }

    @Step
    public void deleteEmployee(int employeeId) {
        response = given().contentType(ContentType.JSON)
                .pathParam("id", employeeId)
                .log().method().log().uri().log().body()
                .delete("/delete/{id}");
        response.then().assertThat().body("status", is("success"));
    }

    @Step
    public void getNewEmployee() {
        response = given().contentType(ContentType.JSON)
                .pathParam("id", newEmployeeId)
                .log().method().log().uri().log().body()
                .get("/employee/{id}");
        response.then().log().body().assertThat().body("status", is("success"));

    }

    public int getNewEmployeeUpdatedSalary() {
        return newEmployeeUpdatedSalary;
    }

    public String getNewEmployeeUpdatedName() {
        return newEmployeeUpdatedName;
    }
}
