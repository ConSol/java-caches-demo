package de.consol.research.cache.part04.infinispan;

import de.consol.research.cache.common.UserEventList;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import static de.consol.research.cache.common.ServletContextKeys.CACHE;
import static de.consol.research.cache.common.ServletContextKeys.CACHE_MANAGER;

@WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        RemoteCacheManager cacheManager = new RemoteCacheManager("localhost:11222;localhost:11223");
        RemoteCache<String, UserEventList> cache = cacheManager.getCache();
        ServletContext context = servletContextEvent.getServletContext();
        context.setAttribute(CACHE_MANAGER, cacheManager);
        context.setAttribute(CACHE, cache);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        RemoteCacheManager cacheManager = (RemoteCacheManager) context.getAttribute(CACHE_MANAGER);
        cacheManager.stop();
    }
}
