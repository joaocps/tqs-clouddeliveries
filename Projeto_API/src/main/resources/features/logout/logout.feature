Feature: Pedro wants to logout from his account
  

  Scenario Outline: Pedro trys to logout
    Given Pedro is logged in
    And is on the "<page>" page
    When he trys to logout
    Then he gets redirected to the home page
    And the session is cleaned
    
  
 
  Examples:
    | page |           
    | userhome |        
    | makeRequest |
    | userprofile |
    
    


  
