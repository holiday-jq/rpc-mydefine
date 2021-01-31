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
