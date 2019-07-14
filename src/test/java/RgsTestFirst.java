import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
        /*String expectedString2 = "Заявка на добровольное медицинское страхование";
        Assert.assertEquals(expectedString2, checkRequest.findElement(By.xpath("//*[@class='modal-header']")).getText());//НЕ РАБОТАЕТ*/

        WebDriverWait wait = new WebDriverWait(driver, 6, 200);//Ожидание появления окна
        wait.until(ExpectedConditions.elementToBeClickable(checkRequest));

        /*String stringForm = "//*[text()= '%s']/following::input[1]";
        String path = String.format(stringForm, "Имя");*/
        fillFields("Имя", "Один");
        //Assert.assertEquals("Один", driver.findElement(By.xpath(path)).getText());

        fillFields("Фамилия", "Два");
        fillFields("Отчество", "Три");
        fillFields("Телефон", "8005553535");
        fillFields("Эл. почта", "qwertyqwerty");
        fillFields("Предпочитаемая дата контакта", "19072019");
        driver.findElement(By.xpath("//*[text()= 'Комментарии']/following::textarea")).sendKeys("Текст");//Комментарии

        List<WebElement> regions = driver.findElements(By.xpath("//*[contains(text(),'Регион')]/following::option"));//Выбор региона
        regions.get(new Random().nextInt(84)).click();

        driver.findElement(By.xpath("//*[contains(text(),'Я согласен')]/preceding-sibling::input")).click();//Я согласен на обработку
        driver.findElement(By.xpath("//*[@id='button-m']")).click();//Отправить

        Assert.assertEquals("Введите адрес электронной почты", driver.findElement(By.xpath("//*[text()='Эл. почта']/following::span[1]")).getText());

        driver.close();
        driver.quit();
    }

    public static void fillFields(String name, String text){
        String path = String.format("//*[text()= '%s']/following::input[1]", name);
        driver.findElement(By.xpath(path)).sendKeys(text);
    }

}

//*[text()='Эл. почта']/following::*[@class='validation-error-text']