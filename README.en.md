# OpenClaw Weixin Workspace

Workspace maintained by LangChat Team for Weixin OpenClaw Java integration (SDK + CLI + Examples).

中文版: [README.md](./README.md)

## 1. What This Repository Is

This workspace is centered on the **Weixin OpenClaw plugin over iLinkAI protocol**:

- Java protocol SDK (JDK 17+)
- Standalone terminal CLI/TUI client
- Example module for quick onboarding and tests

## 2. Repository Layout

```text
openclaw-weixin-workspace/
├─ README.md
├─ README.en.md
├─ bin/
│  ├─ openclaw-weixin
│  └─ publish-openclaw-weixin-sdk
├─ openclaw-weixin-sdk/
├─ openclaw-weixin-ratatui-cli/
└─ openclaw-weixin-examples/
```

## 3. Weixin OpenClaw Plugin and iLinkAI Protocol

### 3.1 Protocol Scope

The SDK implements the Weixin OpenClaw iLinkAI transport flows:

- QR login (`get_bot_qrcode`, `get_qrcode_status`)
- inbound polling (`getupdates`)
- outbound messaging (`sendmessage`)
- typing status (`sendtyping`)
- media upload pipeline (`getuploadurl` + CDN upload/download)
- runtime config (`getconfig`)

### 3.2 iLinkAI Endpoints

```text
https://ilinkai.weixin.qq.com/ilink/bot/get_bot_qrcode
https://ilinkai.weixin.qq.com/ilink/bot/get_qrcode_status
https://ilinkai.weixin.qq.com/ilink/bot/getupdates
https://ilinkai.weixin.qq.com/ilink/bot/sendmessage
https://ilinkai.weixin.qq.com/ilink/bot/sendtyping
https://ilinkai.weixin.qq.com/ilink/bot/getuploadurl
https://ilinkai.weixin.qq.com/ilink/bot/getconfig
```

### 3.3 SDK Runtime Architecture over iLinkAI (ASCII)

```text
+------------------------------------------------------------------------------------+
| Application Layer (Agent / CLI / business service)                                 |
| Calls: login / sendText / sendTyping / monitorStream                               |
+-----------------------------------------------+------------------------------------+
                                                |
                                                v
+------------------------------------------------------------------------------------+
| OpenClawWeixinSdk (facade)                                                          |
|------------------------------------------------------------------------------------|
| Auth: QrLoginFlowService                                                            |
| Transport: WeixinApiClient (JDK HttpClient)                                         |
| Monitor: WeixinLongPollMonitor + WeixinSessionGuard                                 |
| Media: MediaUploadService + CdnUploader/CdnMediaDownloader                          |
| State: FileAccountStore / FileContextTokenStore / FileSyncCursorStore               |
+-------------------------------+-------------------------------+--------------------+
                                |                               |
                                | HTTPS(JSON)                   | local persistence
                                v                               v
+--------------------------------------------------+    +--------------------------+
| Weixin OpenClaw Plugin (iLinkAI endpoints)       |    | ~/.openclaw/openclaw-   |
| /getupdates /sendmessage /sendtyping /getconfig  |    | weixin/*                |
| /get_bot_qrcode /get_qrcode_status /getuploadurl |    | - account               |
|                                                  |    | - context_token         |
|                                                  |    | - get_updates_buf       |
+--------------------------------------------------+    +--------------------------+
```

### 3.4 Long-Poll Loop Sequence (ASCII)

```text
App/CLI            OpenClawWeixinSdk       WeixinLongPollMonitor           iLinkAI
   |                       |                         |                        |
   | monitorStream.start() |                         |                        |
   |---------------------->| createMonitorWithMedia  |                        |
   |                       |------------------------>| load cursor/context     |
   |                       |                         | from File*Store         |
   |                       |                         |                        |
   |                       |                         |==== LOOP ============== |
   |                       |                         | POST /getupdates        |
   |                       |                         | (get_updates_buf,       |
   |                       |                         |  longpoll timeout) ---> |
   |                       |                         | <--- msgs +             |
   |                       |                         |      get_updates_buf +  |
   |                       |                         |      longpolling_timeout|
   |                       |                         | save get_updates_buf     |
   |                       |                         | save context_token       |
   | <---- onMessage/event-|<------------------------| emit callbacks          |
   |                       |                         |========================= |
```

### 3.5 Failure Recovery and Backoff Strategy

- If `getupdates` returns `errcode=-14` (session expired), `WeixinSessionGuard` pauses that `accountId` (default: 1 hour) and blocks further send/poll operations in the window.
- Other failures use `2s` retry; after 3 consecutive failures, the monitor backs off for `30s`.
- HTTP timeout on `getupdates` is treated as an empty poll cycle, not a fatal error.
- If server returns `longpolling_timeout_ms`, SDK updates the next poll timeout dynamically.
- For each inbound message, SDK persists `context_token` for subsequent `sendmessage/sendtyping` continuity.

## 4. SDK Usage

### 4.1 Maven Dependency

```xml
<dependency>
  <groupId>cn.langchat.openclaw</groupId>
  <artifactId>openclaw-weixin-sdk</artifactId>
  <version>0.1.0</version>
</dependency>
```

### 4.2 Java Quick Start

```java
import cn.langchat.openclaw.weixin.OpenClawWeixinSdk;
import cn.langchat.openclaw.weixin.api.WeixinClientConfig;

var sdk = new OpenClawWeixinSdk(
    WeixinClientConfig.builder()
        .baseUrl("https://ilinkai.weixin.qq.com")
        .build()
);

var qr = sdk.qrFlow().start(null, null, false);
System.out.println("QR URL: " + qr.qrcodeUrl());

var login = sdk.qrFlow().waitForConfirm(
    qr.sessionKey(),
    java.time.Duration.ofMinutes(8),
    null
);

if (login.connected()) {
    sdk.sendText(login.accountId(), "<userId@im.wechat>", "Hello from Java");
}
```

## 5. CLI Quick Run

```bash
./bin/openclaw-weixin chat
```

## 6. Build and Test

```bash
mvn -q -f openclaw-weixin-sdk/pom.xml -DskipTests compile
mvn -q -f openclaw-weixin-ratatui-cli/pom.xml -DskipTests package
mvn -q -f openclaw-weixin-examples/pom.xml test
```

## 7. Publish to Maven Central

```bash
./bin/publish-openclaw-weixin-sdk
```

## 8. Boundary

- SDK handles iLinkAI transport + protocol state continuity.
- SDK does not implement business-level message history/orchestration.
- `sendTextStream(...)` is chunked sending, not LLM token callback streaming.
