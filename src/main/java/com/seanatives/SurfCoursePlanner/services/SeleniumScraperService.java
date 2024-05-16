package com.seanatives.SurfCoursePlanner.services;

// Generated by Selenium IDE


import com.seanatives.SurfCoursePlanner.domain.Booking;
import com.seanatives.SurfCoursePlanner.domain.CsvBooking;
import com.seanatives.SurfCoursePlanner.domain.Guest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.time.Duration.ofSeconds;

@Service
public class SeleniumScraperService {
    // Ersetzen Sie dies durch den Pfad zu Ihrer Datei
    public static final String FILE_PATH = System.getProperty("java.io.tmpdir");

    public static final String BOOKINGLAYER_BOOKINGS = "Bookinglayer-Bookings";
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    @Value("${bookinglayer.email}")
    private String bookingLayerLoginEmail;
    @Value("${bookinglayer.password}")
    private String bookingLayerLoginPassword;

    @Autowired
    private BookingParserService bookingParserService;

    @Autowired
    private CSVFileWatcherService CSVFileWatcherService;

    public List<CsvBooking> scrapeAllBookings() throws Exception {
        setUp();
        Path bookingsCsv = loginAndDownload();
        tearDown();
        return bookingParserService.parseBookings(bookingsCsv);
    }

    public List<Guest> scrapeGuestsFor(List<Booking> bookings) {
        setUp();
        List<Guest> guests = new ArrayList<>();
        bookings.forEach(booking -> guests.addAll(scrapeParticipantsForBooking(booking)));
        driver.close();
        return guests;
    }

    private List<Guest> scrapeParticipantsForBooking(Booking booking) {
        List<Guest> guests = new ArrayList<>();
        System.out.println(format("scrape booking: %s for booker %s", booking.getBookingId(), booking.getBookerFirstName()));
        driver.get(format("https://app.bookinglayer.io/orders/%s", booking.getBookingId()));
        loginIfNeeded();
        String participants = waitForAndGetWebElement(By.id("OrderPaxLabel"), 10).getText();
        driver.findElements(By.cssSelector(".guestElement"))
                .forEach(guestElement -> {
                    Guest guest = new Guest();
                    guest.setBooking(booking);
                    guests.add(guest);
                    String guestName = guestElement.findElement(By.cssSelector(".guestElementName")).getText();
                    System.out.println(guestName);
                    guest.setName(guestName);
                    // Surf lesson adults
                    guestElement.findElements(By.cssSelector("[data-product-id='c56a8cc5-1ec5-4011-a64f-e102234acf78']"))
                            .forEach(td -> {
                                String text = td.getText();
                                System.out.println(text);
                                guest.setNumberOfSurfClassesBooked(parseNumberOfSurfCourses(text));
                            });

                    // Surf course adults
                    guestElement.findElements(By.cssSelector("[data-product-id='9beb5277-b0a6-4107-b56f-c33b74c43505']"))
                            .forEach(td -> {
                                System.out.println(td.getText());
                                guest.setNumberOfSurfClassesBooked(5);
                            });
                    // Surf course kids
                    guestElement.findElements(By.cssSelector("[data-product-id='dffa5b6e-9e78-40c6-a8ca-300f6cc1e437']"))
                            .forEach(td -> {
                                System.out.println(td.getText());
                                guest.setNumberOfSurfClassesBooked(5);
                            });

                });
        return guests;
    }

    private int parseNumberOfSurfCourses(String text) {
        System.out.println("parse:");
        System.out.println(text);
        int parseNumberOfSurfCourses = 0;
        Pattern pattern = Pattern.compile("(\\d+)x");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            parseNumberOfSurfCourses = Integer.parseInt(matcher.group(1)); // Gruppe 1 ist die erste gefundene Gruppe in Klammern
            System.out.println("Gefundene Zahl: " + parseNumberOfSurfCourses);
        } else {
            System.out.println("Keine Zahl gefunden, die dem Muster entspricht.");
        }
        System.out.println(format("to be %d", parseNumberOfSurfCourses));
        return 0;
    }


    private void loginIfNeeded() {
        try {
            waitForAndGetWebElement(By.id("login-form"), 2);
            driver.findElement(By.id("email")).sendKeys(bookingLayerLoginEmail);
            driver.findElement(By.id("password")).sendKeys(bookingLayerLoginPassword);
            driver.findElement(By.id("submit-button")).click();
        } catch (Exception e) {
            System.out.println("Could not find login form. It is assumed we are already logged in");
        }
    }

    private static Path getLatestCSVFile() {
        System.out.println(FILE_PATH);
        Path dir = Paths.get(FILE_PATH);
        Optional<Path> lastFilePath = null;

        try {
            lastFilePath = Files.list(dir)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().startsWith(BOOKINGLAYER_BOOKINGS))
                    .max(Comparator.comparingLong(file -> file.toFile().lastModified()));

            if (lastFilePath.isPresent()) {
                System.out.println("Das neueste File ist: " + lastFilePath.get());
            } else {
                System.out.println("Keine Files im Verzeichnis gefunden.");
            }
        } catch (IOException e) {
            System.err.println("Ein Fehler ist aufgetreten beim Lesen des Verzeichnisses: " + e.getMessage());
        }
        return lastFilePath.get();
    }

    private File getLatestCsvFileFrom(String filePath) {
        File dir = new File(filePath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("No files found in the directory.");
        }

        // Sortiere die Dateien nach dem letzten Änderungsdatum
        Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));

        // Die neueste Datei ist nun das erste Element in der Liste
        File latestFile = files[0];
        System.out.println("The latest file is: " + latestFile.getName());
        return latestFile;
    }

    public void setUp() {

        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", FILE_PATH);
        chromePrefs.put("safebrowsing.enabled", "true");  // Optional: Sicherheitsfeatures von Chrome aktivieren

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);

        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
    }


    private static void sleep(int timeout, String message) {
        try {
            System.out.printf("%s  - wait %d seconds ...%n", message, timeout / 1000);
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Path loginAndDownload() {
        login();

        WebElement downloadButton = waitForAndGetWebElement(By.cssSelector(".guczhc > svg"), 10);
        downloadButton.click();

        vars.put("window_handles", driver.getWindowHandles());
        driver.findElement(By.cssSelector(".hbQhqI:nth-child(1)")).click();
        Path downloadCsv = CSVFileWatcherService.waitForFileToBeWritten(FILE_PATH, BOOKINGLAYER_BOOKINGS);

        driver.close();
        return downloadCsv;
    }

    private WebElement waitForAndGetWebElement(By locator, int seconds) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, ofSeconds(seconds));
        System.out.printf("webDriver wait until %s appears...%n", locator.toString());
        WebElement webElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return webElement;
    }

    private void login() {
        driver.get("https://app.bookinglayer.io/login");
        driver.findElement(By.id("email")).sendKeys(bookingLayerLoginEmail);
        driver.findElement(By.id("password")).sendKeys(bookingLayerLoginPassword);
        driver.findElement(By.id("submit-button")).click();
    }

    public void waitForFileToBeWritten() {
        Path path = Paths.get(FILE_PATH);
        long lastModifiedTime = path.toFile().lastModified();
        long fileSize = path.toFile().length();

        while (true) {
            sleep(1000, "wait for file to be written ..");
            File file = path.toFile();

            if (file.lastModified() == lastModifiedTime && fileSize == file.length() && fileSize > 0) {
                System.out.println("File write has completed.");
                break;
            } else {
                lastModifiedTime = file.lastModified();
                fileSize = file.length();
            }
        }

        // Hier können Sie die Datei verarbeiten
        System.out.println("File is ready to be processed.");
    }

    public void tearDown() {
        driver.quit();
    }
}