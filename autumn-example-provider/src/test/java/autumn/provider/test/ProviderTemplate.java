package autumn.provider.test;

import autumn.core.extension.AttachableBinaryProtocol;
import autumn.core.extension.AttachableProcessor;
import autumn.example.provider.SomeServiceImpl;
import io.training.autumn.api.SomeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.Option;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TTransportFactory;

@Slf4j
public class ProviderTemplate {

    public void init() {
        TNonblockingServerSocket serverTransport = null;
        try {
            serverTransport = new TNonblockingServerSocket(30880);
        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }
        //异步IO，需要使用TFramedTransport，它将分块缓存读取。
        TTransportFactory transportFactory = new TFramedTransport.Factory();
        //使用高密度二进制协议
        //TProtocolFactory proFactory = new TBinaryProtocol.Factory();
        TProtocolFactory proFactory = new AttachableBinaryProtocol.Factory();

        TMultiplexedProcessor processor = new TMultiplexedProcessor();
        SomeService.Iface someService = new SomeServiceImpl();
        TProcessor tprocessor = new SomeService.Processor<>(someService);
        TProcessor proxyProcessor = new AttachableProcessor(tprocessor);
        processor.registerProcessor(SomeService.class.getName(), proxyProcessor);

        TServer server = new TThreadedSelectorServer(new
                TThreadedSelectorServer.Args(serverTransport)
                .transportFactory(transportFactory)
                .protocolFactory(proFactory)
                .processor(processor)
        );
        server.serve();
    }
}
