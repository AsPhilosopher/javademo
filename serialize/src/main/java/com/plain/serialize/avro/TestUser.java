package com.plain.serialize.avro;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class TestUser {
    @Test
    public void testCreateUserClass() throws IOException {
        User user1 = new User();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);
        // Leave favorite color null

        // Alternate constructor
        User user2 = new User("Ben", 7, "red");

        // Construct via builder
        User user3 = User.newBuilder()
                .setName("Charlie")
                .setFavoriteColor("blue")
                .setFavoriteNumber(null)
                .build();

        // Serialize user1, user2 and user3 to disk
        DatumWriter<User> userDatumWriter = new SpecificDatumWriter(User.class);
        DataFileWriter<User> dataFileWriter = new DataFileWriter(userDatumWriter);
        dataFileWriter.create(user1.getSchema(), new File("users.avro"));
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.append(user3);
        dataFileWriter.close();

        // Deserialize Users from disk
        DatumReader<User> userDatumReader = new SpecificDatumReader(User.class);
        DataFileReader<User> dataFileReader = new DataFileReader(new File("users.avro"), userDatumReader);
        User user = null;
        while (dataFileReader.hasNext()) {
            // Reuse user object by passing it to next(). This saves us from
            // allocating and garbage collecting many objects for files with
            // many items.
            user = dataFileReader.next(user);
            System.out.println(user);
        }
    }

    @Test
    public void myUserTest() throws IOException {
        MyUser myUser = new MyUser();
        myUser.setName("Lincon");
        myUser.setAge(11);
        myUser.setAddress("China");

        DatumWriter<MyUser> userDatumWriter = new SpecificDatumWriter(MyUser.class);
        DataFileWriter<MyUser> dataFileWriter = new DataFileWriter(userDatumWriter);
        dataFileWriter.create(myUser.getSchema(), new File("my-user.avro"));
        dataFileWriter.append(myUser);
        dataFileWriter.close();

        DatumReader<MyUser> userDatumReader = new SpecificDatumReader(MyUser.class);
        DataFileReader<MyUser> dataFileReader = new DataFileReader(new File("my-user.avro"), userDatumReader);
        MyUser user = null;
        while (dataFileReader.hasNext()) {
            user = dataFileReader.next(user);
            System.out.println(user);
        }
    }
}
