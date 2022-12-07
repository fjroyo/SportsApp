package com.mongodb.sports.model;

import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;



public class ManageUsers {

    private MClient mClient = new MClient();

    public Document findUser(String email) {
        Document d = new Document("_id", email);
        return mClient.getUsersCollection().find(d).first();
    }

    // TODO - insert user
    public Document addUser(String email,
                        String firstName,
                        String lastName,
                        String phone,
                        String position) {

    	Document user = null; // TODO - quitar esta línea tras completar el ejercicio
    	
    	// TODO - completar el código con la lógica para insertar un nuevo usuario

        return user;
    }

    public boolean deleteUser(String email) {
        DeleteResult result =  mClient.getUsersCollection().deleteOne(new Document("_id", email));
        return result.getDeletedCount() > 0;
    }


    public Document addInterest(String email,
                                String interest) {

        return mClient.getUsersCollection().findOneAndUpdate(
                new Document("_id", email),
                new Document("$addToSet", new Document("interests", interest)),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
        );
    }

    public Document removeInterest(String email,
                                   String interest) {

        return mClient.getUsersCollection().findOneAndUpdate(
                            new Document("_id", email),
                            new Document("$pull", new Document("interests", interest)),
                            new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
        );
    }

}
