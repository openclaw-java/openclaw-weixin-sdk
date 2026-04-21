# openclaw-weixin-examples

Example service and usage tests for `openclaw-weixin-sdk`.

## LangChat Pro

**LangChat Pro** 是基于Java生态构建的**企业级AIGC应用开发平台商业版**,为企业提供完整的AI大模型集成解决方案。基于Spring Boot 3和Vue 3构建,支持快速构建智能知识库、多模态AI应用和智能工作流,助力企业实现AI驱动的数字化转型。

**产品官网**: http://langchat.cn/

**开源版地址**: https://github.com/tycoding/langchat (基础功能体验)

**商业版咨询**: 添加微信 **LangchainChat** (备注:公司名称 + [具体咨询内容])

![Workflows 展示截图](http://cdn.langchat.cn/langchat/imgs/20251126151119887.jpg)

## What is included

- `OpenClawWeixinExampleService`: small service wrapper for login/send/monitor flows
- `OpenClawWeixinQuickStartExample`: executable example entry
- `OpenClawWeixinExampleServiceUsageTest`: offline tests for usage patterns
- `OpenClawWeixinManualIntegrationTest`: disabled manual integration example

## Run tests

```bash
mvn -pl openclaw-weixin-examples test
```

## Run quick example

```bash
mvn -pl openclaw-weixin-examples -DskipTests exec:java -Dexec.args="login"
```

Optional env vars:

- `OPENCLAW_EXAMPLE_BASE_URL`
- `OPENCLAW_EXAMPLE_CDN_BASE_URL`
- `OPENCLAW_EXAMPLE_TO`
