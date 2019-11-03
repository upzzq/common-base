//package com.xbd.svc.common.config;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.aop.Advisor;
//import org.springframework.aop.aspectj.AspectJExpressionPointcut;
//import org.springframework.aop.support.DefaultPointcutAdvisor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.TransactionDefinition;
//import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
//import org.springframework.transaction.interceptor.RollbackRuleAttribute;
//import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
//import org.springframework.transaction.interceptor.TransactionAttribute;
//import org.springframework.transaction.interceptor.TransactionInterceptor;
//
///**
// * AOP配置全局事物,有了此配置service包下的方法不需要用事物注解
// * @author administrator
// *
// */
////@Configuration
//public class TransactionManagerConfig {
//	//事物超时时间,单位秒
//	//private static final int TX_METHOD_TIMEOUT = 30;
//	/*切点：拦截com.xbd.svc.*.service包下所有类的所有方法,返回值类型任意的方法*/
//	private static final String AOP_POINTCUT_EXPRESSION="execution (* com.xbd.svc.*.service..*(..))";
//
//	@Autowired
//	private PlatformTransactionManager transactionManager;
//
//
//	//@Bean
//	public TransactionInterceptor txAdvice() {
//		//事务管理规则，声明具备事务管理的方法名
//		NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
//		//只读事物、不做更新删除等
//		//当前存在事务就用当前的事务，当前不存在事务就创建一个新的事务(这里使用默认,默认PROPAGATION_REQUIRED)
//		RuleBasedTransactionAttribute readOnlyRule = new RuleBasedTransactionAttribute();
//		//设置当前事务是否为只读事务，true为只读
//		readOnlyRule.setReadOnly(true);
//		//transactiondefinition 定义事务的隔离级别；
//		//PROPAGATION_NOT_SUPPORTED事务传播级别5，以非事务运行，如果当前存在事务，则把当前事务挂起(查询的方法不需要事物)
//		readOnlyRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
//
//		//回滚事物
//		RuleBasedTransactionAttribute requireRule = new RuleBasedTransactionAttribute();
//		//抛出异常后执行切点回滚
//		requireRule.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(RuntimeException.class)));
//		//PROPAGATION_REQUIRED:事务隔离性为1，若当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是默认值。
//		requireRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//		//设置事务失效时间,默认永不超时
//		//requireRule.setTimeout(TX_METHOD_TIMEOUT);
//
//
//		//设置方法名应用的规则
//		Map<String,TransactionAttribute> txMap = new HashMap<String,TransactionAttribute>();
//
//		//需要事物的方法名
//		txMap.put("add*",requireRule);
//		txMap.put("save*", requireRule);
//		txMap.put("insert*",requireRule);
//		txMap.put("update*",requireRule);
//		txMap.put("delete*",requireRule);
//		txMap.put("remove*",requireRule);
//
//		//只读的、不需要事物的方法名
//		txMap.put("get*",readOnlyRule);
//		txMap.put("query*", readOnlyRule);
//		txMap.put("find*", readOnlyRule);
//		txMap.put("select*",readOnlyRule);
//		source.setNameMap(txMap);
//
//		TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager, source);
//		return txAdvice;
//
//		/*DefaultTransactionAttribute txAttr_REQUIRED = new DefaultTransactionAttribute();
//		txAttr_REQUIRED.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//		//txAttr_REQUIRED.se
//
//		DefaultTransactionAttribute txAttr_REQUIRED_READONLY = new DefaultTransactionAttribute();
//		txAttr_REQUIRED_READONLY.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//		txAttr_REQUIRED_READONLY.setReadOnly(true);
//
//		NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
//		source.addTransactionalMethod("add*", txAttr_REQUIRED);
//		source.addTransactionalMethod("save*", txAttr_REQUIRED);
//		source.addTransactionalMethod("delete*", txAttr_REQUIRED);
//		source.addTransactionalMethod("update*", txAttr_REQUIRED);
//		source.addTransactionalMethod("exec*", txAttr_REQUIRED);
//		source.addTransactionalMethod("set*", txAttr_REQUIRED);
//		source.addTransactionalMethod("get*", txAttr_REQUIRED_READONLY);
//		source.addTransactionalMethod("query*", txAttr_REQUIRED_READONLY);
//		source.addTransactionalMethod("find*", txAttr_REQUIRED_READONLY);
//		source.addTransactionalMethod("list*", txAttr_REQUIRED_READONLY);
//		source.addTransactionalMethod("count*", txAttr_REQUIRED_READONLY);
//		source.addTransactionalMethod("is*", txAttr_REQUIRED_READONLY);
//		return new TransactionInterceptor(transactionManager, source);*/
//	}
//
//	//@Bean
//	public Advisor txAdviceAdvisor(){
//		//声明切点的面
//		//切面（Aspect）：切面就是通知和切入点的结合。通知和切入点共同定义了关于切面的全部内容——它的功能、在何时和何地完成其功能。
//		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//		//声明和设置需要拦截的方法,用切点语言描写
//		pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
//		//设置切面=切点pointcut+通知TxAdvice
//		return new DefaultPointcutAdvisor(pointcut, txAdvice());
//	}
//}
