# TS 对齐评估（openclaw-weixin）

评估时间：2026-04-20

评估基线：
- 官方 TS 仓库：`openclaw-weixin-work/openclaw-weixin-ts`
- 文档：`README.zh_CN.md`
- 代码基线：`src/api/*`, `src/auth/*`, `src/monitor/*`, `src/messaging/*`, `src/media/*`, `src/cdn/*`, `src/storage/*`

## 1. 功能对齐总览

| 能力域 | TS 官方状态 | Java SDK 当前状态 | 结论 |
|---|---|---|---|
| HTTP API（5个核心接口） | 完整 | 完整 | 已对齐 |
| Header/base_info 协议细节 | 完整 | 完整（支持 routeTag） | 已对齐 |
| QR 登录基础接口 | 完整 | 完整 | 已对齐 |
| QR 登录全流程（会话、过期刷新、redirect） | 完整 | 完整（`QrLoginFlowService`） | 已对齐 |
| 长轮询 monitor（超时/重试/退避） | 完整 | 完整（基础版） | 基本对齐 |
| `errcode=-14` 会话冷却 | 完整 | 完整 | 已对齐 |
| context_token 持久化 | 完整 | 完整 | 已对齐 |
| sync cursor 持久化 | 完整 | 完整 | 已对齐 |
| 媒体上传（image/video/file + CDN AES-ECB） | 完整 | 完整 | 已对齐 |
| 媒体下载解密（inbound） | 完整 | 完整（已接入 monitor 事件回调） | 已对齐 |
| 语音 SILK 转 WAV | 完整（依赖 `silk-wasm`） | 未实现（JDK 标准库无原生 SILK 解码） | 未对齐 |
| 远程 URL 媒体发送 | 完整（先下载再上传） | 完整（支持 `http(s)/file://`） | 已对齐 |
| Markdown 流式过滤 | 完整 | 已实现（基础清洗版） | 基本对齐 |
| slash/debug 指令体系 | 完整（插件级） | 未实现（SDK 级暂不内置） | 未对齐 |
| pairing/allowFrom 鉴权存储 | 完整（插件级） | 未实现（SDK 级暂不内置） | 未对齐 |
| OpenClaw runtime 路由/会话写回 | 完整（插件耦合） | 未实现（Java 为独立 SDK） | 未对齐（设计差异） |
| 旧版本兼容迁移（legacy 文件回退） | 完整 | 已实现（account/sync/context 主路径回退） | 基本对齐 |
| 可执行 CLI 封装（脚本入口） | 依赖 openclaw 插件命令 | 已实现（repository 根目录 `bin/openclaw-weixin`） | 已对齐（repository 侧） |

## 2. 已补充能力（本轮新增）

- 新增 `QrLoginFlowService`
  - 支持会话缓存（sessionKey）
  - 支持轮询等待确认
  - 支持二维码过期自动刷新（最多 3 次）
  - 支持 `scaned_but_redirect` 的重定向 host 轮询
  - 成功后自动写入 `FileAccountStore`
- 新增 `CdnMediaDownloader`
  - 支持 `full_url` 直连下载
  - 支持 `encrypted_query_param` 回退拼 URL 下载
  - 支持 `aes_key` 两种编码解析（raw16 / hex32-ascii）
  - 支持 AES-128-ECB 解密落地文件
- 新增多账号上下文解析器 `AccountContextResolver`
  - 根据 recipient + context_token 映射自动推断 accountId
- 新增 `OpenClawRouteTagLoader`
  - 读取 `openclaw.json` 的 `channels.openclaw-weixin.routeTag`
  - 支持 account 级 routeTag 覆盖
- 新增 monitor 入站媒体事件链路
  - `WeixinLongPollMonitor` 支持可选 `InboundMessageEventHandler`
  - 自动识别首个媒体 item，下载/解密并落地本地文件
- 新增远程媒体发送能力
  - `MediaUploadService` 支持 `sendMedia(String mediaUrlOrPath, ...)`
  - 支持本地路径、`file://`、`http(s)` 远程 URL

## 3. 仍有差距（建议优先级）

### P1（建议次优先）

1. 旧版状态迁移兼容增强
- 现状：已支持 raw/normalized/legacy 的主要路径回退
- 剩余：尚未覆盖 TS 全量边界场景（如更复杂旧格式字段）

2. 下行文本 Markdown 过滤
- 现状：已实现基础清洗（标题/引用/链接/图片/行内标记）
- 剩余：未完整对齐 TS 的字符级状态机与边界行为

3. 结构化日志与敏感字段脱敏
- 现状：已实现 API debug 日志 + token/context_token 脱敏
- 剩余：未实现统一结构化日志门面与日志分级输出策略

### P2（可选）

1. SILK 转 WAV
- 现状：JDK 标准库无法直接解码 SILK
- 方案：
  - 方案A：保持“零三方依赖”，仅透传 `.silk`
  - 方案B：引入可选扩展模块（非核心包）支持转码

2. 插件级命令系统（`/toggle-debug` 等）
- 现状：SDK 不包含
- 结论：建议保持在 LangChat/OpenClaw 上层，而非底层 SDK

## 4. 结论

- 作为独立 Java SDK（JDK17+、零三方依赖）当前已覆盖官方 TS 的协议主链路与生产关键路径。
- 与 TS 的主要差距集中在“插件框架耦合功能”和“语音转码/调试命令”两类。
- 若你要用于 LangChat 生产接入，建议下一步优先处理 P1 的“兼容迁移 + markdown 过滤 + 脱敏日志”。
