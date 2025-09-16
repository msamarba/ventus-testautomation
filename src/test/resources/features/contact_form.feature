Feature: Ventus contact form

  @XSP-58
  Scenario: Submit the contact form on the German site
    Given I open "https://www.ventus-itservices.de/"
    And I navigate to the "Kontakt" section
    When I fill the contact form with:
      | field     | value                  |
      | Vorname   | Anna                   |
      | Nachname  | Schulz                 |
      | Email    | anna.schulz@example.de |
      | Nachricht | Hallo, bitte melden.   |
    And I accept the privacy consent checkbox
    And I submit the form
 #   Then I see a confirmation message