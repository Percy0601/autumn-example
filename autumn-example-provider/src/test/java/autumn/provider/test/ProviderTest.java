package autumn.provider.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ProviderTest {

    @Test
    void test() {
        ProviderTemplate template = new ProviderTemplate();
        template.init();
    }
}
