package autumn.consumer.test;

import autumn.core.controller.AutumnFileController;
import autumn.core.pool.impl.ConnectionConfig;
import io.training.autumn.api.SomeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TServiceClient;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;

@Slf4j
public class ClientTest {
    @Test
    void test() {
        String infaceName = SomeService.class.getName();

        log.info("================{}", infaceName);
        ConsumerTemplate template = new ConsumerTemplate("default/some-service");

        AutumnFileController controller = new AutumnFileController();
        controller.applyConsumerConfig();
        long start = System.currentTimeMillis();
//        for(int i = 0; i < 10000; i++) {
//            String result = template.echo("world");
//            log.info("================, result:{}", result);
//        }

        ExecutorService es = Executors.newFixedThreadPool(10);

        List<Future<String>> futureList = new ArrayList<>();
        for(int i = 0; i < 1000000; i++) {
            Future<String> future = es.submit(() -> {
                String result = template.echo("world");
                return result;
            });
            futureList.add(future);
        }

        futureList.parallelStream().forEach(it -> {
            try {
                String result = it.get();
                //log.info("================, result:{}", result);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        long end = System.currentTimeMillis();
        log.info("================, time:{}", (end - start));
    }

    @Test
    void test2() {
        ConcurrentHashMap<String, Function<ConnectionConfig, ? extends TServiceClient>> registry = new ConcurrentHashMap<>();
        registry.put("default/some-service/io.training.autumn.api.SomeService", new Function<ConnectionConfig, TServiceClient>() {
            @Override
            public TServiceClient apply(ConnectionConfig connectionConfig) {
                return null;
            }
        });

        log.info("=================={}", registry.containsKey("default/some-service/io.training.autumn.api.SomeService"));
    }
}
