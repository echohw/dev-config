package com.example.devconfig.interceptor;

import com.example.devconfig.properties.AccessLimitProperties;
import com.example.devconfig.properties.AccessLimitProperties.Properties;
import com.example.devutils.dep.Charsets;
import com.example.devutils.utils.access.RequestUtils;
import com.example.devutils.utils.access.ResponseUtils;
import com.example.devutils.utils.io.StreamUtils;
import com.example.devutils.utils.redis.RedisUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Created by AMe on 2020-06-22 12:16.
 */
@Component
public class AccessInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AccessInterceptor.class);

    @Autowired
    private RedisUtils<String, Object> redisUtils;
    @Autowired
    private AccessLimitProperties accessLimitProperties;

    private String scriptText;

    {
        String luaScriptFile = "scripts/access_limit.lua";
        logger.info("加载Lua文件: {}", luaScriptFile);
       try (
           InputStream inputStream = new ClassPathResource(luaScriptFile).getInputStream()
       ) {
           scriptText = StreamUtils.readAsString(inputStream, Charsets.UTF_8);
       } catch (IOException ex) {
           ex.printStackTrace();
       }
    }

    private String getClientId(HttpServletRequest request, String key) {
        String ipAddr = RequestUtils.getIp(request);
        return ipAddr + ":" + key;
    }

    private String getReqUri(HttpServletRequest request) {
        return RequestUtils.getUri(request);
    }

    private Long getAccessInUnit(String clientId, Integer timeUnit) {
        List<String> keys = Collections.singletonList(clientId);
        Object[] args = {timeUnit};
        return redisUtils.execScript(scriptText, keys, args, Long.class);
    }

    private boolean matchPath(HttpServletRequest request, String path) {
        String reqUri = getReqUri(request);
        return reqUri.equals(path); // TODO 如果路径与path相匹配
    }

    private Long getLimitInUnit(HttpServletRequest request, Long limitInUnit) {
        return limitInUnit;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean access = true;
        for (Entry<String, Properties> entry : accessLimitProperties.getLimits().entrySet()) {
            String key = entry.getKey();
            Properties props = entry.getValue();
            if (matchPath(request, props.getPath())) {
                String clientId = getClientId(request, key);
                Integer timeUnit = props.getTimeUnit();
                Long countInUnit = getAccessInUnit(clientId, timeUnit);
                Long limitInUnit = getLimitInUnit(request, props.getLimitInUnit());
                if (countInUnit > limitInUnit) {
                    access = false;
                    Long ttl = redisUtils.ttl(clientId, TimeUnit.SECONDS);
                    String hint = props.getHint();
                    hint = hint.replace("${ttl}", String.valueOf(ttl));
                    ResponseUtils.writeText(response, hint);
                }
                break;
            }
        }
        return access;
    }

}
