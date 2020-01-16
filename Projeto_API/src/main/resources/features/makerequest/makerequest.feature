Feature: Pedro wants to make a delivery


  Scenario: Pedro inserts correct values and can make a request
  	Given Pedro is logged-in
    And Pedro is on the make-order page
    And can make a request
    When he enters correct credentials
    And then clicks ‘submit’
    Then he gets redirected to the userhome page
    And the order is created


  Scenario: Pedro inserts incorrect values and can make a request
  	Given Pedro is logged-in
    And Pedro is on the make-order page
    And can make a request
    When he enters incorrect credentials
    And then clicks ‘submit’
    Then he gets redirected to the error page
    And the order is not created
    
  Scenario: Pedro inserts correct values and cant make a request
  	Given Pedro is logged-in
    And Pedro is on the make-order page
    And cant make a request
    When he enters correct credentials
    And then clicks ‘submit’
    Then he gets redirected to the userhome page
    And the order is created