# openclaw-weixin-cli

Standalone terminal chat client built by LangChat Team, powered by `openclaw-weixin-sdk`.

- Module: `openclaw-weixin-cli`
- Runtime: `JDK 17+`
- UI stack: TamboUI + JLine backend

## LangChat Pro

**LangChat Pro** 是基于Java生态构建的**企业级AIGC应用开发平台商业版**,为企业提供完整的AI大模型集成解决方案。基于Spring Boot 3和Vue 3构建,支持快速构建智能知识库、多模态AI应用和智能工作流,助力企业实现AI驱动的数字化转型。

**产品官网**: http://langchat.cn/

**开源版地址**: https://github.com/tycoding/langchat (基础功能体验)

**商业版咨询**: 添加微信 **LangchainChat** (备注:公司名称 + [具体咨询内容])

![Workflows 展示截图](http://cdn.langchat.cn/langchat/imgs/20251126151119887.jpg)

## Positioning

This project is intentionally separated from the SDK:

- SDK provides protocol transport and state handling
- CLI provides interactive terminal UX
- the CLI module can serve as both production tool and SDK example application

## Architecture

```text
+-------------------------------+
| openclaw-weixin-cli   |
|-------------------------------|
| TUI state machine             |
| account picker / QR login     |
| slash commands / input box    |
| monitor events -> chat panel  |
+---------------+---------------+
                |
                v
+-------------------------------+
| openclaw-weixin-sdk           |
+-------------------------------+
                |
                v
+-------------------------------+
| Weixin OpenClaw HTTP APIs     |
+-------------------------------+
```

## Features

- Full-screen terminal app (alternate screen mode)
- Startup account selection in TUI
- In-TUI QR code rendering for login
- Split layout: main conversation area + side log area
- Fixed input box at bottom
- Slash command suggestion when typing `/`
- Typing status synchronization (`sendtyping`)
- Monitor loop for inbound messages and media notifications

## Preview

**Web**

![iShot_2026-04-21_15.40.30](http://cdn.langchat.cn/langchat/imgs/20260421154038671.png)

**CLI**

![iShot_2026-04-21_15.27.29](http://cdn.langchat.cn/langchat/imgs/20260421152817512.png)

![iShot_2026-04-21_15.26.47](http://cdn.langchat.cn/langchat/imgs/20260421152827745.png)

![iShot_2026-04-21_15.27.01](http://cdn.langchat.cn/langchat/imgs/20260421152834152.png)

## Build

```bash
mvn -DskipTests -pl openclaw-weixin-cli -am package
```

Generated artifacts:

- `target/openclaw-weixin-cli-<version>.jar`
- `target/openclaw-weixin-cli-<version>-all.jar`

## Run

From repository root:

```bash
./bin/openclaw-weixin chat
./bin/openclaw-weixin login
./bin/openclaw-weixin rebuild
./bin/openclaw-weixin help
```

Or directly by jar:

```bash
java -jar target/openclaw-weixin-cli-0.1.0-all.jar chat
```

## Chat Commands

- `/help`
- `/users`
- `/to <userId@im.wechat>`
- `/media <path|url> [caption]`
- `/login`
- `/logout`
- `/clear`
- `/quit`

## Keybindings

- `Enter`: send message / confirm current action
- `Ctrl+Enter`: append new line in chat draft
- `Esc`: clear current input
- `Ctrl+L`: clear chat + log panes
- `Ctrl+C`: quit

## Troubleshooting

- If no peer is selected, run `/to <userId@im.wechat>` first.
- If login state is stale, run `/logout` then `/login`.
- If you need clean state, set `OPENCLAW_STATE_DIR` to an isolated directory before startup.
