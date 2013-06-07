package de.consol.research.cache.part01;

import de.consol.research.cache.common.UserEventList;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.ConcurrentHashMap;

import static de.consol.research.cache.common.ServletContextKeys.CACHE;

@WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        context.setAttribute(CACHE, new ConcurrentHashMap<String, UserEventList>());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {}
}
