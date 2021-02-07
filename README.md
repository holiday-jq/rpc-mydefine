从0开始实现简易版RPC框架                                                                                                                                                       
项目启动前置条件：
1：配置好maven，下载项目后，首先npm install一下，让子模块打包。                                                                                                              
2：安装好zookeeper并启动                                                                                                                                                    
3：配置rpc-provider的application.yml文件，把注册中心地址改成你启动好的zookper地址                                                                                                 
4：配置rpc-consumer的自定义注解的属性（在Controller注入使用那里）改成你的注册中心地址                                                                                                   
5：启动provider项目                                                                                                                                                       
6：启动consumer项目 然后浏览器访问Controller --》http://localhost:8080/rpc/consumer 返回rpc调用成功信息！                                                                       



工程主要分为6个模块：                                                                                                                                                            
1：registry注册中心模块：目前主流的注册中心有 ZooKeeper、Eureka、Etcd、Consul、Nacos，选择一个高性能、高可用的注册中心对 RPC 框架至关重要 我们可以定义一个接口，里面发布服务的方法，删除服务的方法，获取服务的方法，然后具体接口实现类可以自己扩展，可以是zookpeer,或者是Eureka，当然还需要感知有多少服务端节点可用，然后从中选取一个进行调用，在获取服务的方法里面我们可以实现不同的负载均衡算法来获取服务，此项目采用基于哈希环的一致性哈希算法。                                                                                                                         
2：provider服务提供者模块： 新建一个暴露RPC服务的自定义注解，然后通过spring的BeanPostProcessor后置处理器 检查该bean是否带有自定义注解，如果有就把注解上的属性读取出来，例如服务的版本，服务的接口名称、启动该服务的主机ip和端口 然后通过一些客户端框架保存到zookeeper节点上。                                                                                                     
3：protocol通信协议编解码和序列化：网络通信采用Netty，然后定义rpc协议，例如协议的组成可以是：魔数、协议版本号、序列化算法号、报文状态、报文类型、报文的长度、报文的实际二进制数据，我们可以利用Netty 提供了两个最为常用的编解码抽象基类 MessageToByteEncoder 和 ByteToMessageDecoder，帮助我们很方便地扩展实现自定义协议，当然还有其他开箱即用的拆包器，如基于长度域拆包器 LengthFieldBasedFrameDecoder、分隔符拆包器 DelimiterBasedFrameDecoder， 最后序列化算法可以是JSON、Hessian、或者是谷歌的protoBuffer
自定义rpc协议                                                                                                                                                                              
4:intertface接口模块：仅定义接口和方法，具体实现在provider模块                                                                                                            
5：core核心模块：定义一些共具类和公用实体类                                                                                                                                      
6：consumer消费者和动态代理模块：定义一个调用服务的自定义注解，此注解是作用于字段，项目启动时通过spring的BeanDefinitionRegistryPostProcessor后置处理器，判断Bean的字段是否有自定义的注解，如果有就往IOC容器里面加入beanDefintion，并且该beanDefintion是一个FactoryBean，在该bean生命周期的时候为其创建一个代理类，这样IOC容器就会多了这个bean，并且是一个增强后的代理类。代理类的内容无非是屏蔽远程调用的细节，让调用远程服务就像调用本地服务一样。 

     对比Dubbo：                                                                                                                                                               
                                                                                                                                                                             
1：dubbo的SPI机制提供了极大的扩展性。例如按需加载、AOP（包装类）、IOC功能，如protocol的调用，通过ExtensionLoader.getExtensionLoader(protocol.class).getAdaptiveExtension()获取一个代理类，可以通过URL的参数，根据参数决定调用那个实现类。                                                                                                                                                   
2：从provider服务提供端来看，dubbo提供端启动流程：在ioc容器刷新完成时广播容器刷新完成方法，然后默认先在本地暴露服务，用得是jvm协议，服务端有可能自己掉用本地的服务，这样可以避免网络传输开销，   然后通过代理工厂生成invoker，再通过Protocol代理类进行暴露服务，通过Netty绑定端口，然后再将服务暴露到注册中心（如zookpeer），最后将服务暴露信息，记录在记录表的一个hashMap中。                                 
3：从服务端启动来看，dubbo启动流程：读取配置信息，根据配置信息从注册中心订阅服务，然后获取到provider地址、端口、接口信息后，开启NettyClient连接服务端，然后创建invoker,通过invoker为服务接⼝⽣成代理对象,ReferenceBean是个工厂FactoryBean,可以在getObject这个方法在创建bean的时候提供扩展。 这个代理对象⽤于远程调⽤provider.                                                                                    
4：当然还有很多，例如dubbo的集群容错机制、监控中心、过滤器、负载均衡算法 
