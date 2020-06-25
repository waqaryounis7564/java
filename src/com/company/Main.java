package com.company;


import javafx.util.Pair;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

   public static Pair<String, Integer> sc(){
       return new Pair<String, Integer>("waqar",29);
   }
    public static void main(String[] args) throws IOException, ParseException {

       Pair<String ,Integer> p=sc();
        System.out.println(p.getKey());
        System.out.println(p.getValue());
        // PimData.scrapeData();

        //HkBot.scrapeData();
        //  PermData.scrapeData();
       // SenatorsData.scrapeData();
        // Lithuania_Bot.scrapeData();



    }
}
