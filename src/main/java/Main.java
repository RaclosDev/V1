import gui.Gui;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main {
    private static ChromeDriver driver;
    private static String baseUrl;
    private static boolean acceptNextAlert = true;
    private static StringBuffer verificationErrors = new StringBuffer();
    private static String rutaDriver = "D:\\workspace\\chromedriver.exe";
    private static String rutaChrome = "C:\\Users\\Carlos\\AppData\\Local\\Google\\Chrome\\User Data\\Profile 1";
    private static ArrayList<WebElement> mensajes = new ArrayList<>();
    private static ArrayList<WebElement> archivoMensajes = new ArrayList<>();
    private static ArrayList<WebElement> chats = new ArrayList<>();
    private static String stringMaldoChannel = "MALDO - SEMANAL";
    private static String stringPruebasChannel = "Pruebas";
    private static String stringMaldoReenvio = "MALDO - REENVIO";

    public static void main(String[] args) {
        try {
            setUp();
            //bet365();
            testTelegram();
            shutdownChrome();
        } catch (Exception e) {
            e.printStackTrace();
            shutdownChrome();
        }
    }

    public static void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", rutaDriver);
        ChromeOptions options = new ChromeOptions();

        // loading Chrome with my existing profile instead of a temporary profile
        options.addArguments("user-data-dir=" + rutaChrome);
        //options.addArguments("headless");
        options.addArguments("window-size=1920,1080");

        Gui gui = new Gui();
        gui.setVisible(true);
        driver = new ChromeDriver(options);
        baseUrl = "https://www.google.com/";
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }

    public static void setUp2() throws Exception {
        //Change chrome driver path according
        System.setProperty("webdriver.chrome.driver", rutaDriver);
        ChromeOptions options = new ChromeOptions();
        //options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
        driver = new ChromeDriver(options);
        System.out.println(driver.getTitle());

    }

    public static void setUp3() throws Exception {
        //Change chrome driver path according
        System.setProperty("webdriver.chrome.driver", rutaDriver);
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        System.out.println(driver.getTitle());
        //(CdpRemoteWebDriver) driver).executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", {"source": """Object.defineProperty(navigator, 'maxTouchPoints', {get: () => 1})"""});


    }


    public static void shutdownChrome() {
        driver.close();
        driver.quit();
    }

    public static void bet365() {
        Map<String, Object> params = new HashMap<String, Object>();
        driver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", params);
        driver.get("https://www.bet365.es");
        driver.findElement(By.xpath("//div[4]/div/div")).click();
        driver.findElement(By.xpath("//input[@type='text']")).click();
        driver.findElement(By.xpath("//input[@type='text']")).clear();
        driver.findElement(By.xpath("//input[@type='text']")).sendKeys("Tensung FC v Paro Rinpung Fc ");
        driver.findElement(By.xpath("//div/div/div/div[2]/div/div/div/div/div[2]")).click();
        driver.findElement(By.xpath("//div[2]/div/div/div[2]/div/div/div/div[2]/div/div/div/div/div/div[2]")).click();
        driver.findElement(By.xpath("//div[2]/div/div[2]/div/div/div/div[2]/div[2]/div[2]")).click();
        driver.findElement(By.xpath("//div[5]/div/div/div/div[3]/div")).click();

        params.put("source", "Object.defineProperty(navigator, 'maxTouchPoints', {get: () => 1})");

    }

    public static void testTelegram() throws Exception {
        driver.get("https://web.telegram.org/#/login");
        if (!isLogged()) {
            login();
        }
        findChannel(stringMaldoChannel);
        Thread.sleep(1000);
        loadMensajes();
        while (true) {
            refreshMensajes();
            if (mensajesNuevos()) {
                reenviaMensajes();
                loadMensajes();
            }
        }
    }

    public static boolean isLogged() {
        try {
            ArrayList<WebElement> chats = new ArrayList(driver.findElements(By.className("im_dialog_peer")));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void login() throws Exception {
        driver.findElement(By.xpath("//html[@id='ng-app']/body/div/div/div[3]/div[2]/form/div/div")).click();
        driver.findElement(By.xpath("//input[@type='search']")).clear();
        driver.findElement(By.xpath("//input[@type='search']")).sendKeys("espa");
        driver.findElement(By.xpath("//html[@id='ng-app']/body/div[5]/div[2]/div/div/div[2]/div[2]/div/div/ul/li/a")).click();
        driver.findElement(By.name("phone_number")).click();
        driver.findElement(By.name("phone_number")).clear();
        driver.findElement(By.name("phone_number")).sendKeys("655110459");
        driver.findElement(By.xpath("//html[@id='ng-app']/body/div/div/div[3]/div/div/a/my-i18n")).click();
        driver.findElement(By.xpath("//html[@id='ng-app']/body/div[5]/div[2]/div/div/div[2]/button[2]/span")).click();
        driver.findElement(By.name("phone_code")).clear();
        Thread.sleep(10000);
    }

    public static void findChannel(String channelName) throws Exception {
        chats = new ArrayList(driver.findElements(By.className("im_dialog_peer")));
        for (WebElement chat : chats) {
            if (chat.getText().contains(channelName)) {
                chat.click();
                break;
            }
        }
    }

    public static void loadMensajes() throws Exception {
        findChannel(stringMaldoChannel);
        mensajes = new ArrayList(driver.findElements(By.className("im_history_message_wrap")));
        archivoMensajes = mensajes;
    }

    public static void refreshMensajes() throws Exception {
        mensajes = new ArrayList(driver.findElements(By.className("im_history_message_wrap")));
    }

    public static boolean mensajesNuevos() throws Exception {
        if (mensajes.size() != archivoMensajes.size()) {
            return true;
        }
        return false;
    }

    public static void reenviaMensajes() throws Exception {

        String imgName = "foto.png";
        String path = "c:\\botTelegram";
        Files.createDirectories(Paths.get(path));
        Path imagesPath = Paths.get(path + "\\" + imgName);
        Screenshot screenshot = null;
        Collections.reverse(mensajes);

        for (WebElement mensaje : mensajes) {
            if (!archivoMensajes.contains(mensaje)) {
                findChannel(stringMaldoChannel);
                String stringMensaje = mensaje.findElement(By.className("im_message_text")).getText();
                try {
                    if (stringMensaje.isEmpty()) {
                        //Si es foto
                        WebElement elementoFoto = mensaje.findElement(By.className("im_message_photo_thumb"));
                        elementoFoto.click();
                        screenshot = new AShot().takeScreenshot(driver, mensaje.findElement(By.xpath("//*[@id=\"ng-app\"]/body/div[6]/div[5]/div/div/div/div/div/div[2]/a/img")));
                        driver.findElement(By.xpath("/html/body/div[6]/div[3]")).click();
                        ImageIO.write(screenshot.getImage(), "PNG", new File(path + "\\" + imgName));
                        //Envia
                        findChannel(stringMaldoReenvio);
                        pickImage(imagesPath);
                        Files.delete(imagesPath);
                    } else {
                        //Si no es foto
                        findChannel(stringMaldoReenvio);
                        driver.findElement(By.className("composer_rich_textarea")).sendKeys(stringMensaje);
                        driver.findElement(By.className("composer_rich_textarea")).sendKeys(Keys.ENTER);
                    }
                } catch (Exception e) {
                    System.out.println(e.getCause());
                }
            } else {
                break;
            }

        }
    }


    public static void pickImage(Path imagesPath) throws Exception {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', arguments[1])", driver.findElement(By.xpath("//input[@type='file']")), "0");
        js.executeScript("arguments[0].setAttribute('class', arguments[1])", driver.findElement(By.xpath("//input[@type='file']/../../div[2]")), "a");
        driver.findElement(By.xpath("//input[@type='file']")).sendKeys(imagesPath.toString());
        Thread.sleep(1000);
    }

}

