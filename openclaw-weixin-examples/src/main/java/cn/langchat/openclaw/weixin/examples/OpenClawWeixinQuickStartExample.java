package cn.langchat.openclaw.weixin.examples;

import cn.langchat.openclaw.weixin.api.WeixinClientConfig;

import java.time.Duration;

/**
 * @since 2026-04-20
 * @author LangChat Team
 */
public final class OpenClawWeixinQuickStartExample {
    private OpenClawWeixinQuickStartExample() {
    }

    public static void main(String[] args) {
        String command = (args.length == 0) ? "help" : args[0];
        switch (command) {
            case "login" -> runLoginDemo();
            case "help", "--help", "-h" -> printHelp();
            default -> throw new IllegalArgumentException("Unknown command: " + command);
        }
    }

    private static void runLoginDemo() {
        String baseUrl = envOrDefault("OPENCLAW_EXAMPLE_BASE_URL", WeixinClientConfig.DEFAULT_BASE_URL);
        String cdnBaseUrl = envOrDefault("OPENCLAW_EXAMPLE_CDN_BASE_URL", WeixinClientConfig.DEFAULT_CDN_BASE_URL);

        OpenClawWeixinExampleService service = OpenClawWeixinExampleService.create(baseUrl, cdnBaseUrl);

        var session = service.startQrLogin(null);
        System.out.println("[example] QR URL: " + session.qrcodeUrl());
        System.out.println("[example] Waiting for scan/confirm ...");

        var result = service.waitForConfirm(session.sessionKey(), Duration.ofMinutes(8));
        if (!result.connected()) {
            System.out.println("[example] Login not completed: " + result.message());
            return;
        }

        String accountId = result.accountId();
        System.out.println("[example] Login success, accountId=" + accountId);

        String to = System.getenv("OPENCLAW_EXAMPLE_TO");
        if (to == null || to.isBlank()) {
            System.out.println("[example] Set OPENCLAW_EXAMPLE_TO to send a test message.");
            return;
        }

        String mid = service.sendText(accountId, to, "Hello from openclaw-weixin-examples");
        System.out.println("[example] Message sent, id=" + mid);

        service.newMonitorStream(accountId)
            .onStart(() -> System.out.println("[example] monitor started"))
            .onMessage(msg -> System.out.println("[inbound] " + msg.textBody()))
            .onError(err -> System.err.println("[example] monitor error: " + err.getMessage()))
            .onStop(() -> System.out.println("[example] monitor stopped"))
            .startAsync();

        System.out.println("[example] Press Ctrl+C to exit.");
        for (; ; ) {
            sleep(Duration.ofSeconds(1));
        }
    }

    private static void printHelp() {
        System.out.println("openclaw-weixin examples");
        System.out.println("Usage:");
        System.out.println("  mvn -pl openclaw-weixin-examples -DskipTests exec:java -Dexec.args=\"login\"");
        System.out.println("Env:");
        System.out.println("  OPENCLAW_EXAMPLE_BASE_URL      default https://ilinkai.weixin.qq.com");
        System.out.println("  OPENCLAW_EXAMPLE_CDN_BASE_URL  default https://api.ilink-ai.com");
        System.out.println("  OPENCLAW_EXAMPLE_TO            optional, target peer to send one message");
    }

    private static String envOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }

    private static void sleep(Duration duration) {
        try {
            Thread.sleep(Math.max(0L, duration.toMillis()));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted", ex);
        }
    }
}
