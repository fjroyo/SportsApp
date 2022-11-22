package com.mongodb.sports.model;

import com.mongodb.client.ClientSession;
import com.mongodb.client.TransactionBody;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;
import org.bson.types.ObjectId;


public class ManageInvitations {

    private MClient mClient = new MClient();

    public Document acceptInvitation(ObjectId eventId,
                                     String email,
                                     String team  //The value of this parameter will be "team1" or "team2"
                                    ) {
        ClientSession session = mClient.getClient().startSession();
        Document user = null;
        final String playersField =  team + ".players";

        try {
            user = session.withTransaction(
                    new TransactionBody<Document>() {
                        public Document execute() {
                            Document user = mClient.getUsersCollection().findOneAndUpdate(
                                    session,
                                    new Document("_id", email),
                                    new Document("$pull", new Document("pendingInvitations", new Document("eventId", eventId))),
                                    new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));
                            Document player = new Document("email", email)
                                    .append("firstName", user.getString("firstName"))
                                    .append("lastName", user.getString("lastName"));
                            mClient.getEventsCollection().updateOne(session,
                                    new Document("_id", eventId),
                                    new Document("$addToSet", new Document(playersField, player))
                            );
                            return user;
                        }
                    }
            );
        } catch (RuntimeException e) {
            //Transaction aborted, we could do error handling here
            throw e;
        } finally {
            session.close();
        }
        return user;
    }


    public Document rejectInvitation(ObjectId eventId,
                                     String email) {
        return mClient.getUsersCollection().findOneAndUpdate(
                new Document("_id", email),
                new Document("$pull", new Document("pendingInvitations", new Document("eventId", eventId))),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));
    }


}
