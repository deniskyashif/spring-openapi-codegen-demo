## Generate OpenApi spec using code annotations

[The OpenAPI Specification Explained](https://oai.github.io/Documentation/specification.html)

1. Add `'org.springdoc:springdoc-openapi-ui:1.6.9'` to generate OpenAPI UI in the browser
2. Add `'io.swagger.core.v3:swagger-core:2.2.2'` to include code annotations for documenting API endpoints
3. Document the API endpoints using code annotations (see `GreetingController.java`), 
4. Run the web app `gradle bootRun` and navigate to `localhost:8080/swagger-ui/index.html` to see the interactive documentation.
5. Access the OpenAPI specification JSON document on `http://localhost:8080/v3/api-docs`

To generate the OpenAPI spec as a file, add the [springdoc-openapi-gradle-plugin](https://github.com/springdoc/springdoc-openapi-gradle-plugin) and run:  

`gradle clean generateOpenApiDocs` 

The file is located in `<path-to-project>/build/openapi.json`. Make sure no other process is running on port `:8080`. 

## Generate an API client from an OpenAPI spec

The OpenAPI Generator requires [node.js](https://nodejs.org/en/). See the instructions for [installation](https://openapi-generator.tech/docs/installation) and [usage](https://openapi-generator.tech/docs/usage).

List the available generators:  
`openapi-generator-cli-list`

Validate the OpenApi spec file:  
`openapi-generator-cli validate -i <path-to>/openapi.json`

Generate a C# API Client:  
`openapi-generator-cli generate -i <path-to>/openapi.json -g csharp-netcore -o ./tmp/ClientApp/`