package io.github.kimmking.gateway.router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author guozq
 * @date 2020-11-04-6:06 下午
 */
public class HttpEndpointRouterLoadBalance implements HttpEndpointRouter {
    static Random random = new Random();

    @Override
    public String route(List<String> endpoints) {
        int num = random.nextInt(endpoints.size());
        return endpoints.get(num);
    }

    private String weightRandom(List<String> endpoints) {
        Map<String, Integer> endpointsMap = new HashMap<>();
        for (int i = 0; i < endpoints.size(); i++) {
            endpointsMap.put(endpoints.get(i), i);
        }
        ArrayList ipList = new ArrayList();
        for (Map.Entry<String, Integer> entry : endpointsMap.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                ipList.add(entry.getKey());
            }
        }
        int allWeight = endpointsMap.values().stream().mapToInt(a -> a).sum();
        int number = random.nextInt(allWeight);
        return (String) ipList.get(number);
    }
}
