package nl.hu.bep.shopping.webservices;

import nl.hu.bep.shopping.model.Item;
import nl.hu.bep.shopping.model.Shop;
import nl.hu.bep.shopping.model.ShoppingList;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("list")
public class ListResource {

    @GET
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShoppingLists() {
        List<ShoppingList> shoppingLists = Shop.getShop().getAllShoppingLists();
        if (shoppingLists.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("error", "no lists present");

            return Response.status(404).entity(errors).build();
        }
        //java stream example, see @getShoppingListByName for a step by step output example

        return Response.ok(shoppingLists).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{name}")
    public String getShoppingListByName(@PathParam("name") String name) {
        Shop shop = Shop.getShop();
        JsonObjectBuilder listjob = Json.createObjectBuilder();
        ShoppingList list = shop.getShoppingListByName(name);
        //very specific JSON output to illustrate full control of parameters from domain to outside world
        try {
            listjob.add("name", list.getName());
            listjob.add("owner", list.getOwner().getName());
            JsonArrayBuilder jab = Json.createArrayBuilder();
            for (Item e : list.getListItems()) {
                JsonObjectBuilder itemjob = Json.createObjectBuilder();
                itemjob.add("itemName", e.getName());
                itemjob.add("itemAmount", e.getAmount());
                jab.add(itemjob);
            }
            listjob.add("items", jab);
        } catch (NullPointerException e) {
            listjob.add("error", "No list by that name");
        }
        return listjob.build().toString();

    }
}
