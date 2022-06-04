Feature: Store Test Suite

  @getinventory
  Scenario: Get the inventory of the store
  Given we get the inventory of the store
  Then the store response code is 200

  @placeorder
  Scenario Outline: Place an order
  Given we have the details of the order
  And the store response code for the order placed is 200
  Then the body response contains the "<id>" of the order

    Examples:
      |   id    |
      |  708585 |

  @getorder
  Scenario Outline: Get order by ID
  Given we have the ID of the order
  And the store response code for the order we need is 200
  Then the body response contains the "<id>" of the order

    Examples:
      |   id    |
      |  708585 |

  @deleteorder
  Scenario: Verify a order can be deleted
  Given we have the ID of the order to be deleted
  Then the response code for the order to be deleted is 200