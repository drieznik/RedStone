package redStone.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class AssetsPage extends BasePage{

    @FindBy (css = "span[class*='k-label']")
    WebElement numberOfAssets;

    public AssetsPage(WebDriver driver) {
        super(driver);
        waitForContent();
    }

    private void waitForContent() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("row ng-scope")));
        } catch (TimeoutException te) {/**/}
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("row ng-scope")));
    }

    public SwaggerPage openSwaggerTab() {
        ((JavascriptExecutor)driver).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());

        driver.switchTo().window(tabs.get(1)); //switches to new tab
        driver.get("http://rsequity-test.cloudapp.net/swagger/ui/index");
        return new SwaggerPage(this.driver);

    }

}
