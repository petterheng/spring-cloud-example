package cn.sh.ribbon.service;

import cn.sh.common.entity.User;
import cn.sh.ribbon.command.UserCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author sh
 */
@Service
public class HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloService.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 使用@HystrixCommand注解指定回调方法
     * @param name
     * @return
     */
    @HystrixCommand(fallbackMethod = "ribbonHelloFallback", commandKey = "helloKey")
    public String ribbonHello(String name) {
        long start = System.currentTimeMillis();
        String result = restTemplate.getForObject("http://HELLO-SERVICE/hello?name=" + name, String.class);
        long end = System.currentTimeMillis();
        logger.info("Spend Time:" + (end - start));
        return result;
    }

    public String ribbonHelloFallback(String name) {
        return "Hello, this is fallback";
    }


    /**
     * 第一种使用命令的方式
     * @param id
     * @return
     */
    public User getUserById(Long id) {
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("userKey");
        com.netflix.hystrix.HystrixCommand.Setter setter = com.netflix.hystrix.HystrixCommand.Setter.withGroupKey(groupKey);
        UserCommand userCommand = new UserCommand(setter, restTemplate, id);
        // 同步执行获取结果
//        return userCommand.execute();
        // 异步执行获取结果
        Future<User> future = userCommand.queue();
        try {
            return future.get();
        } catch (Exception e) {
            logger.info("获取结果发生异常", e);
        }
        return null;
    }


    /**
     * 通过注解方式同步执行获取User
     * @param id
     * @return
     */
    @HystrixCommand
    public User findUserById(Long id) {
        return restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
    }

    /**
     * 通过注解方式异步执行获取User
     * @param id
     * @return
     */
    public User asyncFindUserById(Long id) {
        Future<User> userFuture = asyncFindUserFutureById(id);
        try {
            return userFuture.get();
        } catch (Exception e) {
            logger.error("获取结果发生异常", e);
        }
        return null;
    }

    /**
     * 通过注解方式异步执行获取User
     * @param id
     * @return
     */
    @HystrixCommand
    public Future<User> asyncFindUserFutureById(Long id) {
        return new AsyncResult<User>() {
            @Override
            public User invoke() {
                return restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
            }
        };
    }
}
