package com.holiday.matcloud.consumer;

import java.lang.reflect.Field;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import com.holiday.matcloud.consumer.annotation.RpcReference;
/**
 * 修改BeanDefinition用这个 -->BeanFactoryPostProcess后置处理器
 * 注册BeanDefinition用这个 -->BeanDefinitionRegistryPostProcessor后置处理器
 */
@Component
public class ConsumerRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	private final String INITMETHOD = "init";

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// 如果有其他类也实现beanFactoryPostProcess
		// BeanDefinitionRegistryPostProcessor 的postProcessBeanFactory 比
		// BeanFactoryPostProcess 的postProcessBeanFactory先执行
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		String[] beanDefinitionNames = registry.getBeanDefinitionNames();
		for (String beanName : beanDefinitionNames) {
			BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
			String beanClassName = beanDefinition.getBeanClassName();
			if (beanClassName != null) {
				Class<?> clazz = ClassUtils.resolveClassName(beanClassName, getClass().getClassLoader());
				parseRpcReference(clazz, registry);
			}

		}
	}

	private void parseRpcReference(Class<?> fieldclass, BeanDefinitionRegistry registry) {
		Field[] fields = fieldclass.getDeclaredFields();
		for (Field field : fields) {
			//判断字段上有没有自定义的RpcReference注解
			RpcReference annotation = AnnotationUtils.getAnnotation(field, RpcReference.class);
			if (annotation != null) {
				BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(RpcReferenceBean.class);
				//设置bean生命周期 Init方法
				builder.setInitMethodName(INITMETHOD);
				builder.addPropertyValue("interfaceClassName", field.getType());
				builder.addPropertyValue("serviceVersion", annotation.serviceVersion());
				builder.addPropertyValue("registryType", annotation.registryType());
				builder.addPropertyValue("registryAddr", annotation.registryAddress());
				builder.addPropertyValue("timeout", annotation.timeout());
				BeanDefinition beanDefinition = builder.getBeanDefinition();
				registry.registerBeanDefinition(field.getName(), beanDefinition);
			}
		}
	}

}
