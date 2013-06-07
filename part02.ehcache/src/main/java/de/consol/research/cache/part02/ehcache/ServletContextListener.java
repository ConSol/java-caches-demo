package de.consol.research.cache.part02.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import static de.consol.research.cache.common.ServletContextKeys.CACHE;

@WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Cache cache = CacheManager.getInstance().getCache("events");
        ServletContext context = servletContextEvent.getServletContext();
        context.setAttribute(CACHE, cache);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        CacheManager.getInstance().shutdown();
    }
}
