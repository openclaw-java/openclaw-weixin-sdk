# openclaw-weixin-web-frontend

Vue 3 + TypeScript + Tailwind + xterm.js frontend for scheme-1 web console mode.

## LangChat Pro

**LangChat Pro** 是基于Java生态构建的**企业级AIGC应用开发平台商业版**,为企业提供完整的AI大模型集成解决方案。基于Spring Boot 3和Vue 3构建,支持快速构建智能知识库、多模态AI应用和智能工作流,助力企业实现AI驱动的数字化转型。

**产品官网**: http://langchat.cn/

**开源版地址**: https://github.com/tycoding/langchat (基础功能体验)

**商业版咨询**: 添加微信 **LangchainChat** (备注:公司名称 + [具体咨询内容])

![Workflows 展示截图](http://cdn.langchat.cn/langchat/imgs/20251126151119887.jpg)

## Structure

- `src/components/TerminalViewport.vue`: xterm terminal component (input/output + resize + ws bridge)
- `src/components/SessionSidebar.vue`: session list and operations
- `src/components/EventPanel.vue`: event stream panel
- `src/App.vue`: regional layout and state orchestration

## Run

```bash
cd openclaw-weixin-web-frontend
npm install
npm run dev
```

## Preview

**Web**

![iShot_2026-04-21_15.40.30](http://cdn.langchat.cn/langchat/imgs/20260421154038671.png)

**CLI**

![iShot_2026-04-21_15.27.29](http://cdn.langchat.cn/langchat/imgs/20260421152817512.png)

![iShot_2026-04-21_15.26.47](http://cdn.langchat.cn/langchat/imgs/20260421152827745.png)

![iShot_2026-04-21_15.27.01](http://cdn.langchat.cn/langchat/imgs/20260421152834152.png)

## Build

```bash
npm run typecheck
npm run build
```

Default dev URL: `http://localhost:15173`

Proxy defaults:

- `/api` -> `http://localhost:18080`
- `/ws` -> `ws://localhost:18080`
