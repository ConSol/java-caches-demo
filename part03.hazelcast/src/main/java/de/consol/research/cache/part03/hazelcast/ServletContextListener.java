package de.consol.research.cache.part03.hazelcast;

import com.hazelcast.config.Config;
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
        Config cfg = new Config();
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
        ConcurrentMap<String, UserEventList> map = instance.getMap("events");
        ServletContext context = servletContextEvent.getServletContext();
        context.setAttribute(CACHE, map);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Hazelcast.shutdownAll();
    }
}
