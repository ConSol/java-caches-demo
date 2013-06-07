package de.consol.research.cache.part03.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.net.URL;

import static de.consol.research.cache.common.ServletContextKeys.CACHE;

@WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener {

    // mvn tomcat7:run-war -pl ch03.ehcache -am verify -Dmaven.tomcat.port=8080 -Dehcache.config.filename=ehcache1.xml

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String cfg = "/" + System.getProperty("ehcache.config.filename", "ehcache1.xml");
        URL url = getClass().getResource(cfg);
        Cache cache = CacheManager.create(url).getCache("events");
        ServletContext context = servletContextEvent.getServletContext();
        context.setAttribute(CACHE, cache);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        CacheManager.getInstance().shutdown();
    }
}
