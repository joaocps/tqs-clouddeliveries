Feature: Pedro wants comment a finished order
  

  Scenario: Pedro trys to comment an order that isnt finished 
    Given Pedro is logged in
    And no order is finished
    And is on userhome page
    When Pedro searches for the button to comment
    Then nothing shows up
  

  Scenario: Pedro trys to comment an order that is finished 
    Given Pedro is logged in
    And at least one order is finished
    And is on userhome page
    When Pedro searches for the button to comment
    Then the button shows
    And the user writes and introduces the comment
    
    
