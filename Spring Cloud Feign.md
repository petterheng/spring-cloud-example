# 快速入门
Spring Cloud Feign整合了Spring Cloud Ribbon和Spring Cloud Hystrix的使用，除了提供了上述两种框架的强大功能之外，它还提供了一种
声明式的Web服务客户端定义方式。（相关代码在feign-consumer模块下）

**1、创建feign-consumer工程，在pom.xml种引入spring-cloud-starter-eureka和spring-cloud-starter-openfeign依赖。**

**2、创建应用程序主类ConsumerApplication，并通过@EnableFeignClients注解开启Spring Cloud Feign的支持功能。**

**3、定义HelloService接口，通过@FeignClient注解指定服务名来绑定服务，然后再使用Spring MVC注解来绑定具体该服务提供的REST接口。**

**4、新增FeignConsumerController来实现对Feign客户端的调用。**

**5、在application.properties中指定服务注册中心，并定义自身的服务名为feign-consumer，端口使用9001**