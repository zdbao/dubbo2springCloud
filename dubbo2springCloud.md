# 服务端升级

## 1. 独立 dto

将二方 client 包中的 dto 独立出单独的二方包。

如原dubbo二方包名为demo-provider-client, 其中的dto类移到单独的dto二方包中(比如名字为demo-provider-dto)

样例：
```java
import com.example.provider.dto.User;

public interface UserService {
    Map<String,Object> getUserByConfKey(String confKey);
    User getUserById(int id);
}
```
`User`本来在demo-provider-client中，将其移动到独立的dto二方包demo-provider-client-dto中。
demo-provider-client的pom引入demo-provider-dto依赖。

## 2. remote 服务应用提供 http 协议

    首先保证remote服务已经升级为springBoot, jdk版本不能低于在1.8

* **增加 springCloud 依赖**

    注意： 可以同时保留dubbo

```xml
<properties>
    <java.version>1.8</java.version>
    <spring-cloud.version>Hoxton.SR1</spring-cloud.version>
</properties>
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

* **增加http请求地址**

为原service实现类中的方法增加http接口地址
样例代码：
```java
@Service
@RestController
public class UserServiceImpl implements UserService {

    @Reference
    private CmsConfService cmsConfService;

    @Override
    @RequestMapping("/user/getUserByConfKey")
    public Map<String, Object> getUserByConfKey(@RequestParam String confKey) {
        Map<String,Object> map = new HashMap<>();
        map.putAll(cmsConfService.getByConfKey(confKey));
        map.put("user",getUserById(1));
        return map;
    }
```
增加了`@RestController`与`@RequestMapping`。 
如果需要，入参需要增加`@RequestParam`

**调整application.properties文件**
```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
server.port=8777
```
如上，增加注册中心地址，以及暴露出应用的http端口。

## 3.为http接口增加api二方包

    增加新二方包的目的是为了减轻依赖方的代码工作量

如原dubbo二方包名为demo-provider-client, 新提供的http接口二方包名为demo-provider-http-client。

样例如下： (注意引入dto二方包demo-provider-dto)
```xml
<groupId>com.example</groupId>
<artifactId>demo-provider-http-client</artifactId>
<version>0.0.1-SNAPSHOT</version>
<name>howbuy-cms-http-client</name>
 <properties>
    <java.version>1.8</java.version>
    <spring-cloud.version>Hoxton.SR1</spring-cloud.version>
</properties>
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
<dependencies>
    <dependency>
        <groupId>com.example</groupId>
        <artifactId>demo-provider-dto</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
        <exclusions>
            <exclusion>
                <groupId>org.junit.vintage</groupId>
                <artifactId>junit-vintage-engine</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
</dependencies>
```

* **增加启动配置**

> 如果是通过spring.io生成的springCloud或springBoot应用，请删除Application启动类。

```java
@Configuration
@ComponentScan(basePackages = "com.example.provider")
public class DemoProviderConfiguration {
}
```

* **增加spring.factories**

resources/META-INF目录下增加spring.factories, 请根据自己实际情况更改
```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.example.provider.DemoProviderConfiguration
```

* **增加接口api**

根据demo-provider-client中的interface复刻一份新的到demo-provider-http-client中。 请保证包路径，类名，方法名，入参类型，入参个数都相同。(`目的是为了方便依赖方切换调用协议`)

样例：
```java
@FeignClient(value = "demo-provider",fallback = UserServiceHystrix.class)
public interface UserService {
    @RequestMapping(value = "/user/getUserByConfKey",method = RequestMethod.GET)
    Map<String,Object> getUserByConfKey(@RequestParam("confKey") String confKey);

    @RequestMapping(value = "/user/getUserById",method = RequestMethod.GET)
    User getUserById(@RequestParam("id") int id);
}
```

> 注意： service的方法上要增加`@RequestMapping`,如果有必要，请在入参增加`@RequestParam`。

如果不需要用到熔断器，可以去掉`@FeignClient`


# 消费端升级

> 请先保证应用已经升级为springBoot, jdk版本不能低于1.8

* **增加springCloud包依赖**

pom.xml

```xml
<properties>
    <java.version>1.8</java.version>
    <dubbo.version>2.7.3</dubbo.version>
    <spring-cloud.version>Hoxton.SR1</spring-cloud.version>
</properties>
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
<dependencies>
<!-- 如果使用的不是eureka,请根据实际情况切换自己的注册中心 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependencies>
```

* **增加springCloud配置**

application.properties

```properties
server.port=8080

eureka.client.service-url.defaultZone=http://localhost:8761/eureka

feign.hystrix.enabled=true
#接口超时时间
feign.client.config.demo-provider.connect-timeout=2000
feign.client.config.demo-provider.read-timeout=2000

#设置多久触发熔断器。单位ms。最好比接口的超时时间大
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=20000
#并发执行的最大线程数，默认10
hystrix.threadpool.default.coreSize=1000
```

> 注意： 配置中的feign.***可以不用配置，具体根据实际情况，如所依赖的服务已经升级为springCloud，并且服务放提供的`*-http-client`有熔断器，建议设置feign

* **增加启动注解**

```java
@EnableEurekaClient
@EnableDiscoveryClient
@EnableDubbo(scanBasePackages = {"com.example.consumer.controller"})
@SpringBootApplication
public class DemoConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoConsumerApplication.class, args);
    }
}
```

* **依赖的服务升级为springCloud协议**

如果依赖的服务已经提供有springCloud的http协议。变更pom依赖：

如： cms服务dubbo协议二方包为demo-provider-client, 升级后springCloud协议二方包为demo-provider-http-client
删除demo-provider-client依赖，加入demo-provider-http-client依赖

pom.xml

```xml
<dependencies>
    <dependency>
        <groupId>com.example</groupId>
        <artifactId>demo-provider-http-client</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
</dependencies>
```

> 如果依赖的服务有如熔断器等配置，请根据实际情况，调整application.properties
