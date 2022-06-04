Feature: User Test Suite

  @adduser
  Scenario: Verify an user can be added
    Given the details of user has been added
    And the response code for add user is 200
    Then the body response contains the "<id>" of the user created

    Examples:
      |   id    |
      |  444222 |

  @getuser
  Scenario: Get a user by name
    Given we have the name of the user
    And the response code for get user is 200
    Then the body response contains the "<name>" of the user

    Examples:
      |   name    |
      |  perico   |

  @updateuser
  Scenario: Verify a user can be updated
    Given the details of the user has been updated
    And the response code for update user is 200
    Then the body response contains the updated "<phone>"

    Examples:
      |   phone    |
      |   67777    |

  @deleteuser
  Scenario: Verify a user can be deleted
  Given the user is deleted by username
  Then the body response is empty

  @loginuser
  Scenario: Verify a user can log in
  Given the login details username and password
  Then the response code for login user is 200

  @logoutuser
  Scenario: Verify a user can log out
    Given the user logs out
    Then the response code for logout user is 200

