# aws-java
Simple AWS wrappers with integration tests for "localstack"
* basic code that exercises AWS APIs with localstack (docker based AWS mocks) https://github.com/localstack/localstack
* simple java examples using s3, sqs, sns, etc.
* plain old java, no frameworks
* junit integration tests assume localstack is running


### running the integration tests
* $ ./gradlew test

If you get errors, check to see that localstack docker is up and that the ports match up.

### target
* s3
* sqs
* sns
* dynamo
* more...

#### todo
* more aws...
* ci/cd

### problems

* Problems with gradle builds or IDE import?
    * $ gradle wrapper
    
    
    
    
    