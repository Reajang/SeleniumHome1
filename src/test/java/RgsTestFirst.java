import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;

public class RgsTestFirst {
    static WebDriver driver;

    @Test
    public void testOne(){
        System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.rgs.ru");

        WebElement insuranse = driver.findElement(By.xpath("//*[contains(text(), 'Страхование')]")); //Элемент Страхование(уточнить xpath)
        insuranse.click();

        WebElement dms = driver.findElement(By.xpath("//*[contains(text(), 'ДМС')]"));
        dms.click();

        WebElement checkDmsTitle = driver.findElement(By.xpath("//*[@class='clearfix']//following-sibling::h1"));//для проверки
        String expectedString1 = "ДМС " + "\u2014" +" добровольное медицинское страхование";//длинное тире
        Assert.assertEquals(expectedString1, checkDmsTitle.getText());//проверка заголовка

        WebElement request = driver.findElement(By.xpath("//*[@class='rgs-context-bar-content-call-to-action-buttons']/a[3]"));
        request.click();

        WebElement checkRequest = driver.findElement(By.xpath("//*[@class='modal-content']"));

        WebDriverWait wait = new WebDriverWait(driver, 5, 200);//Ожидание появления окна
        wait.until(ExpectedConditions.elementToBeClickable(checkRequest));

        String expectedString2 = "Заявка на добровольное медицинское страхование";
        Assert.assertEquals(expectedString2, checkRequest.findElement(By.xpath("//*[@class='modal-title']")).getText());//Проверка заголовка

        //Ввод и проверка поля Имя
        fillFields("Имя", "Один");
        checkRequest.click();//Клик на поле checkReques как действие для окончания ввода в поле имя
        wait.until(ExpectedConditions.visibilityOf(checkRequest.findElement(By.xpath("//*[text()= 'Имя']/following::input[@class='form-control validation-control-has-success']"))));//Ожидает валидации введенных данных в поле
        String checkName = checkRequest.findElement(By.xpath("//*[text()= 'Имя']/following::input[1]")).getAttribute("value");
        Assert.assertEquals("Один", checkName);

        //Ввод и проверка поля Фамилия
        fillFields("Фамилия", "Два");
        checkRequest.click();
        wait.until(ExpectedConditions.visibilityOf(checkRequest.findElement(By.xpath("//*[text()= 'Фамилия']/following::input[@class='form-control validation-control-has-success']"))));
        String checkLastname = checkRequest.findElement(By.xpath("//*[text()= 'Фамилия']/following::input[1]")).getAttribute("value");
        Assert.assertEquals("Два", checkLastname);

        //Ввод и проверка поля Отчество
        fillFields("Отчество", "Три");
        checkRequest.click();
        wait.until(ExpectedConditions.visibilityOf(checkRequest.findElement(By.xpath("//*[text()= 'Отчество']/following::input[@class='form-control validation-control-has-success']"))));
        String checkMidname = checkRequest.findElement(By.xpath("//*[text()= 'Отчество']/following::input[1]")).getAttribute("value");
        Assert.assertEquals("Три", checkMidname);

        //Телефон
        fillFields("Телефон", "8005553535");
        checkRequest.click();
        wait.until(ExpectedConditions.visibilityOf(checkRequest.findElement(By.xpath("//*[text()= 'Телефон']/following::input[@class='form-control validation-control-has-success']"))));
        String checkTel = checkRequest.findElement(By.xpath("//*[text()= 'Телефон']/following::input[1]")).getAttribute("value");
        Assert.assertEquals("+7 (800) 555-35-35", checkTel);

        //Эл. почта
        fillFields("Эл. почта", "qwertyqwerty");
        checkRequest.click();
        //Нет ожидания. т.к. вводятся неверные данные и поля описанного ниже не появится
        //wait.until(ExpectedConditions.visibilityOf(checkRequest.findElement(By.xpath("//*[text()= 'Эл. почта']/following::input[@class='form-control validation-control-has-success']"))));
        String checkMail = checkRequest.findElement(By.xpath("//*[text()= 'Эл. почта']/following::input[1]")).getAttribute("value");
        Assert.assertEquals("qwertyqwerty", checkMail);

        //Дата контакта
        fillFields("Предпочитаемая дата контакта", "19072019");
        //checkRequest.click();
        checkRequest.findElement(By.xpath("//*[@class='modal-title']")).click();
        //wait.until(ExpectedConditions.visibilityOf(checkRequest.findElement(By.xpath("//*[text()= 'Предпочитаемая дата контакта']/following::input[@class='form-control validation-control-has-success']"))));
        String checkColl = checkRequest.findElement(By.xpath("//*[text()= 'Предпочитаемая дата контакта']/following::input[1]")).getAttribute("value");
        Assert.assertEquals("19.07.2019", checkColl);

        //Комментарий
        String comPath = "//*[text()= 'Комментарии']/following::textarea";
        driver.findElement(By.xpath(comPath)).sendKeys("Текст");
        checkRequest.click();
        String checkCom = checkRequest.findElement(By.xpath(comPath)).getAttribute("value");
        Assert.assertEquals("Текст", checkCom);

        //Регион
        List<WebElement> regions = driver.findElements(By.xpath("//*[contains(text(),'Регион')]/following::option"));//Список всех доступных регионов
        int regValue = new Random().nextInt(regions.size());
        regions.get(regValue).click();
        //wait.until(ExpectedConditions.visibilityOf(checkRequest.findElement(By.xpath("//*[@class='popupSelect form-control validation-control-has-success']"))));
        String checkReg = new Select(checkRequest.findElement(By.xpath("//*[@name='Region']"))).getFirstSelectedOption().getText();
        String selectedReg = regions.get(regValue).getText();
        Assert.assertEquals(selectedReg, checkReg);

        driver.findElement(By.xpath("//*[contains(text(),'Я согласен')]/preceding-sibling::input")).click();//Я согласен на обработку
        driver.findElement(By.xpath("//*[@id='button-m']")).click();//Отправить

        wait.until(ExpectedConditions.visibilityOf(checkRequest.findElement(By.xpath("//*[text()='Эл. почта']//following::label[1]"))));//Ожидание поля с ошибкой у поля Эл. почта
        String emailErrorMes = checkRequest.findElement(By.xpath("//*[text()='Эл. почта']//following::label[1]")).getText().trim();
        Assert.assertEquals("Введите адрес электронной почты", emailErrorMes);//Проверка что у поля Эл. почта присутствует сообщение об ошибке

        //Корректный еmail
        checkRequest.findElement(By.xpath("//*[text()= 'Эл. почта']/following::input[1]")).clear();//Удаление старых данных в графе email
        fillFields("Эл. почта", "trainee@aplana.ru");
        checkRequest.click();

        driver.close();
        driver.quit();
    }

    public static void fillFields(String name, String text){
        String path = String.format("//*[text()= '%s']/following::input[1]", name);
        driver.findElement(By.xpath(path)).sendKeys(text);
    }

}

