# openclaw-weixin-examples

`openclaw-weixin-sdk` 的示例服务与使用测试模块。

## 包含内容

- `OpenClawWeixinExampleService`：封装登录/发送/监听主链路的示例服务
- `OpenClawWeixinQuickStartExample`：可执行示例入口
- `OpenClawWeixinExampleServiceUsageTest`：离线可跑的使用模式测试
- `OpenClawWeixinManualIntegrationTest`：默认禁用的手动集成示例

## 运行测试

```bash
mvn -pl openclaw-weixin-examples test
```

## 运行示例

```bash
mvn -pl openclaw-weixin-examples -DskipTests exec:java -Dexec.args="login"
```

可选环境变量：

- `OPENCLAW_EXAMPLE_BASE_URL`
- `OPENCLAW_EXAMPLE_CDN_BASE_URL`
- `OPENCLAW_EXAMPLE_TO`
