package com.globant.service;

import com.globant.model.System.ExchangeSystem;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class ExchangeSystemService {
    public static void write() {
        try (FileOutputStream fileToWrite = new FileOutputStream("system.ser");
             ObjectOutputStream writer = new ObjectOutputStream(fileToWrite)) {
             writer.writeObject(ExchangeSystem.getInstance());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read() {
        try (FileInputStream fileToRead = new FileInputStream("system.ser");
             ObjectInputStream reader = new ObjectInputStream(fileToRead)) {
            ExchangeSystem instance = (ExchangeSystem) reader.readObject();
            ExchangeSystem.setInstance(instance);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            write();
        }
    }
}