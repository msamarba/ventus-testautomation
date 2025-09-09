package bdd.steps;

import bdd.Hooks;
import io.cucumber.java.en.*;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import pages.ContactPage;
import pages.HomePage;

import java.util.Map;

public class VentusSteps {
    WebDriver d(){ return Hooks.driver(); }
    HomePage home;
    ContactPage contact;

    @Given("I open {string}")
    public void i_open(String url) {
        home = new HomePage(d()).open(url);
    }

    @Given("I navigate to the {string} section")
    public void i_navigate_to_section(String section) {
        // we only need Kontakt now; extend if needed
        contact = home.goToKontakt();
    }

    @When("I fill the contact form with:")
    public void i_fill(io.cucumber.datatable.DataTable table) {
        Map<String,String> m = table.asMap(String.class, String.class);
        contact
                .fill("Vorname",   m.get("Vorname"))
                .fill("Nachname",  m.get("Nachname"))
                .fill("E-Mail",    m.get("E-Mail"))
                .fill("Betreff",   m.get("Betreff"))
                .fill("Nachricht", m.get("Nachricht"));
    }

    @When("I accept the privacy/consent checkbox")
    public void i_accept_privacy() { contact.acceptPrivacy(); }

    @When("I submit the form")
    public void i_submit() { contact.submit(); }

    @Then("I see a confirmation message")
    public void i_see_confirmation() {
        Assertions.assertThat(contact.confirmationVisible())
                .as("Confirmation text should be visible").isTrue();
    }
}