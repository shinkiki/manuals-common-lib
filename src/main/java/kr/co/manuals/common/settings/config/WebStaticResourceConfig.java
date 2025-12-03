package kr.co.manuals.common.settings.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

/**
 * 프론트엔드 정적 리소스 서빙 설정
 * - /static/** 경로로 정적 파일 서빙
 * - SPA 라우팅을 위해 존재하지 않는 경로는 index.html로 포워딩
 * 
 * 활성화: manuals.common.frontend.enabled=true
 */
@Configuration
@ConditionalOnProperty(
    prefix = "manuals.common.frontend",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = false
)
public class WebStaticResourceConfig implements WebMvcConfigurer {
    
    private static final String[] STATIC_RESOURCE_LOCATIONS = {
        "classpath:/static/",
        "classpath:/public/"
    };
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 정적 리소스 핸들러 (js, css, images 등)
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/static/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
        
        // 루트 경로 정적 파일 (favicon, manifest 등)
        registry.addResourceHandler("/favicon.ico", "/manifest.json", "/robots.txt", "/*.png", "/*.svg")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
        
        // SPA 라우팅: API가 아닌 모든 경로에 대해 index.html 반환
        registry.addResourceHandler("/**")
                .addResourceLocations(STATIC_RESOURCE_LOCATIONS)
                .setCachePeriod(0)
                .resourceChain(true)
                .addResolver(new SpaPathResourceResolver());
    }
    
    /**
     * SPA를 위한 커스텀 리소스 리졸버
     * 존재하지 않는 리소스 요청 시 index.html 반환
     */
    private static class SpaPathResourceResolver extends PathResourceResolver {
        
        @Override
        protected Resource getResource(String resourcePath, Resource location) throws IOException {
            Resource resource = super.getResource(resourcePath, location);
            
            // 리소스가 존재하면 그대로 반환
            if (resource != null && resource.exists()) {
                return resource;
            }
            
            // API 경로는 제외 (v1.0, api, swagger 등)
            if (resourcePath.startsWith("v1.0/") || 
                resourcePath.startsWith("api/") || 
                resourcePath.startsWith("swagger") ||
                resourcePath.startsWith("actuator/")) {
                return null;
            }
            
            // 정적 파일 확장자는 제외
            if (hasStaticExtension(resourcePath)) {
                return null;
            }
            
            // 그 외 경로는 index.html로 포워딩 (SPA 라우팅)
            Resource indexResource = new ClassPathResource("/static/index.html");
            return indexResource.exists() ? indexResource : null;
        }
        
        private boolean hasStaticExtension(String path) {
            String lowerPath = path.toLowerCase();
            return lowerPath.endsWith(".js") || 
                   lowerPath.endsWith(".css") || 
                   lowerPath.endsWith(".map") ||
                   lowerPath.endsWith(".png") || 
                   lowerPath.endsWith(".jpg") || 
                   lowerPath.endsWith(".jpeg") ||
                   lowerPath.endsWith(".gif") || 
                   lowerPath.endsWith(".svg") || 
                   lowerPath.endsWith(".ico") ||
                   lowerPath.endsWith(".woff") || 
                   lowerPath.endsWith(".woff2") || 
                   lowerPath.endsWith(".ttf") ||
                   lowerPath.endsWith(".json") || 
                   lowerPath.endsWith(".xml") ||
                   lowerPath.endsWith(".txt");
        }
    }
}
