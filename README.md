# OpenAI Starter English Documentation

[English](README.md) | [中文](README_CN.md)

## Introduction

This is a Spring Boot Starter for integrating the OpenAI API. It offers a straightforward way to configure and use the OpenAI API.

## How to Use

### 1. Add Dependency

Add the following dependency to your Maven project:

```xml
<dependency>
    <groupId>dev.leesama</groupId>
    <artifactId>openai-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

### 2. Configuration

Add the following configuration to your `application.properties` or `application.yml` file:

```yaml
openai:
  enabled: true
  apiKey: your-openai-api-key
  baseUrl: https://api.openai.com
  models:
    - name: model-name
      maxTokens: 100
      temperature: 0.7
```

Here, `apiKey` is your OpenAI API key, and `models` is the list of models you plan to use.

### 3. Usage

In your code, you can use the OpenAI API by injecting the `OpenAIService`. For example:

```java
@Autowired
private OpenAIService openAIService;

public void someMethod() {
    String modelName = "model-name";
    String prompt = "your-prompt";
    Mono<String> result = openAIService.callOpenAI(modelName, prompt);
    // do something with result
}
```

## Notes

- The `callOpenAI` method in `OpenAIService` returns a `Mono<String>`, which is a reactive type. You can use the `subscribe` or `block` methods to obtain the result.
- If you try to use a model name that is not defined in the configuration, the `callOpenAI` method will throw an `IllegalArgumentException`.

## Contribution

If you have any issues or suggestions, feel free to submit an issue or pull request.
