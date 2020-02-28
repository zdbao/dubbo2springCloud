# euraka-server
eureka服务注册中心。 直接启动即可。端口好为8761

可以在application.yml中更改端口，更改后，依赖eureka的客户端也要变更。

# demo-dubbo-provider-client

dubbo服务二方包, 只提供dubbo服务

# demo-dubbo-provider

dubbo服务提供端。 

通过更改application.properties文件中的dubbo.registry.address变更zk地址

# demo-provider-client

另一个dubbo服务二方包

# demo-provider

另一个服务提供端，与`demo-dubbo-provider`不同之处在于，该应用除了提供dubbo协议外，还提供了springCloud的http协议。

同时，该应用还消费了`demo-dubbo-provider`, 用于测试springCloud访问dubbo的场景。

# demo-provider-api

`demo-provider`向外暴露springCloud的http协议后，消费者需要调用时锁需要依赖的二方包。

其中提供了fegin, 熔断， 自启动扫描配置。

# demo-consumer

消费端。

提供有三个http测试地址，分别测试了：

1. 访问dubbo
2. 访问springCloud
3. 访问springCloud->dubbo

