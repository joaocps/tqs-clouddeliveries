Feature: Pedro wants to change his account information

  Scenario Outline: Pedro inserts correct values
    Given Pedro is logged in
    And he is in the changeuserprofile page
    When he inserts a correct "<field>"
    And then clicks 'submit'
    Then is redirected to userhome
    And his "field" is updated
    
    
  Examples: 
    | field |
    | address |
    | firstname |
    
    
  Scenario: Pedro inserts incorrect values
    Given Pedro is logged in
    And he is in the changeuserprofile page
    When he inserts incorrect fields
    And then clicks 'submit'
    Then is redirected to error page
    And his fields are not updated