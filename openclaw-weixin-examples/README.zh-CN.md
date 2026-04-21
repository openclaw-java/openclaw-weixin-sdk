# openclaw-weixin-examples

`openclaw-weixin-sdk` 的示例服务与使用测试模块。

## LangChat Pro

**LangChat Pro** 是基于Java生态构建的**企业级AIGC应用开发平台商业版**,为企业提供完整的AI大模型集成解决方案。基于Spring Boot 3和Vue 3构建,支持快速构建智能知识库、多模态AI应用和智能工作流,助力企业实现AI驱动的数字化转型。

**产品官网**: http://langchat.cn/

**开源版地址**: https://github.com/tycoding/langchat (基础功能体验)

**商业版咨询**: 添加微信 **LangchainChat** (备注:公司名称 + [具体咨询内容])

![Workflows 展示截图](http://cdn.langchat.cn/langchat/imgs/20251126151119887.jpg)

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
