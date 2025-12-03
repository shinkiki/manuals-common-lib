package kr.co.manuals.common.api.application.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SwaggerSuggester {
    private final Optional<OpenAPI> openAPI;
    private final ObjectMapper objectMapper;
    
    @Value("${springdoc.swagger-ui.url:}")
    private String apiDocsUrl;
    
    private OpenAPI effectiveSpec;

    @PostConstruct
    public void init() {
        this.effectiveSpec = loadSpec();
    }

    private OpenAPI loadSpec() {
        if (openAPI.isPresent() && openAPI.get().getPaths() != null && !openAPI.get().getPaths().isEmpty()) {
            return openAPI.get();
        }
        String url = (apiDocsUrl == null || apiDocsUrl.isBlank()) ? "http://localhost:8080/v3/api-docs" : apiDocsUrl;
        try {
            String json = WebClient.create(url)
                    .get()
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .onErrorResume(e -> Mono.empty())
                    .block();
            if (json != null && !json.isBlank()) {
                Map<String, Object> map = objectMapper.readValue(json, new TypeReference<>() {});
                OpenAPI loaded = new OpenAPI();
                if (map.containsKey("paths")) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> pathsMap = (Map<String, Object>) map.get("paths");
                    io.swagger.v3.oas.models.Paths paths = new io.swagger.v3.oas.models.Paths();
                    for (var entry : pathsMap.entrySet()) {
                        String pathKey = entry.getKey();
                        @SuppressWarnings("unchecked")
                        Map<String, Object> methodMap = (Map<String, Object>) entry.getValue();
                        PathItem pathItem = parsePathItem(methodMap);
                        paths.addPathItem(pathKey, pathItem);
                    }
                    loaded.setPaths(paths);
                }
                return loaded;
            }
        } catch (Exception ignored) {}
        return openAPI.orElse(null);
    }

    private PathItem parsePathItem(Map<String, Object> methodMap) {
        PathItem item = new PathItem();
        for (var me : methodMap.entrySet()) {
            String method = me.getKey().toLowerCase(Locale.ROOT);
            @SuppressWarnings("unchecked")
            Map<String, Object> opMap = (Map<String, Object>) me.getValue();
            Operation op = parseOperation(opMap);
            switch (method) {
                case "get" -> item.setGet(op);
                case "post" -> item.setPost(op);
                case "put" -> item.setPut(op);
                case "delete" -> item.setDelete(op);
                case "patch" -> item.setPatch(op);
            }
        }
        return item;
    }

    private Operation parseOperation(Map<String, Object> opMap) {
        Operation op = new Operation();
        op.setSummary((String) opMap.get("summary"));
        op.setDescription((String) opMap.get("description"));
        if (opMap.containsKey("parameters")) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> paramsList = (List<Map<String, Object>>) opMap.get("parameters");
            List<Parameter> params = new ArrayList<>();
            for (var pm : paramsList) {
                Parameter p = new Parameter();
                p.setName((String) pm.get("name"));
                p.setIn((String) pm.get("in"));
                p.setDescription((String) pm.get("description"));
                p.setRequired((Boolean) pm.get("required"));
                params.add(p);
            }
            op.setParameters(params);
        }
        if (opMap.containsKey("requestBody")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> rb = (Map<String, Object>) opMap.get("requestBody");
            io.swagger.v3.oas.models.parameters.RequestBody requestBody = parseRequestBody(rb);
            op.setRequestBody(requestBody);
        }
        if (opMap.containsKey("responses")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> respMap = (Map<String, Object>) opMap.get("responses");
            ApiResponses responses = new ApiResponses();
            for (var re : respMap.entrySet()) {
                @SuppressWarnings("unchecked")
                Map<String, Object> respObj = (Map<String, Object>) re.getValue();
                ApiResponse ar = parseApiResponse(respObj);
                responses.addApiResponse(re.getKey(), ar);
            }
            op.setResponses(responses);
        }
        return op;
    }

    private io.swagger.v3.oas.models.parameters.RequestBody parseRequestBody(Map<String, Object> rb) {
        io.swagger.v3.oas.models.parameters.RequestBody requestBody = new io.swagger.v3.oas.models.parameters.RequestBody();
        if (rb.containsKey("content")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> contentMap = (Map<String, Object>) rb.get("content");
            Content content = parseContent(contentMap);
            requestBody.setContent(content);
        }
        return requestBody;
    }

    private ApiResponse parseApiResponse(Map<String, Object> respObj) {
        ApiResponse ar = new ApiResponse();
        ar.setDescription((String) respObj.get("description"));
        if (respObj.containsKey("headers")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> headersMap = (Map<String, Object>) respObj.get("headers");
            Map<String, Header> headers = new LinkedHashMap<>();
            for (var he : headersMap.entrySet()) {
                Header h = new Header();
                h.setDescription(he.getKey());
                headers.put(he.getKey(), h);
            }
            ar.setHeaders(headers);
        }
        if (respObj.containsKey("content")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> contentMap = (Map<String, Object>) respObj.get("content");
            Content content = parseContent(contentMap);
            ar.setContent(content);
        }
        return ar;
    }

    private Content parseContent(Map<String, Object> contentMap) {
        Content content = new Content();
        for (var ce : contentMap.entrySet()) {
            MediaType mt = new MediaType();
            @SuppressWarnings("unchecked")
            Map<String, Object> mediaObj = (Map<String, Object>) ce.getValue();
            if (mediaObj.containsKey("schema")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> schemaObj = (Map<String, Object>) mediaObj.get("schema");
                Schema<?> schema = parseSchema(schemaObj);
                mt.setSchema(schema);
            }
            content.addMediaType(ce.getKey(), mt);
        }
        return content;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Schema<?> parseSchema(Map<String, Object> schemaObj) {
        String type = (String) schemaObj.get("type");
        if ("array".equals(type)) {
            ArraySchema as = new ArraySchema();
            if (schemaObj.containsKey("items")) {
                Map<String, Object> itemsObj = (Map<String, Object>) schemaObj.get("items");
                as.setItems(parseSchema(itemsObj));
            }
            return as;
        }
        if ("object".equals(type) || schemaObj.containsKey("properties")) {
            ObjectSchema os = new ObjectSchema();
            if (schemaObj.containsKey("properties")) {
                Map<String, Object> propsMap = (Map<String, Object>) schemaObj.get("properties");
                for (var pe : propsMap.entrySet()) {
                    Map<String, Object> propSchema = (Map<String, Object>) pe.getValue();
                    os.addProperty(pe.getKey(), parseSchema(propSchema));
                }
            }
            if (schemaObj.containsKey("example")) {
                os.setExample(schemaObj.get("example"));
            }
            return os;
        }
        Schema s = new Schema();
        s.setType(type);
        if (schemaObj.containsKey("format")) s.setFormat((String) schemaObj.get("format"));
        if (schemaObj.containsKey("example")) s.setExample(schemaObj.get("example"));
        return s;
    }

    @Getter
    public static class Examples {
        private final String header;
        private final String body;
        private final String response;
        private final String apiNm;
        private final String apiDsctn;

        public Examples(String header, String body, String response, String apiNm, String apiDsctn) {
            this.header = header;
            this.body = body;
            this.response = response;
            this.apiNm = apiNm;
            this.apiDsctn = apiDsctn;
        }
    }

    public record Endpoint(String method, String path, String apiNm, String apiDsctn) {}

    public List<Endpoint> endpoints() {
        if (effectiveSpec == null || effectiveSpec.getPaths() == null) return List.of();
        List<Endpoint> list = new ArrayList<>();
        effectiveSpec.getPaths().forEach((path, item) -> {
            addEndpointIfPresent(list, "GET", path, item.getGet());
            addEndpointIfPresent(list, "POST", path, item.getPost());
            addEndpointIfPresent(list, "PUT", path, item.getPut());
            addEndpointIfPresent(list, "DELETE", path, item.getDelete());
            addEndpointIfPresent(list, "PATCH", path, item.getPatch());
        });
        return list;
    }

    private void addEndpointIfPresent(List<Endpoint> list, String method, String path, Operation op) {
        if (op != null) {
            list.add(new Endpoint(method, path, op.getSummary(), op.getDescription()));
        }
    }

    public Examples suggest(String method, String path) {
        if (effectiveSpec == null || effectiveSpec.getPaths() == null) {
            return new Examples(null, null, null, null, null);
        }
        PathItem item = findPathItem(path);
        if (item == null) return new Examples(null, null, null, null, null);
        Operation op = getOperation(item, method);
        if (op == null) return new Examples(null, null, null, null, null);
        String headers = extractHeaders(op);
        String body = extractRequestBody(op);
        String response = extractResponse(op);
        return new Examples(headers, body, response, op.getSummary(), op.getDescription());
    }

    private PathItem findPathItem(String path) {
        if (path == null) return null;
        String normalized = normalizePath(path);
        for (var e : effectiveSpec.getPaths().entrySet()) {
            if (normalizePath(e.getKey()).equals(normalized) || matchPathTemplate(e.getKey(), path)) {
                return e.getValue();
            }
        }
        return null;
    }

    private boolean matchPathTemplate(String template, String actual) {
        String[] tParts = template.split("/");
        String[] aParts = actual.split("/");
        if (tParts.length != aParts.length) return false;
        for (int i = 0; i < tParts.length; i++) {
            String t = tParts[i];
            String a = aParts[i];
            if (t.startsWith("{") && t.endsWith("}")) continue;
            if (!t.equalsIgnoreCase(a)) return false;
        }
        return true;
    }

    private String normalizePath(String p) {
        if (p == null) return "";
        String r = p.trim().replaceAll("//+", "/");
        if (r.endsWith("/") && r.length() > 1) r = r.substring(0, r.length() - 1);
        return r.toLowerCase(Locale.ROOT);
    }

    private Operation getOperation(PathItem item, String method) {
        if (method == null) return null;
        return switch (method.toUpperCase(Locale.ROOT)) {
            case "GET" -> item.getGet();
            case "POST" -> item.getPost();
            case "PUT" -> item.getPut();
            case "DELETE" -> item.getDelete();
            case "PATCH" -> item.getPatch();
            default -> null;
        };
    }

    private String extractHeaders(Operation op) {
        if (op == null || op.getParameters() == null) return null;
        Map<String, Object> map = new LinkedHashMap<>();
        for (Parameter p : op.getParameters()) {
            if ("header".equalsIgnoreCase(p.getIn())) {
                map.put(p.getName(), exampleForSchema(p.getSchema()));
            }
        }
        return map.isEmpty() ? null : toJson(map);
    }

    private String extractRequestBody(Operation op) {
        if (op == null || op.getRequestBody() == null) return null;
        Content content = op.getRequestBody().getContent();
        if (content == null) return null;
        MediaType mt = content.get("application/json");
        if (mt == null) mt = content.values().stream().findFirst().orElse(null);
        if (mt == null || mt.getSchema() == null) return null;
        Object example = schemaToExample(mt.getSchema());
        return toJson(example);
    }

    private String extractResponse(Operation op) {
        if (op == null || op.getResponses() == null) return null;
        ApiResponses responses = op.getResponses();
        ApiResponse resp = responses.get("200");
        if (resp == null) resp = responses.get("201");
        if (resp == null) resp = responses.values().stream().findFirst().orElse(null);
        if (resp == null || resp.getContent() == null) return null;
        MediaType mt = resp.getContent().get("application/json");
        if (mt == null) mt = resp.getContent().get("*/*");
        if (mt == null) mt = resp.getContent().values().stream().findFirst().orElse(null);
        if (mt == null || mt.getSchema() == null) return null;
        Object example = schemaToExample(mt.getSchema());
        return toJson(example);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Object schemaToExample(Schema schema) {
        if (schema == null) return null;
        if (schema.getExample() != null) return schema.getExample();
        if (schema instanceof ArraySchema as) {
            Object itemEx = schemaToExample(as.getItems());
            return itemEx == null ? List.of() : List.of(itemEx);
        }
        if (schema instanceof ObjectSchema || (schema.getProperties() != null && !schema.getProperties().isEmpty())) {
            Map<String, Object> map = new LinkedHashMap<>();
            Map<String, Schema> props = schema.getProperties();
            if (props != null) {
                for (var e : props.entrySet()) {
                    map.put(e.getKey(), schemaToExample(e.getValue()));
                }
            }
            return map;
        }
        return exampleForSchema(schema);
    }

    @SuppressWarnings("rawtypes")
    private Object exampleForSchema(Schema schema) {
        if (schema == null) return null;
        if (schema.getExample() != null) return schema.getExample();
        String type = schema.getType();
        String format = schema.getFormat();
        if (type == null) return null;
        return switch (type) {
            case "string" -> formatStringExample(format);
            case "integer" -> 0;
            case "number" -> 0.0;
            case "boolean" -> false;
            case "array" -> List.of();
            default -> null;
        };
    }

    private Object formatStringExample(String format) {
        if (format == null) return "string";
        return switch (format) {
            case "uuid" -> "00000000-0000-0000-0000-000000000000";
            case "date" -> "2024-01-01";
            case "date-time" -> "2024-01-01T00:00:00";
            case "email" -> "user@example.com";
            case "uri" -> "https://example.com";
            default -> "string";
        };
    }

    private String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return obj.toString();
        }
    }
}
