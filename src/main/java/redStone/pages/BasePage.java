package redStone.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BasePage {
    public WebDriver getDriver() {
        return driver;

    }

    protected WebDriver driver;
    protected WebDriverWait wait ;

    public BasePage(WebDriver driver) {

        this.driver = driver;
        wait = new WebDriverWait(this.driver, 10L);

        PageFactory.initElements(driver, this);
    }

    public RedStoneLoginPage navigateToLoginPage(String url){
        driver.get(url);
        return new RedStoneLoginPage(driver);
    }
}
