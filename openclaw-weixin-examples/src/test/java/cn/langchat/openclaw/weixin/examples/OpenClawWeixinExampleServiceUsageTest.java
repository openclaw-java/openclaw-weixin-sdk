package cn.langchat.openclaw.weixin.examples;

import cn.langchat.openclaw.weixin.api.WeixinClientConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @since 2026-04-20
 * @author LangChat Team
 */
class OpenClawWeixinExampleServiceUsageTest {

    @Test
    void shouldCreateServiceAndComposeMonitorCallbacksWithoutNetwork() {
        OpenClawWeixinExampleService service = new OpenClawWeixinExampleService(
            WeixinClientConfig.builder()
                .baseUrl(WeixinClientConfig.DEFAULT_BASE_URL)
                .cdnBaseUrl(WeixinClientConfig.DEFAULT_CDN_BASE_URL)
                .build()
        );

        var stream = service.newMonitorStream("demo-account")
            .onStart(() -> {
            })
            .onMessage(msg -> {
            })
            .onEvent(event -> {
            })
            .onLog((level, message) -> {
            })
            .onError(err -> {
            })
            .onStop(() -> {
            });

        assertNotNull(stream);
        assertFalse(stream.isRunning());
    }

    @Test
    void shouldCreateDefaultService() {
        OpenClawWeixinExampleService service = OpenClawWeixinExampleService.createDefault();
        assertNotNull(service);
        assertNotNull(service.sdk());
    }
}
