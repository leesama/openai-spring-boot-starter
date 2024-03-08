# OpenAI Starter 中文说明

## 介绍

这是一个用于集成 OpenAI API 的 Spring Boot Starter。它提供了一种简单的方式来配置和使用 OpenAI API。

## 如何使用

### 1. 添加依赖

在你的 Maven 项目中添加以下依赖：

```xml
<dependency>
    <groupId>dev.leesama</groupId>
    <artifactId>openai-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置

在你的 `application.properties` 或 `application.yml` 文件中添加以下配置：

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

其中，`apiKey` 是你的 OpenAI API 密钥，`models` 是你打算使用的模型列表。

### 3. 使用

在你的代码中，你可以通过注入 `OpenAIService` 来使用 OpenAI API。例如：

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

## 注意事项

- `OpenAIService` 中的 `callOpenAI` 方法会返回一个 `Mono<String>`，这是一个响应式类型，你可以使用 `subscribe` 或 `block` 方法来获取结果。
- 如果你尝试使用一个未在配置中定义的模型名称，`callOpenAI` 方法会抛出 `IllegalArgumentException`。

## 贡献

如果你有任何问题或建议，欢迎提交 issue 或 pull request。
