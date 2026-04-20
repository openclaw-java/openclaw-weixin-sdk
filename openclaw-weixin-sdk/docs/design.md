# OpenClaw Weixin Java SDK 设计文档

## 1. 项目定位

- 项目坐标：`cn.langchat.openclaw:openclaw-weixin-sdk`
- 最低运行版本：`JDK 17`
- 目标：对齐腾讯 `openclaw-weixin` TS 插件协议与核心流程，提供 LangChat 可控、可扩展的 Java SDK。
- 约束：运行时不依赖第三方库（HTTP/JSON/加密/存储全部使用 JDK 标准库实现）。

## 2. 对齐 TS 架构的模块映射

- `api/*` -> `api.WeixinApiClient`
  - 统一 Header 组装：`AuthorizationType`、`Authorization`、`X-WECHAT-UIN`、`iLink-App-*`、`SKRouteTag`
  - 统一 `base_info.channel_version`
- `auth/login-qr.ts` -> `auth.QrLoginClient`
- `monitor/monitor.ts` -> `monitor.WeixinLongPollMonitor`
- `api/session-guard.ts` -> `monitor.WeixinSessionGuard`
- `messaging/inbound.ts` 上下文 token -> `storage.FileContextTokenStore`
- `storage/sync-buf.ts` -> `storage.FileSyncCursorStore`
- `cdn/*` + `messaging/send-media.ts` -> `media.MediaUploadService` + `media.CdnUploader`

## 3. 包结构

- `cn.langchat.openclaw.weixin.api`：协议 API 客户端
- `cn.langchat.openclaw.weixin.auth`：扫码登录客户端
- `cn.langchat.openclaw.weixin.model`：协议模型
- `cn.langchat.openclaw.weixin.media`：CDN 上传、AES 能力
- `cn.langchat.openclaw.weixin.monitor`：长轮询与会话守卫
- `cn.langchat.openclaw.weixin.storage`：账户、游标、token 存储
- `cn.langchat.openclaw.weixin.util`：JSON/映射/哈希/ID 基础设施

## 4. 线程与状态模型

- `WeixinApiClient`：线程安全（`HttpClient` 无状态复用）。
- `FileContextTokenStore`：内存 `ConcurrentHashMap` + 文件落盘。
- `WeixinSessionGuard`：内存冷却窗口（`accountId -> pauseUntil`）。
- `WeixinLongPollMonitor`：单账户单循环；通过 `AtomicBoolean` 停止。

## 5. 协议关键点

- 长轮询：`getupdates` 超时视为正常空轮询。
- 会话过期：`errcode=-14` 触发账户 1 小时冷却。
- 下行消息：文本与媒体按 item 分开发送，兼容官方行为。
- 媒体上传：
  - 明文 MD5 + AES-128-ECB 填充后 size 上报
  - `getuploadurl` 获取上传参数
  - CDN POST 密文并读取 `x-encrypted-param`

## 6. 类注释规范

- 每个顶级类/record/interface/enum 前必须包含：
  - `@since 2026-04-20`
  - `@author LangChat Team`
- 示例：
  - `@since 2026-04-20`
  - `@author LangChat Team`

## 7. 首版范围（v0.1.0）

- 完成协议主链路：登录、收消息、发消息、媒体上传、媒体下载解密、状态存储。
- CLI/TUI 作为独立模块维护，不放在 SDK 模块内。
- 暂不包含：
- 语音 SILK 转码
- host runtime 适配器（由 LangChat 上层注入）
