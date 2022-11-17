package autumn.consumer.test;

import io.training.autumn.api.SomeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ClientTest {
    @Test
    void test() {
        String infaceName = SomeService.class.getName();

        log.info("================{}", infaceName);
    }
}
