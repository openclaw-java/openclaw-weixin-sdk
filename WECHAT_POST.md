# 用 Java 打通微信 OpenClaw：LangChat Team 开源 `openclaw-weixin-sdk`（JDK 17+）

如果你正在做企业 AI 助手、智能客服、Agent 编排，且希望以 Java 体系稳定接入微信 OpenClaw（iLinkAI 协议），这套 SDK 可以直接落地。

GitHub 仓库：
https://github.com/openclaw-java/openclaw-weixin-sdk

---

## 这是什么

`openclaw-weixin-sdk` 是 LangChat Team 维护的微信 OpenClaw Java 方案，包含：

- `openclaw-weixin-sdk`：核心协议 SDK（发布到 Maven Central）
- `openclaw-weixin-cli`：可直接运行的命令行/TUI 工具
- `openclaw-weixin-examples`：示例与测试
- `openclaw-weixin-web-backend` + `openclaw-weixin-web-frontend`：Web Terminal 方案

目标很明确：让 Java 团队以尽量少依赖、可控架构，稳定接入微信 OpenClaw 协议能力。

---

## 协议能力（iLinkAI）

SDK 对齐了微信 OpenClaw 常见链路：

- 二维码登录：`get_bot_qrcode` + `get_qrcode_status`
- 长轮询收消息：`getupdates`
- 发送消息：`sendmessage`
- 输入中状态：`sendtyping`
- 媒体上传下载：`getuploadurl` + CDN
- 协议配置：`getconfig`

并提供本地状态持久化（账号、上下文 token、轮询游标）与失败恢复策略，方便生产环境长期运行。

---

## iLink 协议补充说明

SDK 与微信 OpenClaw 的通信本质是标准 HTTPS JSON 交互，核心约定包括：

- `account_id / token`：账号身份与鉴权
- `context_token`：同一会话上下文串联关键字段
- `get_updates_buf`：长轮询游标，保证消息连续拉取
- `longpolling_timeout_ms`：服务端建议的下一次轮询等待窗口

典型接口职责：

- `get_bot_qrcode`：申请登录二维码
- `get_qrcode_status`：轮询二维码状态，直到确认登录
- `getupdates`：长轮询拉取新消息与游标
- `sendmessage`：发送文本/媒体消息（可带 `context_token`）
- `sendtyping`：上报“正在输入中”状态
- `getuploadurl`：获取媒体上传地址
- `getconfig`：读取协议侧配置能力（如 typing ticket）

---

## 交互通信时序图（登录 + 收发消息）

```text
应用/CLI/Web           OpenClawWeixinSdk                iLinkAI(OpenClaw)
    |                         |                               |
    | 1) 请求登录二维码        |                               |
    |------------------------>| get_bot_qrcode                |
    |                         |------------------------------>|
    |                         |<------------------------------|
    |<------------------------| qrcode_url / session_key      |
    |                         |                               |
    | 2) 轮询扫码状态          | get_qrcode_status             |
    |------------------------>|------------------------------>|
    |                         |<------------------------------|
    |<------------------------| connected + account_id/token  |
    |                         |                               |
    | 3) 启动消息监听          | getupdates(long poll)         |
    |------------------------>|------------------------------>|
    |                         |<------------------------------|
    |<------------------------| messages + get_updates_buf    |
    |                         |(本地保存 context_token/buf)    |
    |                         |                               |
    | 4) 发送消息              | sendmessage(context_token)    |
    |------------------------>|------------------------------>|
    |                         |<------------------------------|
    |<------------------------| message_id                    |
    |                         |                               |
    | 5) 输入中状态            | sendtyping                    |
    |------------------------>|------------------------------>|
    |                         |<------------------------------|
    |<------------------------| ack                           |
```

---

## SDK 架构图（ASCII）

```text
+--------------------------------------------------------------------------------------+
| 业务层 / Agent / CLI / Web                                                          |
| 调用: qrFlow / sendText / sendMedia / sendTyping / monitorStream                    |
+-------------------------------------------+------------------------------------------+
                                            |
                                            v
+--------------------------------------------------------------------------------------+
| OpenClawWeixinSdk (Facade)                                                           |
|--------------------------------------------------------------------------------------|
| Auth            : QrLoginFlowService / QrLoginClient                                |
| API Transport   : WeixinApiClient (JDK HttpClient)                                  |
| Long Poll       : WeixinLongPollMonitor / WeixinMonitorStream / WeixinSessionGuard  |
| Media           : MediaUploadService / CdnMediaDownloader                            |
| Local State     : FileAccountStore / FileContextTokenStore / FileSyncCursorStore     |
+------------------------------+----------------------------------+---------------------+
                               |                                  |
                               | HTTPS JSON                       | File Persist
                               v                                  v
+----------------------------------------------------+   +------------------------------+
| WeChat OpenClaw (iLinkAI Endpoints)               |   | ~/.openclaw/openclaw-weixin  |
| - get_bot_qrcode / get_qrcode_status              |   | - accounts                    |
| - getupdates / sendmessage / sendtyping           |   | - context tokens              |
| - getuploadurl / getconfig                        |   | - sync cursor                 |
+----------------------------------------------------+   +------------------------------+
```

---

## 快速接入（Maven）

```xml
<dependency>
  <groupId>cn.langchat.openclaw</groupId>
  <artifactId>openclaw-weixin-sdk</artifactId>
  <version>0.1.1</version>
</dependency>
```

---

## 为什么是 Java 版

- 只兼容并优化 JDK 17+
- 核心通信基于 JDK `HttpClient`
- 低依赖、结构清晰，方便二次封装进企业系统
- 既能 SDK 嵌入业务，也能直接使用 CLI/Web Terminal 快速调试

---

## LangChat Pro（重点）

**LangChat Pro** 是基于 Java 生态构建的企业级 AIGC 应用开发平台商业版，面向企业提供完整的大模型集成解决方案。  
基于 Spring Boot 3 + Vue 3，支持快速构建智能知识库、多模态 AI 应用与智能工作流，助力企业 AI 落地与数字化升级。

- 产品官网：http://langchat.cn/
- 开源版地址：https://github.com/tycoding/langchat
- 商业版咨询：微信 `LangchainChat`（备注：公司名称 + 具体需求）

---

## 适用场景

- 企业微信侧 Agent/客服接入
- AI 工作流系统中的消息收发节点
- Java 后端统一封装微信 OpenClaw 通道
- 在 LangChat Pro 中作为微信连接层集成

---

## Preview（文末图片）

**Web Terminal**

![Web Preview](http://cdn.langchat.cn/langchat/imgs/20260421154038671.png)

**CLI / TUI**

![CLI Preview 1](http://cdn.langchat.cn/langchat/imgs/20260421152817512.png)

![CLI Preview 2](http://cdn.langchat.cn/langchat/imgs/20260421152827745.png)

![CLI Preview 3](http://cdn.langchat.cn/langchat/imgs/20260421152834152.png)

**LangChat Pro Workflow**

![LangChat Pro Workflow](http://cdn.langchat.cn/langchat/imgs/20251126151119887.jpg)
