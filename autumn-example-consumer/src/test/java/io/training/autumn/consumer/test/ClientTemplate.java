package io.training.autumn.consumer.test;

import cloud.micronative.autumn.core.pool.AutumnPool;
import cloud.micronative.autumn.core.pool.impl.ConnectionConfig;
import cloud.micronative.autumn.core.pool.impl.ConnectionFactory;
import io.training.autumn.api.SomeService;
import io.training.autumn.api.User;
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

public class ClientTemplate implements SomeService.Iface {
    private String serviceName = "default/some-service";
    public ClientTemplate() {

    }

    public void init() {
        Function<ConnectionConfig, ? extends TServiceClient> consumer = ( it -> {


            return null;
        });
        ConnectionFactory factory = ConnectionFactory.getInstance();
        factory.registry(serviceName, consumer);
    }

    private static void handleMultiProtocol() {
        System.out.println("客户端启动....");
        TTransport transport = null;
        try {
            String addr = "localhost:8761";
            String[] ipAndPort = addr.split(":");
            TTransport tsocket = new TSocket(ipAndPort[0], Integer.valueOf(ipAndPort[1]));
            transport = new TFramedTransport(tsocket);
            TProtocol protocol = new TBinaryProtocol(transport);
//            TProtocol protocol = new TBinaryProtocol(transport);
            //io.training.thrift.api.SomeService$
            TMultiplexedProtocol multiplexedProtocol = new TMultiplexedProtocol(protocol, SomeService.class.getName() + "$");
            SomeService.Client client = new SomeService.Client(multiplexedProtocol);
            tsocket.open();
            for(int i = 0; i < 10000; i++) {
                String hello = client.echo("kdfasdf" + i);
                System.out.println("======" + hello);
            }

        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
    }

    @Override
    public String echo(String msg) throws TException {
        return null;
    }

    @Override
    public int addUser(User user) throws TException {
        return 0;
    }

    @Override
    public List<User> findUserByIds(List<Integer> idList) throws TException {
        return null;
    }
}
