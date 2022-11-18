package autumn.consumer.test;

import autumn.core.pool.AutumnPool;
import autumn.core.pool.ConnectionFactory;
import autumn.core.pool.impl.ConcurrentBagEntry;
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
    private String service;
    private String interfaceName = SomeService.class.getName();

    public ConsumerTemplate(String service) {
        this.service = service;
    }


    @Override
    public String echo(String msg) {
        AutumnPool pool = AutumnPool.getInstance();
        ConcurrentBagEntry entry = pool.getConnection(this.service);
        TTransport transport = (TTransport) entry.getEntry();
        TProtocol protocol = new TBinaryProtocol(transport);
        TMultiplexedProtocol multiplexedProtocol = new TMultiplexedProtocol(protocol, interfaceName);
        SomeService.Client client = new SomeService.Client(multiplexedProtocol);
        try {
            String result = client.echo(msg);
            pool.release(service, entry);
            return result;
        } catch (TException e) {
            if(log.isDebugEnabled()) {
                log.debug("proxy invoke exception!");
            }
            pool.evict(service, entry);
            throw new AutumnException("proxy invoke exception!", e);
        }
    }

    @Override
    public int addUser(User user) {
        AutumnPool pool = AutumnPool.getInstance();
        ConcurrentBagEntry entry = pool.getConnection(this.service);
        TTransport transport = (TTransport) entry.getEntry();
        TProtocol protocol = new TBinaryProtocol(transport);
        TMultiplexedProtocol multiplexedProtocol = new TMultiplexedProtocol(protocol, interfaceName);
        SomeService.Client client = new SomeService.Client(multiplexedProtocol);
        try {
            int result = client.addUser(user);
            pool.release(service, entry);
            return result;
        } catch (TException e) {
            if(log.isDebugEnabled()) {
                log.debug("proxy invoke exception!");
            }
            pool.evict(service, entry);
            throw new AutumnException("proxy invoke exception!", e);
        }
    }

    @Override
    public List<User> findUserByIds(List<Integer> idList){
        AutumnPool pool = AutumnPool.getInstance();
        ConcurrentBagEntry entry = pool.getConnection(this.service);
        TTransport transport = (TTransport) entry.getEntry();
        TProtocol protocol = new TBinaryProtocol(transport);
        TMultiplexedProtocol multiplexedProtocol = new TMultiplexedProtocol(protocol, interfaceName);
        SomeService.Client client = new SomeService.Client(multiplexedProtocol);
        try {
            List<User> result = client.findUserByIds(idList);
            pool.release(service, entry);
            return result;
        } catch (TException e) {
            if(log.isDebugEnabled()) {
                log.debug("proxy invoke exception!");
            }
            pool.evict(service, entry);
            throw new AutumnException("proxy invoke exception!", e);
        }
    }
}
