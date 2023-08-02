Feature: lambda request
  Background:
    * string req = '{"ccd": "123456"}'
    * def AWSConn = Java.type('aws.AwsConnect')
    * def region = aws.region
    * def lambName = aws.lambdaFunctionName
    * def urlbase = 'https://tk7v98e2t8.execute-api.us-east-1.amazonaws.com/v0/'

  @ReqLambda
  Scenario: lambda req
    Given def res = AWSConn.invokeLambda(lambName,region,req)
    When print res
    Then match res.statusLambda == 200
    And match res.log !contains 'KeyError'

  @ReqApi
  Scenario: Api request
    * configure ssl = true
    * configure headers = { 'Content-Type': 'application/json', 'charset':'UTF-8' }
    * print urlbase
    Given url urlbase
    And request {"cc": "123456"}
    When method POST
    * print response
    Then match response.Correo == 'eazac@choucairtesting.com'