package de.consol.research.cache.part02.infinispan;

import de.consol.research.cache.common.UserEventList;
import org.infinispan.manager.DefaultCacheManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

import static de.consol.research.cache.common.ServletContextKeys.CACHE;
import static de.consol.research.cache.common.ServletContextKeys.CACHE_MANAGER;

@WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            DefaultCacheManager cacheManager = new DefaultCacheManager("infinispan.xml");
            ConcurrentMap<String, UserEventList> map = cacheManager.getCache();
            ServletContext context = servletContextEvent.getServletContext();
            context.setAttribute(CACHE, map);
            context.setAttribute(CACHE_MANAGER, cacheManager);
        } catch (IOException e) {
            throw new WebApplicationException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        DefaultCacheManager cacheManager = (DefaultCacheManager) context.getAttribute(CACHE_MANAGER);
        cacheManager.stop();
    }
}
