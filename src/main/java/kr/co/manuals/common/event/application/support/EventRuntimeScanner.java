package kr.co.manuals.common.event.application.support;

import kr.co.manuals.common.event.application.dto.RuntimeListenerResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 런타임에서 @EventListener 어노테이션이 붙은 메서드들을 스캔하는 컴포넌트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EventRuntimeScanner {

    private final ApplicationContext applicationContext;
    private final Map<String, List<RuntimeListenerResult>> cache = new ConcurrentHashMap<>();

    public Map<String, List<RuntimeListenerResult>> scanAllListeners() {
        if (!cache.isEmpty()) {
            return Collections.unmodifiableMap(cache);
        }
        
        Map<String, List<RuntimeListenerResult>> result = new LinkedHashMap<>();
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        
        for (String beanName : beanNames) {
            try {
                Object bean = applicationContext.getBean(beanName);
                Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
                
                for (Method method : targetClass.getDeclaredMethods()) {
                    if (!Modifier.isPublic(method.getModifiers())) continue;
                    
                    EventListener annotation = AnnotatedElementUtils.findMergedAnnotation(method, EventListener.class);
                    if (annotation == null) continue;
                    
                    Class<?>[] eventClasses = annotation.value().length > 0 
                            ? annotation.value() 
                            : method.getParameterTypes();
                    
                    for (Class<?> eventClass : eventClasses) {
                        String eventKey = eventClass.getName();
                        List<String> paramTypes = Arrays.stream(method.getParameterTypes())
                                .map(Class::getName)
                                .toList();
                        
                        RuntimeListenerResult listener = new RuntimeListenerResult(
                                beanName,
                                targetClass.getName(),
                                "ANNOTATION",
                                method.getName(),
                                eventKey,
                                paramTypes,
                                null
                        );
                        
                        result.computeIfAbsent(eventKey, k -> new ArrayList<>()).add(listener);
                    }
                }
            } catch (Exception e) {
                log.debug("Bean {} 스캔 실패: {}", beanName, e.getMessage());
            }
        }
        
        cache.putAll(result);
        return Collections.unmodifiableMap(result);
    }

    public List<RuntimeListenerResult> getListenersByEventKey(String eventKey) {
        Map<String, List<RuntimeListenerResult>> all = scanAllListeners();
        return all.getOrDefault(eventKey, Collections.emptyList());
    }

    public void clearCache() {
        cache.clear();
    }
}
