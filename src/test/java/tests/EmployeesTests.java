package tests;

import io.restassured.RestAssured;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import steps.EmployeesSteps;

@RunWith(SerenityRunner.class)
public class EmployeesTests {

    @Steps
    EmployeesSteps employeesSteps;

    @BeforeClass
    public static void setupURL() {
        RestAssured.baseURI = "http://dummy.restapiexample.com";
        RestAssured.basePath = "/api/v1";
    }

    @Test
    public void TestScenario() {
        employeesSteps.getAllEmployees();
        employeesSteps.statusCodeShouldBe(200);
        employeesSteps.getNoOfEmployeesOverThirty();
        employeesSteps.statusCodeShouldBe(200);
        employeesSteps.addNewEmployee();
        employeesSteps.statusCodeShouldBe(200);
        employeesSteps.updateNewlyCreatedEmployee();
        employeesSteps.statusCodeShouldBe(200);
        employeesSteps.getNewEmployee();
        employeesSteps.valueForFieldInPathShouldBe
                ("data.employee_salary", employeesSteps.getNewEmployeeUpdatedSalary());
        employeesSteps.valueForFieldInPathShouldBe
                ("data.employee_name", employeesSteps.getNewEmployeeUpdatedName());
        employeesSteps.getAllEmployees();
        employeesSteps.valueForFieldInPathShouldBe
                ("data.findAll { it.employee_age > 30 }.size()", employeesSteps.getNoOfEmployeesOver30() + 1);
        employeesSteps.deleteEmployee(employeesSteps.getIdNewEmployee());
        employeesSteps.statusCodeShouldBe(200);
        employeesSteps.getNewEmployee();
        employeesSteps.valueForFieldInPathShouldBe
                ("data", null);
    }
}
