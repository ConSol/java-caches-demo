package de.consol.research.cache.part04.infinispan;

import de.consol.research.cache.common.UserEventList;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.VersionedValue;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/events")
public class RestInterface {

    @Context
    private ServletContext context;
    private RemoteCache<String, UserEventList> map; // <-- not map!

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void init() {
        map = (RemoteCache) context.getAttribute("cache");
    }

    @POST
    @Path("{user}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void appendEvent(@PathParam("user") String user, String msg) {
        map.putIfAbsent(user, UserEventList.emptyList());
        boolean success;
        do {
            UserEventList oldMsgList = map.get(user);
            UserEventList newMsgList = UserEventList.append(oldMsgList, msg);
//            success = map.replace(user, oldMsgList, newMsgList); // <!-- unsupported operation exception !!!
            VersionedValue valueBinary = map.getVersioned(user);
            success = map.replaceWithVersion(user, newMsgList, valueBinary.getVersion());
        }
        while ( ! success );
    }

    @GET
    @Path("{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> searchByUser(@PathParam("user") String user) {
        UserEventList result = map.get(user);
        if ( result == null ) {
            return new ArrayList<>();
        }
        return result.getMessages();
    }
}
