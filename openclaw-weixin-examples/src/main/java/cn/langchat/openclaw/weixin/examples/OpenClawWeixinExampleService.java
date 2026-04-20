package cn.langchat.openclaw.weixin.examples;

import cn.langchat.openclaw.weixin.OpenClawWeixinSdk;
import cn.langchat.openclaw.weixin.api.WeixinClientConfig;
import cn.langchat.openclaw.weixin.auth.QrLoginFlowResult;
import cn.langchat.openclaw.weixin.auth.QrLoginSession;
import cn.langchat.openclaw.weixin.monitor.WeixinMonitorStream;

import java.time.Duration;
import java.util.Objects;

/**
 * @since 2026-04-20
 * @author LangChat Team
 */
public final class OpenClawWeixinExampleService {
    private final OpenClawWeixinSdk sdk;

    public OpenClawWeixinExampleService(WeixinClientConfig config) {
        this.sdk = new OpenClawWeixinSdk(Objects.requireNonNull(config, "config"));
    }

    public OpenClawWeixinSdk sdk() {
        return sdk;
    }

    public QrLoginSession startQrLogin(String accountIdSeed) {
        return sdk.qrFlow().start(accountIdSeed, null, false);
    }

    public QrLoginFlowResult waitForConfirm(String sessionKey, Duration timeout) {
        return sdk.qrFlow().waitForConfirm(sessionKey, timeout, null);
    }

    public String sendText(String accountId, String toUserId, String text) {
        return sdk.sendText(accountId, toUserId, text);
    }

    public String sendMedia(String accountId, String toUserId, String mediaPathOrUrl, String caption) {
        return sdk.sendMedia(accountId, toUserId, mediaPathOrUrl, caption);
    }

    public WeixinMonitorStream newMonitorStream(String accountId) {
        return sdk.monitorStream(accountId);
    }

    public static OpenClawWeixinExampleService createDefault() {
        return new OpenClawWeixinExampleService(
            WeixinClientConfig.builder().build()
        );
    }

    public static OpenClawWeixinExampleService create(String baseUrl, String cdnBaseUrl) {
        return new OpenClawWeixinExampleService(
            WeixinClientConfig.builder()
                .baseUrl(baseUrl)
                .cdnBaseUrl(cdnBaseUrl)
                .build()
        );
    }
}
