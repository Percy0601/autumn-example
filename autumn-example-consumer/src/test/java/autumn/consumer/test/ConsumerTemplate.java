package autumn.consumer.test;

import autumn.core.pool.AutumnPool;
import autumn.core.pool.ConnectionFactory;
import autumn.core.pool.impl.ConnectionConfig;
import autumn.core.util.AutumnException;
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

import java.util.List;
import java.util.function.Function;

@Data
@Slf4j
public class ConsumerTemplate implements SomeService.Iface {
    private String service = "default/some-service";
    private String interfaceName = SomeService.class.getName();

    public ConsumerTemplate(String service) {
        this.service = service;
        init();
    }

    private void init() {
        Function<ConnectionConfig, ? extends TServiceClient> consumer = (it -> {
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
        factory.registry(service, interfaceName, consumer);
    }

    private SomeService.Client getClient() {
        SomeService.Client client = (SomeService.Client)AutumnPool.getInstance().getConnection(service, interfaceName);
        return client;
    }

    @Override
    public String echo(String msg) {
        try {
            return getClient().echo(msg);
        } catch (TException e) {
            if(log.isDebugEnabled()) {
                log.debug("proxy invoke exception!");
            }
            throw new AutumnException("proxy invoke exception!", e);
        }
    }

    @Override
    public int addUser(User user) {
        try {
            return getClient().addUser(user);
        } catch (TException e) {
            if(log.isDebugEnabled()) {
                log.debug("proxy invoke exception!");
            }
            throw new AutumnException("proxy invoke exception!", e);
        }
    }

    @Override
    public List<User> findUserByIds(List<Integer> idList){
        try {
            return getClient().findUserByIds(idList);
        } catch (TException e) {
            if(log.isDebugEnabled()) {
                log.debug("proxy invoke exception!");
            }
            throw new AutumnException("proxy invoke exception!", e);
        }
    }
}
