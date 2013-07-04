package de.consol.research.cache.part03.ehcache;

import de.consol.research.cache.common.UserEventList;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import static de.consol.research.cache.common.ServletContextKeys.CACHE;

@Path("/events")
public class RestInterface {

    @Context
    private ServletContext context;
    private Cache cache;

    @PostConstruct
    public void init() {
        cache = (Cache) context.getAttribute(CACHE);
    }

//    @POST
//    @Path("{user}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public void appendEvent(@PathParam("user") String user, String msg) {
//        cache.putIfAbsent(new Element(user, UserEventList.emptyList()));
//        boolean success;
//        do {
//            Element oldElement = cache.get(user);
//            UserEventList oldList = (UserEventList) oldElement.getObjectValue();
//            UserEventList newList = UserEventList.append(oldList, msg);
//            Element newElement = new Element(user, newList);
//            success = cache.replace(oldElement, newElement);
//        }
//        while ( ! success );
//    }

    @POST
    @Path("{user}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void appendEvent(@PathParam("user") String user, String msg) {
        // not thread safe
        Element oldElement = cache.get(user);
        UserEventList oldList = oldElement == null ? UserEventList.emptyList() : (UserEventList) oldElement.getObjectValue();
        UserEventList newList = UserEventList.append(oldList, msg);
        Element newElement = new Element(user, newList);
        cache.put(newElement);
    }

    @GET
    @Path("{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> searchByUser(@PathParam("user") String user) {
        Element result = cache.get(user);
        if ( result == null ) {
            return new ArrayList<>();
        }
        return ((UserEventList) result.getObjectValue()).getMessages();
    }
}
