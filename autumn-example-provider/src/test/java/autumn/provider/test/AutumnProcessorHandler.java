package autumn.provider.test;

import autumn.core.extension.AttachableProcessor;
import autumn.core.model.provider.AutumnProcessor;
import autumn.core.model.provider.ProcessorEntry;
import io.training.autumn.api.SomeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AutumnProcessorHandler implements AutumnProcessor {
    @Autowired
    SomeService.Iface someService;
    public ProcessorEntry buildSomeService() {
        TProcessor tprocessor = new SomeService.Processor<>(someService);
        TProcessor proxyProcessor = new AttachableProcessor(tprocessor);

        ProcessorEntry entry = new ProcessorEntry();

        return null;
    }


    @Override
    public TProcessor multiplexedProcessor() {



        return null;
    }
}
