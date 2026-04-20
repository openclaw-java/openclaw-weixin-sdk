package cn.langchat.openclaw.weixin.cli;

import cn.langchat.openclaw.weixin.api.WeixinClientConfig;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @since 2026-04-20
 * @author LangChat Team
 */
public final class OpenClawWeixinCli {
    private OpenClawWeixinCli() {
    }

    public static void main(String[] args) {
        try {
            new OpenClawWeixinCli().run(args);
        } catch (Exception ex) {
            System.err.println("ERROR: " + ex.getMessage());
            System.exit(1);
        }
    }

    private void run(String[] args) throws Exception {
        String command = (args.length == 0) ? "chat" : args[0];
        if ("--help".equals(command) || "-h".equals(command) || "help".equals(command)) {
            printHelp();
            return;
        }

        Map<String, String> opts = parseArgs(args, args.length == 0 ? 0 : 1);

        switch (command) {
            case "chat" -> runChat(opts);
            case "login" -> runLogin(opts);
            default -> throw new IllegalArgumentException("unknown command: " + command);
        }
    }

    private void runChat(Map<String, String> opts) throws Exception {
        String baseUrl = opts.getOrDefault("--base-url", WeixinClientConfig.DEFAULT_BASE_URL);
        String cdnBaseUrl = opts.getOrDefault("--cdn-base-url", WeixinClientConfig.DEFAULT_CDN_BASE_URL);

        LaunchContext context = new LaunchContext(
            baseUrl,
            cdnBaseUrl,
            opts.get("--account-id"),
            opts.get("--to"),
            opts.containsKey("--new")
        );

        new WeixinCliApp(context).run();
    }

    private void runLogin(Map<String, String> opts) throws Exception {
        Map<String, String> merged = new LinkedHashMap<>(opts);
        merged.put("--new", "true");
        runChat(merged);
    }

    private static Map<String, String> parseArgs(String[] args, int start) {
        Map<String, String> opts = new LinkedHashMap<>();
        for (int i = start; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("--")) {
                if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                    opts.put(arg, args[++i]);
                } else {
                    opts.put(arg, "true");
                }
            }
        }
        return opts;
    }

    private static void printHelp() {
        System.out.println("openclaw-weixin TUI CLI");
        System.out.println("Usage:");
        System.out.println("  openclaw-weixin chat [--account-id <id>] [--to <userId@im.wechat>] [--new] [--base-url <url>] [--cdn-base-url <url>]");
        System.out.println("  openclaw-weixin login [--account-id <id>] [--base-url <url>] [--cdn-base-url <url>]");
        System.out.println("  openclaw-weixin help");
    }

    /**
     * @since 2026-04-20
     * @author LangChat Team
     */
    public record LaunchContext(
        String baseUrl,
        String cdnBaseUrl,
        String preferredAccountId,
        String initialPeer,
        boolean forceNewLogin
    ) {
    }
}
