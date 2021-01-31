package com.holiday.matcloud.registry.loadbalancer;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.curator.x.discovery.ServiceInstance;
import com.holiday.matcloud.common.ServiceMeta;

/**
 *  服务消费者在发起RPC调用之前，需要感知有多少服务端节点可用，然后从中选择一个进行调用
 *   常用的负载均衡策略： 轮询、权重、最少连接数、一致性hash
 */
/**
 * 一致性Hash算法 可以保证每个服务节点分摊的流量尽可能均匀，而且能把服务节点扩缩带来的影响降低
 */
public class consistentHashLoadBalancer implements ServiceLoadBalancer<ServiceInstance<ServiceMeta>> {

	private final static int VIRTUAL_NODE_SIZE = 10;

	private final static String VIRTUAL_NODE_SPLIT = "#";

	@Override
	public ServiceInstance<ServiceMeta> select(List<ServiceInstance<ServiceMeta>> servers, int hashCode) {
		TreeMap<Integer, ServiceInstance<ServiceMeta>> ring = makeConsistentHashRing(servers);
		return allocateNode(ring, hashCode);
	}

	/**
	 * 分配服务
	 * @param ring
	 * @param hashCode
	 * @return
	 */
	private ServiceInstance<ServiceMeta> allocateNode(TreeMap<Integer, ServiceInstance<ServiceMeta>> ring,
			int hashCode) {
		Map.Entry<Integer, ServiceInstance<ServiceMeta>> entry = ring.ceilingEntry(hashCode);
		// 如果找不到大于等于客户端hashcode的节点， 那就直接返回TreeMap的第一个节点。
		if (entry == null) {
			entry = ring.firstEntry();
		}
		return entry.getValue();
	}

	/**
	 * 创建哈希环 计算出每个服务实例 ServiceInstance 的地址和端口对应的 hashCode， 然后直接放入 TreeMap 中，TreeMap
	 * 会对 hashCode 默认从小到大进行排序
	 * 
	 * @param servers
	 * @return
	 */
	private TreeMap<Integer, ServiceInstance<ServiceMeta>> makeConsistentHashRing(List<ServiceInstance<ServiceMeta>> servers) {
		TreeMap<Integer, ServiceInstance<ServiceMeta>> ring = new TreeMap<>();
		for (ServiceInstance<ServiceMeta> instance : servers) {
			// 虚拟节点
			for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
				// ip+port + # + i 组成的字符串 再计算hashcode
				ring.put((buildServiceInstanceKey(instance) + VIRTUAL_NODE_SPLIT + i).hashCode(), instance);
			}
		}
		return ring;
	}

	private String buildServiceInstanceKey(ServiceInstance<ServiceMeta> instance) {
		ServiceMeta payload = instance.getPayload();
		return String.join(":", payload.getServiceAddress(), String.valueOf(payload.getServicePort()));
	}

}
