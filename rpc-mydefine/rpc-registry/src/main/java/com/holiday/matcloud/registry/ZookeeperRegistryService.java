package com.holiday.matcloud.registry;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import com.holiday.matcloud.common.RpcServiceUtils;
import com.holiday.matcloud.common.ServiceMeta;
import com.holiday.matcloud.registry.loadbalancer.consistentHashLoadBalancer;

public class ZookeeperRegistryService implements RegistryService {
	/**
	 * 初始的sleep时间，
	 */
	private static final int BASE_SLEEP_TIME_MS = 1000;
	/**
	 * 最大重试次数。
	 */
	public static final int MAX_RETRIES = 3;

	/**
	 * 服务注册到Zookeeper开始节点路径
	 */
	public static final String PATH = "/rpc-mydefine";

	private final ServiceDiscovery<ServiceMeta> serviceDiscovery;

	public ZookeeperRegistryService(String zookeeperAdderssPort) throws Exception {
		// 其中RetryPolicy为重试策略，第一个参数为baseSleepTimeMs初始的sleep时间，
		// 用于计算之后的每次重试的sleep时间。第二个参数为maxRetries，最大重试次数。
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES);
		CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperAdderssPort, retryPolicy);
		client.start();
		JsonInstanceSerializer<ServiceMeta> serializer = new JsonInstanceSerializer<>(ServiceMeta.class);
		serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMeta.class)
				.client(client)
				.serializer(serializer)
				.basePath(PATH)
				.build();
		serviceDiscovery.start();
	}

	@Override
	public void register(ServiceMeta meta) throws Exception {
		ServiceInstance<ServiceMeta> serviceInstance = commonResolving(meta);
		serviceDiscovery.registerService(serviceInstance);
	}

	@Override
	public void unRegister(ServiceMeta meta) throws Exception {
		// 代码构造跟上面一样 只是 注册和 移除注册的区别
		ServiceInstance<ServiceMeta> serviceInstance = commonResolving(meta);
		serviceDiscovery.unregisterService(serviceInstance);
	}

	/**
	 *  拿到服务列表之后 可以实现   负载均衡算法  选择其中一个服务
	 */
	@Override
	public ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception {
		// TODO Auto-generated method stub
        Collection<ServiceInstance<ServiceMeta>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        List<ServiceInstance<ServiceMeta>> instanceList = (List<ServiceInstance<ServiceMeta>>) serviceInstances;
		//一致性hash均衡策略
        ServiceInstance<ServiceMeta> instance = new consistentHashLoadBalancer().select(instanceList, invokerHashCode);
        if (instance != null) {
			 return instance.getPayload();
		}
        return null;
	}

	/**
	 * 公用解析方法
	 * @param meta
	 * @return ServiceInstance<T>
	 * @throws Exception
	 */
	public ServiceInstance<ServiceMeta> commonResolving(ServiceMeta meta) throws Exception {
		ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance.<ServiceMeta>builder()
				.name(RpcServiceUtils.createServiceKey(meta.getServiceName(), meta.getServiceVersion()))
				.address(meta.getServiceAddress())
				.port(meta.getServicePort())
				.payload(meta)
				.build();
		return serviceInstance;
	}

	@Override
	public void destroy() throws IOException {
		serviceDiscovery.close();		
	}

}
