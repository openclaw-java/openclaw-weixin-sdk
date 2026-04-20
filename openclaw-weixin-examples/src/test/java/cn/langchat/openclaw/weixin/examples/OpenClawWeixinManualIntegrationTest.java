package cn.langchat.openclaw.weixin.examples;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @since 2026-04-20
 * @author LangChat Team
 */
class OpenClawWeixinManualIntegrationTest {

    @Test
    @Disabled("Manual integration test: requires real Weixin QR login environment")
    void manualQrLoginFlow() {
        OpenClawWeixinExampleService service = OpenClawWeixinExampleService.createDefault();

        var session = service.startQrLogin(null);
        assertNotNull(session.qrcodeUrl());

        var result = service.waitForConfirm(session.sessionKey(), Duration.ofMinutes(8));
        assertNotNull(result);
    }
}
