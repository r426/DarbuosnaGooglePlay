package com.ryeslim.darbuosna.model;

import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class WorkWithProverbs {

    private int index = -1;
    private ArrayList<Proverb> theCurrentList;//the list of proverbs seen during this session
    private static WorkWithProverbs instance = null;
    private Context context;
    private int bookmarkIndex;


    //constructor
    private WorkWithProverbs() {
        theCurrentList = new ArrayList<>();
    }

    //singleton
    public static WorkWithProverbs getInstance() {
        if (instance == null) {
            instance = new WorkWithProverbs();
        }
        return instance;
    }

    public Proverb getTheNext() {
        index++;
        if (index == theCurrentList.size()) {
            Proverb proverb = AllProverbs.getInstance().getRandom();
            theCurrentList.add(proverb);
        }
        return theCurrentList.get(index);
    }

    public Proverb getThePrevious() {
        index--;
        if (index < 0) {
            index = theCurrentList.size() - 1;
        }
        return theCurrentList.get(index);
    }

    public Proverb getTheFirst() {
        index = 0;
        return theCurrentList.get(index);
    }

    public Proverb getTheLast() {
        index = theCurrentList.size() - 1;
        return theCurrentList.get(index);
    }

    public void addToTheFile() {

        String fileName = "bookmarks.txt";
        FileWriter writer = null;
        File file = new File(context.getFilesDir(), fileName);
        try {
            writer = new FileWriter(file, true);//will add to the existing information in the file

        } catch (IOException e) {
            e.printStackTrace();
        }
        String prefix = "\n";

        if (writer != null) {
            try {
                writer.write(theCurrentList.get(index).getTheID() + " ");
                writer.write(theCurrentList.get(index).getProverb() + prefix);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //adapted from https://developer.android.com/training/sharing/send#java
    public void shareSomehow() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, theCurrentList.get(index).getProverb());
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Send to"));
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Proverb> listOfBookmarks = null;

    //reads the saved favorites list from the file
    public ArrayList readBookmarks() {

        listOfBookmarks = new ArrayList<>();
        String fileName = "bookmarks.txt";
        File file = new File(context.getFilesDir(), fileName);
        try {

            FileReader reader = new FileReader(file);
            Scanner sc = new Scanner(reader);

            while (sc.hasNext()) {
                short dummyID = sc.nextShort();
                String dummyProverb = sc.nextLine().trim();
                listOfBookmarks.add(new Proverb(dummyID, dummyProverb));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // listOfBookmarks is reversed "upside down" so that the newest saved proverbs are shown at the top
        Collections.reverse(listOfBookmarks);
        return listOfBookmarks;
    }

    public void removeFromArray() {
        listOfBookmarks.remove(bookmarkIndex);
    }

    public void saveTheUpdatedList() {

        String fileName = "bookmarks.txt";
        FileWriter writer = null;
        File file = new File(context.getFilesDir(), fileName);
        try {
            writer = new FileWriter(file, false);//will empty the file before writing

        } catch (IOException e) {
            e.printStackTrace();
        }
        // listOfBookmarks is "upside down" so that the newest saved proverbs are shown at the top
        // but in the file the new proverbs are added to the end
        // that is why the the listOfProverbs should be reversed before writing to the file
        // and then reversed back to be ready to be shown on the screen
        Collections.reverse(listOfBookmarks);
        for (int index = 0; index < listOfBookmarks.size(); index++) {
            String prefix = "\n";
            if (writer != null) {
                try {
                    writer.write(listOfBookmarks.get(index).getTheID() + " ");
                    writer.write(listOfBookmarks.get(index).getProverb() + prefix);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Collections.reverse(listOfBookmarks);
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isBookmarked(short thisID) {
        for (bookmarkIndex = 0; bookmarkIndex < listOfBookmarks.size(); bookmarkIndex++) {
            if (listOfBookmarks.get(bookmarkIndex).getTheID() == thisID) return true;
        }
        return false;
    }
}
