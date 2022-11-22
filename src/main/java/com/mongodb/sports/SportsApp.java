package com.mongodb.sports;

import com.mongodb.sports.model.ManageEvents;
import com.mongodb.sports.model.ManageInvitations;
import com.mongodb.sports.model.ManageUsers;
import org.apache.velocity.tools.generic.DateTool;
import org.bson.Document;
import org.bson.types.ObjectId;
import spark.*;
import spark.template.velocity.*;

import java.text.SimpleDateFormat;
import java.util.*;
import static spark.Spark.*;

public class SportsApp {
    public static void main(String[] args) {
        staticFiles.location("/public");

        get("/", (req, res) -> renderEvents(req));
        get("/events", (req, res) -> renderEvents(req));
        get("/users", (req, res) -> renderUsers(req));

        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });
    }

    private static String renderEvents(Request req) throws Exception {
        String action = req.queryParams("action");
        Map<String, Object> model = new HashMap<>();
        ManageEvents manageEvents = new ManageEvents();

        if("addEvent".equals(action)) {

            Document event = manageEvents.addEvent(
                    req.queryParams("sport"),
                    req.queryParams("team1"),
                    req.queryParams("team2"),
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(req.queryParams("startTime")),
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(req.queryParams("endTime")),
                    req.queryParams("location")
            );

            model.put("event", event);

        } else if("findEvent".equals(action)) {

            List<Document> events = manageEvents.findEvents(
                    "".equals(req.queryParams("sport")) ? null : req.queryParams("sport"),
                    "".equals(req.queryParams("minStartTime")) ? null : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(req.queryParams("minStartTime")),
                    "".equals(req.queryParams("maxStartTime")) ? null : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(req.queryParams("maxStartTime")),
                     "".equals(req.queryParams("location")) ? null :req.queryParams("location"));

            if(events.size() > 0)
                model.put("event", events.get(0));
            //TODO: return list instead of single value
        }

        model.put("date", new DateTool());
        return new VelocityTemplateEngine().render(new ModelAndView(model, "velocity/pages/events.vm"));
    }


    private static String renderUsers(Request req) throws Exception {
        String action = req.queryParams("action");
        Map<String, Object> model = new HashMap<>();

        if("findUser".equals(action)){
            ManageUsers manageUsers = new ManageUsers();
            Document user = manageUsers.findUser(req.queryParams("email"));
            model.put("user", user);

        } else if("addUser".equals(action)) {
            ManageUsers manageUsers = new ManageUsers();
            Document user = manageUsers.addUser(
                    req.queryParams("email"),
                    req.queryParams("firstName"),
                    req.queryParams("lastName"),
                    req.queryParams("phone"),
                    req.queryParams("position")
            );
            model.put("user", user);

        } else if("addInterest".equals(action)) {
            ManageUsers manageUsers = new ManageUsers();
            Document user = manageUsers.addInterest(
                    req.queryParams("email"),
                    req.queryParams("interest")
            );
            model.put("user", user);

        } else if("removeInterest".equals(action)) {
            ManageUsers manageUsers = new ManageUsers();
            Document user = manageUsers.removeInterest(
                    req.queryParams("email"),
                    req.queryParams("interest")
            );
            model.put("user", user);

        } else if("acceptInvitation".equals(action)) {
            ManageInvitations manageInvitations = new ManageInvitations();
            Document user = manageInvitations.acceptInvitation(
                    new ObjectId(req.queryParams("eventId")),
                    req.queryParams("email"),
                    req.queryParams("team")
            );
            model.put("user", user);

        } else if("rejectInvitation".equals(action)) {
            ManageInvitations manageInvitations = new ManageInvitations();
            Document user = manageInvitations.rejectInvitation(
                    new ObjectId(req.queryParams("eventId")),
                    req.queryParams("email")
            );
            model.put("user", user);
        }
        model.put("date", new DateTool());
        return new VelocityTemplateEngine().render(new ModelAndView(model, "velocity/pages/users.vm"));
    }

}