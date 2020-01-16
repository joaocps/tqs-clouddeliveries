Feature: Pedro wants to log-in into his account
  As a CloudDeliveries user, Pedro wants to access to his account so he can start working


  Scenario: Pedro inserts incorrect credentials 
    Given Pedro is on the log-in page 
    When he enters incorrect credentials
    And then clicks ‘log In’
    Then he gets redirected to an error page


  Scenario: Pedro inserts correct credentials
    Given Pedro is on the log-in page
    When he enters correct credentials
    And then clicks ‘log In’
    Then he gets redirected to the Home page
    And user object session is created 
