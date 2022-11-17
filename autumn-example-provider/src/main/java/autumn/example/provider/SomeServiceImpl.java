package autumn.example.provider;

import autumn.core.annotation.Export;
import io.training.autumn.api.SomeService;
import io.training.autumn.api.User;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Export
@Service
public class SomeServiceImpl implements SomeService.Iface {
    @Override
    public String echo(String msg) throws TException {
        return "Hello " + msg;
    }

    @Override
    public int addUser(User user) throws TException {
        return new Random().nextInt(20);
    }

    @Override
    public List<User> findUserByIds(List<Integer> idList) throws TException {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setUserId(1);
        user.setUsername("Someone");
        users.add(user);
        return users;
    }
}
