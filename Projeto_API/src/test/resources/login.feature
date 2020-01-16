Story: Pedro wants to log-in into his account

As a CloudDeliveries user, Pedro wants to access to his account so he can start working


Scenario 1: Pedro inserts incorrect credentials 
GIVEN Pedro in on the log-in page 
WHEN he enters incorrect credentials and then clicks ‘log In’
THEN he gets redirected to an error page


Scenario 2: Pedro supplies correct user name and password
 GIVEN that Pedro is on the log-in page
 WHEN he enters his user name and password correctly
 And clicks ‘log In’
 THEN he is taken to the Home page
 AND user object session is created 
