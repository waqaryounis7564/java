package com.company;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SenatorBioDesc {
    private static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36 Edg/83.0.478.44";

    public static void Scrap() {
        try {
            List<String> senatorsUrl = new ArrayList<>();
            Connection.Response response;
            for (int page = 1; page <= 27; page++) {
                response = Jsoup.connect("https://www.govtrack.us/congress/members/current?sort=sortname&page=" + page + "&faceting=false&allow_redirect=false&do_search=1")
                        .userAgent(userAgent)
                        .followRedirects(true)
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .header("Accept", "application/json")
                        .header("Accept-Language", "en-US,en;q=0.9")
                        .followRedirects(false)
                        .method(Connection.Method.GET)
                        .timeout(1000000)
                        .execute();
                JSONObject jsonObject = new JSONObject(response.body());
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (Object obj : jsonArray) {
                    String data = obj.toString();
                    Document document = Jsoup.parse(data);
                    // String name=document.select("a").text();
                    String href = document.select("a").attr("href");
                    String url = "https://www.govtrack.us" + href;
                    senatorsUrl.add(url);
                }

            }
            Document document=null;
            for (String url : senatorsUrl) {
                document=Jsoup.connect(url).get();
                String name=document.select("h1").text();
                String description=document.select("#track_panel_base > div > p").text().replace("(view map)","");
                System.out.println(name);
                System.out.println(description);
                System.out.println("------------------");


            }


            System.out.println(senatorsUrl.size());


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }


    }

