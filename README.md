# Contract Testing Learning Hour 1

## Consumer Driven Contract Testing

This workshop is aimed at deliberately practicing producing a consumer driven contract using Pact.

Whilst contract testing can be applied retrospectively to already existing systems 
(you may want to consider bi-directional contract testing for those use cases), we will follow the 
[consumer driven contracts](https://martinfowler.com/articles/consumerDrivenContracts.html) approach in this kata - where a new consumer "goes first" producing 
a contract that the provider will use to TDD the endpoints in their service in parallel.  
Both parties can then be sure that they have built exactly what each other was expecting 
making integration or e2e testing between the real services a formality.

This workshop should take from 1 to 2 hours, depending on how deep you want to go into each topic.

**Workshop outline**:

- [step 1: **Define the expectation of the provider**](https://github.com/jjwils/cdc-lh1-kata/tree/samirs-taxi-step1#step-1---define-the-expectation-of-the-provider): Define the provider API in a test in our Consumer codebase.
- [step 2: **Write a test against the mock provider**](https://github.com/jjwils/cdc-lh1-kata/tree/samirs-taxi-step2#step-2---write-a-test-against-the-mock-providers): Write a unit test for our consumer
- [step 3: **Run the test and produce a contract**](https://github.com/jjwils/cdc-lh1-kata/tree/samirs-taxi-step-3#step-3---run-the-test-and-produce-a-contract): Write a Pact test for our consumer


_NOTE: Each step is tied to, and must be run within, a git branch, allowing you to progress through each stage incrementally. For example, to move to step 2 run the following: `git checkout step2`_

## Learning objectives

If running this as a team workshop format, you may want to take a look through the [learning objectives](./LEARNING.md).

## Requirements

- JDK 17+
- Maven 3+

## Scenario

There are two components in scope for our workshop.

1. Product Catalog website. It provides an interface to query the Product service for product information.
1. Product Service (Provider). Provides useful things about products, such as listing all products and getting the details of an individual product.

## Step 1 - Define the expectation of the provider

*Provider states* is an important concept of Pact that we need to introduce. These states define the state that the provider should be in for specific interactions. For the moment, we will initially be testing the following state that 
is defined in the ```.given("a job exists")``` on line 4 below.
You can think of this as the *Given* step in *Given, When, Then* and is where *us the consumer* are designing the API of the provider *before it even exists*.

Your task is to fill out the TODO's below to create the API we want from the provider to meet Samir's expected response.

*Hint* - have a look at the traditional integration test in the JobServiceClientTest class.


```java
@Pact(consumer = "SamirsApp")
public RequestResponsePact getJob(PactDslWithProvider builder) {
return builder
.given("a job exists")
.uponReceiving("get job")
.path("TODO")
.willRespondWith()
.status(-1)//CHANGE THIS TO THE INT FOR HTTP.OK
.body(
new PactDslJsonBody()
.object("TODO", new PactDslJsonBody()
.stringType("TODO", "TODO")
.stringType("TODO", "TODO")
.stringType("TODO", "TODO")
)
.stringType("TODO", "TODO")
.stringType("TODO", "TODO")
.stringType("TODO", "TODO")
.stringType("TODO", "TODO")
.booleanType("TODO", false)
)
.toPact();
}
```

*Move on to [step 2](https://github.com/pact-foundation/pact-workshop-Maven-Springboot-JUnit5/tree/step2#step-2---client-tested-but-integration-fails)*

## Step 2 - Write a test against the mock provider

Now let's create a basic test for our mock provider. We're going to check 2 things:

1. That our client code hits the expected endpoint
1. That the response is marshalled into an object that is usable.

Notice how this is identical to the traditional integration test in the JobServiceClientTest class.


```java
 @Test
@PactTestFor(pactMethod = "getJob", pactVersion = PactSpecVersion.V3)
void should_get_job(MockServer mockServer) {
    jobServiceClient.setBaseUrl(mockServer.getUrl());
    Job job = jobServiceClient.getJob();
    assertThat(job, is(equalTo(new Job(
            new Customer("TODO", "TODO", "TODO"),
            "TODO",
            "TODO",
            "TODO",
            "TODO",
            false
    ))));
}
```


*Move on to [step 3](https://github.com/pact-foundation/pact-workshop-Maven-Springboot-JUnit5/tree/step3#step-3---pact-to-the-rescue)*

## Step 3 - Run the test and produce a contract

Running this test starts a mock server on a random port that acts as our provider service. To get this to work we 
updated the URL in the `JobServiceClient` to point to the mock server that Pact provides for the test.

Once the test goes green and passes, a pact file should be generated in ```target/pacts/SamirsApp-JobService.json```.  
Open the file in the editor and have a look at how the contract is formed. It is this file that we will send to the 
broker in a later kata and which the provider will use to TDD the endpoints in their service.

--




Unit tests are written and executed in isolation of any other services. When we write tests for code that talk to
other services, they are built on trust that the contracts are upheld. There is no way to validate that the 
consumer and provider can communicate correctly.

> An integration contract test is a test at the boundary of an external service verifying that it meets the 
> contract expected by a consuming service — [Martin Fowler](https://martinfowler.com/bliki/IntegrationContractTest.html)

Adding contract tests via Pact would have highlighted the `/product/{id}` endpoint was incorrect.

Let us add Pact to the project and write a consumer pact test for the `GET /products/{id}` endpoint.

*Provider states* is an important concept of Pact that we need to introduce. These states help define the state that the provider should be in for specific interactions. For the moment, we will initially be testing the following states:

- `product with ID 10 exists`
- `products exist`

The consumer can define the state of an interaction using the `given` property.

Pact test `consumer/src/test/java/io/pact/workshop/product_catalogue/clients/ProductServiceClientPactTest.java`:

```java
@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "ProductService")
class ProductServiceClientPactTest {
  @Autowired
  private ProductServiceClient productServiceClient;

  @Pact(consumer = "ProductCatalogue")
  public RequestResponsePact allProducts(PactDslWithProvider builder) {
    return builder
      .given("products exists")
        .uponReceiving("get all products")
        .path("/products")
      .willRespondWith()
        .status(200)
        .body(
          new PactDslJsonBody()
            .minArrayLike("products", 1, 2)
              .integerType("id", 9L)
              .stringType("name", "Gem Visa")
              .stringType("type", "CREDIT_CARD")
              .closeObject()
            .closeArray()
        )
      .toPact();
  }

  @Test
  @PactTestFor(pactMethod = "allProducts", pactVersion = PactSpecVersion.V3)
  void testAllProducts(MockServer mockServer) {
    productServiceClient.setBaseUrl(mockServer.getUrl());
    List<Product> products = productServiceClient.fetchProducts().getProducts();
    assertThat(products, hasSize(2));
    assertThat(products.get(0), is(equalTo(new Product(9L, "Gem Visa", "CREDIT_CARD", null, null))));
  }

  @Pact(consumer = "ProductCatalogue")
  public RequestResponsePact singleProduct(PactDslWithProvider builder) {
    return builder
      .given("product with ID 10 exists", "id", 10)
      .uponReceiving("get product with ID 10")
        .path("/products/10")
      .willRespondWith()
        .status(200)
        .body(
          new PactDslJsonBody()
            .integerType("id", 10L)
            .stringType("name", "28 Degrees")
            .stringType("type", "CREDIT_CARD")
            .stringType("code", "CC_001")
            .stringType("version", "v1")
        )
      .toPact();
  }

  @Test
  @PactTestFor(pactMethod = "singleProduct", pactVersion = PactSpecVersion.V3)
  void testSingleProduct(MockServer mockServer) {
    productServiceClient.setBaseUrl(mockServer.getUrl());
    Product product = productServiceClient.getProductById(10L);
    assertThat(product, is(equalTo(new Product(10L, "28 Degrees", "CREDIT_CARD", "v1", "CC_001"))));
  }
}

```


![Test using Pact](diagrams/workshop_step3_pact.svg)

These tests starts a mock server on a random port that acts as our provider service. To get this to work we update the 
URL in the `ProductServiceClient` to point to the mock server that Pact provides for the test.

Running this test also passes, but it creates a pact file which we can use to validate our assumptions on the 
provider side, and have conversation around.

```console
consumer ❯ ./mvnw verify
[INFO] Scanning for projects...
[INFO] 
[INFO] -----------------< io.pact.workshop:product-catalogue >-----------------
[INFO] Building product-catalogue 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-resources-plugin:3.3.0:resources (default-resources) @ product-catalogue ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Using 'UTF-8' encoding to copy filtered properties files.
[INFO] Copying 1 resource
[INFO] Copying 6 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.8.1:compile (default-compile) @ product-catalogue ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- maven-resources-plugin:3.3.0:testResources (default-testResources) @ product-catalogue ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Using 'UTF-8' encoding to copy filtered properties files.
[INFO] skip non existing resourceDirectory /home/ronald/Development/Projects/Pact/pact-workshop-Maven-Springboot-JUnit5/consumer/src/test/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.8.1:testCompile (default-testCompile) @ product-catalogue ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 2 source files to /home/ronald/Development/Projects/Pact/pact-workshop-Maven-Springboot-JUnit5/consumer/target/test-classes
[INFO] 
[INFO] --- maven-surefire-plugin:2.22.2:test (default-test) @ product-catalogue ---
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------

<<< Omitted >>>

[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] 
[INFO] --- maven-jar-plugin:3.3.0:jar (default-jar) @ product-catalogue ---
[INFO] Building jar: /home/ronald/Development/Projects/Pact/pact-workshop-Maven-Springboot-JUnit5/consumer/target/product-catalogue-0.0.1-SNAPSHOT.jar
[INFO] 
[INFO] --- spring-boot-maven-plugin:2.4.3:repackage (repackage) @ product-catalogue ---
[INFO] Replacing main artifact with repackaged archive
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  6.013 s
[INFO] Finished at: 2021-02-26T09:43:03+11:00
[INFO] ------------------------------------------------------------------------
```

A pact file should have been generated in *consumer/target/pacts/ProductCatalogue-ProductService.json*

*NOTE*: even if the API client had been graciously provided for us by our Provider Team, it doesn't mean that we 
shouldn't write contract tests - because the version of the client we have may not always be in sync with the 
deployed API - and also because we will write tests on the output appropriate to our specific needs.

*Move on to [step 4](https://github.com/pact-foundation/pact-workshop-Maven-Springboot-JUnit5/tree/step4#step-4---verify-the-provider)*
