package com.mongodb.sports.model;

import com.mongodb.client.ClientSession;
import com.mongodb.client.TransactionBody;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ManageEvents {
    private MClient mClient = new MClient();

    public List<Document> findEvents(String sport,
                                     Date minStartTime,
                                     Date maxStartTime,
                                     String location) {

        Document query = new Document("sport", sport);
        query.append("location", location);
        Document startTimeRange = new Document("$gte", minStartTime)
                                      .append("$lte", maxStartTime);
        query.append("startTime", startTimeRange);
        return mClient.getEventsCollection().find(query)
                .sort(new Document("startTime", 1))
                .into(new ArrayList<Document>());
    }


    public Document addEvent(String sport,
                             String nameTeam1,
                             String nameTeam2,
                             Date startTime,
                             Date endTime,
                             String location) {

        Document event = new Document("_id", new ObjectId())
                .append("sport", sport)
                .append("startTime", startTime)
                .append("endTime", endTime)
                .append("location", location)
                .append("team1", new Document("name", nameTeam1))
                .append("team2", new Document("name", nameTeam2));

        Document invitation =  new Document("sport", sport)
                .append("team1", nameTeam1)
                .append("team2", nameTeam2)
                .append("startTime", startTime)
                .append("eventId", event.getObjectId("_id"));
        ClientSession session = mClient.getClient().startSession();
        try {
            session.withTransaction(
                    new TransactionBody<Void>() {
                        public Void execute() {
                            mClient.getEventsCollection().insertOne(session, event);
                            mClient.getUsersCollection().updateMany(
                                    session,
                                    new Document("interests", sport),
                                    new Document("$addToSet", new Document("pendingInvitations", invitation))
                            );
                            return null;
                        }
                    }
            );
        } catch (RuntimeException e) {
            //Transaction aborted, we could do error handling here
            throw e;
        } finally {
            session.close();
        }
        return event;
    }

}
