package defstep.basepage.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import io.cucumber.java.en.Given;
import setup.drivers.DriverUtils;

public class GoogleSteps {

    @Given("I open Google")
    public void i_open_google() {
        WebDriver driver = DriverUtils.getDriver();
        driver.get("https://www.google.com");

        System.out.println("Title: " + driver.getTitle());
        System.out.println("Thread here: " + Thread.currentThread().getId());
    }

    @Given("I open url {string}")
    public void goToUrl(String url) {
        WebDriver driver = DriverUtils.getDriver();
        driver.get(url);
    }

    @Given("Click on Platform menu")
    public void clickOnPlatform() throws InterruptedException {
        WebDriver driver = DriverUtils.getDriver();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//ul[@class='breakdance-menu-list']//a[contains(text(),'Platform')]")).click();
    }

}
