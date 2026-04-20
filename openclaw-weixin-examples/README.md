# openclaw-weixin-examples

Example service and usage tests for `openclaw-weixin-sdk`.

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
