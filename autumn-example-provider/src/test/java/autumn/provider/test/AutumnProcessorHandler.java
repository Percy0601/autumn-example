package autumn.provider.test;

import autumn.core.extension.AttachableProcessor;
import autumn.core.model.provider.AutumnProcessor;
import autumn.example.provider.SomeServiceImpl;
import io.training.autumn.api.SomeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AutumnProcessorHandler implements AutumnProcessor {
    @Autowired(required = false)
    SomeService.Iface someService;


    @Override
    public TProcessor multiplexedProcessor() {
        TMultiplexedProcessor processor = new TMultiplexedProcessor();
        SomeService.Iface someService = new SomeServiceImpl();
        TProcessor tprocessor = new SomeService.Processor<>(someService);
        TProcessor proxyProcessor = new AttachableProcessor(tprocessor);
        processor.registerProcessor(SomeService.class.getName(), proxyProcessor);

        return processor;
    }
}
