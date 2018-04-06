package redStone.pages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RedStoneLoginPage extends BasePage{

    @FindBy (id = "UserName")
    WebElement userNameInput;

    @FindBy (id = "Password")
    WebElement passwordInput;

    @FindBy (className = "primary-button")
    WebElement loginBtn;


    public RedStoneLoginPage(WebDriver driver){
        super(driver);
    }



    public AssetsPage goToAssetsPage(){
        userNameInput.clear();
        userNameInput.sendKeys("admin@wolfgang.com");
        passwordInput.clear();
        passwordInput.sendKeys("k9Dq7WGtLK");
        loginBtn.click();
        return new AssetsPage(this.driver);
    }
}
