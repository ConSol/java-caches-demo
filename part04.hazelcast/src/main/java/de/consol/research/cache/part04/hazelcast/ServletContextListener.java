package de.consol.research.cache.part04.hazelcast;

import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import de.consol.research.cache.common.UserEventList;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.ConcurrentMap;

import static de.consol.research.cache.common.ServletContextKeys.CACHE;

@WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getGroupConfig().setName("dev").setPassword("dev-pass");
        clientConfig.addAddress("localhost:5701", "localhost:5702");
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        ConcurrentMap<String, UserEventList> map = client.getMap("events");
        ServletContext context = servletContextEvent.getServletContext();
        context.setAttribute(CACHE, map);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Hazelcast.shutdownAll();
    }
}
