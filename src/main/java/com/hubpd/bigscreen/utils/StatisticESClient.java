package com.hubpd.bigscreen.utils;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.io.IOException;
import java.net.InetAddress;

/**
 * 运营分析分析所使用的es
 *
 * @author SongLude
 */
public class StatisticESClient {
    private volatile static TransportClient client = null;

    /**
     * @return
     * @throws IOException
     */
    public static TransportClient getClientIns() throws IOException {
        if (client == null) {
            synchronized (StatisticESClient.class) {
                if (client == null) {
                    client = getClient();
                }
            }
        }
        return client;
    }

    public static void setNull() {
        if (client != null) {
            synchronized (StatisticESClient.class) {
                if (client != null) {
                    client = null;
                }
            }
        }
    }


    private static TransportClient getClient() throws IOException {
        String clustername = ESConfigConstants.ES_STATISTIC_CLUSTER_NAME;
        String nodes = ESConfigConstants.ES_STATISTIC_TRANSPORT_NODES;
        String node[] = nodes.split(";");

        Settings settings = Settings.settingsBuilder().put("cluster.name", clustername).build();
        TransportClient client = TransportClient.builder().settings(settings).build();
        for (String str : node) {
            String value[] = str.split(":");
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(value[0]), Integer.parseInt(value[1])));
        }
        return client;
    }

    public static void close() {
        if (client != null) {
            client.close();
        }
    }
}
