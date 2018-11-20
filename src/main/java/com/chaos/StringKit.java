package com.chaos;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.*;

/**
 * Created by chaos on 2018/6/1.
 */
public interface StringKit {

    static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    static String shortUUID() {
        String[] chars = new String[] {
                "a", "b", "c", "d", "e", "f",
                "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
                "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z" };
        StringBuilder stringBuilder = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            stringBuilder.append(chars[x % 0x3E]);
        }
        return stringBuilder.toString();
    }

    static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    static String percentage(double value) {
        return percentage(value, 0);
    }

    static String percentage(double value, int decimal) {
        return String.format("%." + decimal + "f%%", value * 100);
    }

    static String serialize(Map map) {
        return serialize(map, ",", "=");
    }

    static String serialize(Map map, String on, String separator) {
        return Joiner.on(on).withKeyValueSeparator(separator).useForNull("null").join(map);
    }

    static String serialize(Iterable iterable) {
        return serialize(iterable, ",");
    }

    static String serialize(Iterable iterable, String on) {
        return Joiner.on(on).join(iterable);
    }

    /**
     * @param hostPort 127.0.0.1:8080-8082,127.0.0.2:8080-8082,127.0.0.1:8088
     * @return 127.0.0.1:8080,127.0.0.1:8081,127.0.0.1:8082,127.0.0.2:8080,127.0.0.2:8081,127.0.0.2:8082,127.0.0.1:8088
     */
    static String flatHostPort(String hostPort) {
        List<String> hostPortList = Splitter.on(",").splitToList(hostPort);
        Map<String, String> hostPortMap = new HashMap<>();
        hostPortList.forEach(hp -> {
            String host = hp.split(":")[0];
            hostPortMap.merge(host, hp.split(":")[1], (oldVal, newVal)-> oldVal + "," + newVal);
        });
        Set<String> hostPortSet = new HashSet<>();
        MapOptional.ofEmpty(hostPortMap).each((host, ports) -> {
            Splitter.on(",").splitToList(ports).forEach(p -> {
                for (int port : IntegerKit.flatInteger(Splitter.on("-").splitToList(p).stream().mapToInt(Integer::parseInt).toArray())) {
                    hostPortSet.add(host + ":" + port);
                }
            });
        });
        return serialize(new ArrayList(hostPortSet));
    }

    static void main(String... a) {
        System.out.println(flatHostPort("127.0.0.1:8080-8082"));
    }
}
