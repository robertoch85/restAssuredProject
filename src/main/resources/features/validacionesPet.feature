Feature: Pet Test Suite

  @addPet
  Scenario Outline: Verify a pet can be added
    Given that the user is in the pet page
    And the details of the pet has been added
    Then the body response contains the key id


  @updatePet
  Scenario Outline: Verify a pet can be updated
    Given that the user is in the pet page
    And the details of the pet has been updated
    And the response code for the update is 200
    Then the body response contains the update "<name>"

    Examples:
      |   name       |
      |   Chucho     |

  @getPetID
  Scenario Outline: Get a pet by ID
    Given that the user is in the pet page
    And we have the ID of the pet
    And the response code for the pet is 200
    Then the body response contains the "<ID>" of the pet

    Examples:
      |   ID   |
      |  555666 |

  @getPetStatus
  Scenario: Get pet by status
  Given that the user is in the pet page
  And the status of the request is status
  Then the body response contains the following ids


  @deletePet
  Scenario: Verify a pet can be deleted
    Given that the user is in the pet page
    And the pet is deleted by ID
    And the body response is empty


