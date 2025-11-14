@regression
Feature: Sample API

  @smoke
  Scenario: Description Expectation
    Given Base URL is "https://jsonplaceholder.typicode.com"
    Given I set query params:
      | postId | 1 |
    When I send "GET" request to "/comments"
    Then the response status code should be 200

  @smoke @regression
  Scenario: Validate API response with body csv
    Given Base URL is "https://jsonplaceholder.typicode.com"
    When the response status code is 200 when I send "POST" request to "/posts" with body csv "postCsv.csv"



