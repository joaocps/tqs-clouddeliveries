Feature: Pedro, As an user, wants to sign in so he can use CloudDeliveries

  Scenario: Pedro supplies correct information
    Given Pedro is in signup page
    And his account is not created
    When he inserts correct credentials
    And then clicks ‘Register’
    Then he is redirected to 'choose service' page
    Then he chooses his user type
    Then a CloudDeliveries account is created
    And he is redirected to 'login page'


  Scenario: Pedro supplies incorrect information
    Given Pedro is in signup page
    And his account is not created
    When he inserts incorrect credentials
    And then clicks ‘Register’
    Then a CloudDeliveries account is not created
    And he is redirected to 'error' page
