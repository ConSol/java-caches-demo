package de.consol.research.cache.part03.infinispan;

import de.consol.research.cache.common.UserEventList;
import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;

import static de.consol.research.cache.common.ServletContextKeys.CACHE;
import static de.consol.research.cache.common.ServletContextKeys.CACHE_MANAGER;

@WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            String cfg = System.getProperty("infinispan.config.filename", "infinispan1.xml");
            EmbeddedCacheManager cacheManager = new DefaultCacheManager(cfg);
            Cache<String, UserEventList> cache = cacheManager.getCache();
            ServletContext context = servletContextEvent.getServletContext();
            context.setAttribute(CACHE_MANAGER, cacheManager);
            context.setAttribute(CACHE, cache);
        } catch (IOException e) {
            throw new WebApplicationException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        EmbeddedCacheManager cacheManager = (EmbeddedCacheManager) context.getAttribute(CACHE_MANAGER);
        cacheManager.stop();
    }
}
