package io.training.autumn.consumer.test;

import cloud.micronative.autumn.core.pool.AutumnPool;
import cloud.micronative.autumn.core.pool.impl.ConnectionConfig;
import cloud.micronative.autumn.core.pool.impl.ConnectionFactory;
import io.training.autumn.api.SomeService;
import io.training.autumn.api.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.junit.platform.commons.util.ExceptionUtils;

import java.util.List;
import java.util.function.Function;

@Data
@Slf4j
public class ClientTemplate implements SomeService.Iface {
    private String service = "default/some-service";
    private String interfaceName = SomeService.class.getName();

    private SomeService.Client client = null;
    public ClientTemplate(String service) {
        this.service = service;
        init();
    }

    public void init() {
        Function<ConnectionConfig, ? extends TServiceClient> consumer = ( it -> {
            String ip = it.getIp();
            String port = it.getPort();
            TTransport socket = new TSocket(ip, Integer.valueOf(port));
            TTransport transport = new TFramedTransport(socket);
            TProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol multiplexedProtocol = new TMultiplexedProtocol(protocol, interfaceName);
            SomeService.Client client = new SomeService.Client(multiplexedProtocol);
            try {
                socket.open();
                return client;
            } catch (TTransportException e) {
                log.warn("init client exception, config:{}, interface:{}, exception:", it);
                return null;
            }
        });
        ConnectionFactory factory = ConnectionFactory.getInstance();
        factory.registry(service, consumer);
        AutumnPool pool =  AutumnPool.getInstance();

    }


    @Override
    public String echo(String msg) {

        return null;
    }

    @Override
    public int addUser(User user) {
        return 0;
    }

    @Override
    public List<User> findUserByIds(List<Integer> idList){
        return null;
    }
}
