# openclaw-weixin-web-frontend

方案1前端：Vue3 + TypeScript + Tailwind + xterm.js。

## LangChat Pro

**LangChat Pro** 是基于Java生态构建的**企业级AIGC应用开发平台商业版**,为企业提供完整的AI大模型集成解决方案。基于Spring Boot 3和Vue 3构建,支持快速构建智能知识库、多模态AI应用和智能工作流,助力企业实现AI驱动的数字化转型。

**产品官网**: http://langchat.cn/

**开源版地址**: https://github.com/tycoding/langchat (基础功能体验)

**商业版咨询**: 添加微信 **LangchainChat** (备注:公司名称 + [具体咨询内容])

![Workflows 展示截图](http://cdn.langchat.cn/langchat/imgs/20251126151119887.jpg)

## 组件拆分

- `src/components/TerminalViewport.vue`：Terminal 组件封装（输入输出、窗口尺寸同步、WebSocket）
- `src/components/SessionSidebar.vue`：会话列表与控制区
- `src/components/EventPanel.vue`：事件日志区
- `src/App.vue`：分区域布局与状态编排

## 启动

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

## 构建检查

```bash
npm run typecheck
npm run build
```

默认访问：`http://localhost:15173`

开发代理：

- `/api` -> `http://localhost:18080`
- `/ws` -> `ws://localhost:18080`
