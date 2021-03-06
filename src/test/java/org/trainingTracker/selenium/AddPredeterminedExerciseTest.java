package org.trainingTracker.selenium;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.trainingTracker.database.dataAccesObject.UsersDAO;

import java.util.Iterator;

import static org.junit.Assert.assertFalse;
import static org.trainingTracker.selenium.TestUtils.*;

/**
 * Test class to check if the process to add a new predetermined exercise
 * to the home page works correctly.
 */
public class AddPredeterminedExerciseTest {

    private static WebDriver driver;
    private static final String ADD_EXERCISE_BUTTON = "addPredButton";
    private static final String MG_SELECT = "selectMGPredetermined";
    private static final String EXERCISE_SELECT = "selectExercise";
    private static final String SUCCESS_MESSAGE = "successAddingExercise";

    @BeforeClass
    public static void setUp(){
        boolean res = UsersDAO.addUser(USERNAME, PASS, EMAIL);
        System.out.println("***** EJECUTA CREATE EN PRED: " + res);
        driver = new FirefoxDriver();
        driver.get(STARTER_URL);
        try{
            goToStarter(driver);
            login(driver);
            goToAddExercise(driver);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
     * Tests the process of adding a new predetermined
     * exercise to the home page. The method test all
     * the existing muscle groups and the exercises
     * within that muscle groups.
     */
    @Test
    public void addPredeterminedExerciseTest(){
        try{
            selectPredetermined();
            WebElement addButton = driver.findElement(By.name(ADD_EXERCISE_BUTTON));
            // Finds the select with the muscle group options
            Select select = new Select(driver.findElement(By.id(MG_SELECT)));
            Iterator<WebElement> iter1 = select.getOptions().iterator();
            // Skips the first option in the select (which is blank)
            iter1.next();
            // Iterates all the muscle group options
            while (iter1.hasNext()){
                iter1.next().click();
                Thread.sleep(SLEEP_FOR_DISPLAY);
                // Findes the select with the exercise options for the selected muscle group
                select = new Select(driver.findElement(By.id(EXERCISE_SELECT)));
                Iterator<WebElement> iter2 = select.getOptions().iterator();
                // Skips the first option in the select (which is blank)
                iter2.next();
                // Iterates all the exercise options for that muscle group
                while (iter2.hasNext()){
                    // Tries to add a new exercise with the selected options and check if the process has been successful.
                    iter2.next().click();
                    Thread.sleep(SLEEP_FOR_DISPLAY);
                    addButton.click();
                    Thread.sleep(SLEEP_FOR_DISPLAY);
                    assertFalse(driver.findElements(By.name(SUCCESS_MESSAGE)).isEmpty());
                }
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
     * Selects the "Predetermined" option on the "addExercise" page.
     */
    private static void selectPredetermined() throws InterruptedException{
        WebElement element;
        element = driver.findElement(By.name("predButton"));
        element.click();
        Thread.sleep(SLEEP_FOR_DISPLAY);
    }

    @AfterClass
    public static void tearDown(){
        driver.close();
        driver.quit();
        System.out.println("EJECUTA DELETE EN PRED: " + UsersDAO.deleteUser(USERNAME));
    }
}

