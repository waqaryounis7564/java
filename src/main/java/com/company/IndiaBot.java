package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;


public class IndiaBot {
    public static void scrapeData(){
        try {
            Date now = new Date();
            LocalDate localDate = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            int month = localDate.getMonthValue();
            int year = localDate.getYear();
            cal.clear();
            cal.set(Calendar.YEAR, year);

            for (int currentMonth = month; currentMonth > 0; currentMonth--) {
                cal.set(Calendar.MONTH, currentMonth);
                YearMonth yearMonthObject = YearMonth.of(year, currentMonth);
                int days = yearMonthObject.lengthOfMonth();
                for (int currentDate = days; currentDate > 0; currentDate--) {

                    String loopDate = year + "/" + currentMonth + "/" + currentDate;
                    DateFormat sdf = new SimpleDateFormat("yyyy/M/d");
                    Date dates = sdf.parse(loopDate);
                    String formattedDate = new SimpleDateFormat("yyyyMMdd").format(dates);
                    String url = "https://api.bseindia.com/BseIndiaAPI/api/AnnGetData/w?strCat=-1&strPrevDate=" + formattedDate + "&strScrip=&strSearch=P&strToDate=" + formattedDate + "&strType=C";

                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("User-Agent", "Mozilla/5.0");
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("Table");
                    if (jsonArray.length() == 0) continue;

                    for (Object object : jsonArray) {
                        JSONObject jsonObject1 = new JSONObject(object.toString());
                        String newsId = jsonObject1.getString("NEWSID");
                        System.out.println(newsId);
                        int companyId = jsonObject1.getInt("SCRIP_CD");
                        System.out.println(companyId);
                        String headline = jsonObject1.getString("NEWSSUB");
                        System.out.println("HeadLine " + " " + headline);
                        String table = jsonObject1.getString("MORE");
                        System.out.println("table " + table);
                        String date = jsonObject1.getString("NEWS_DT");
                        System.out.println("date " + date);
                        String pdf = jsonObject1.getString("ATTACHMENTNAME");
                        String pdfLink;
                        if (pdf.equals(" ")) {
                            pdfLink = table;
                        }
                        pdfLink = "https://www.bseindia.com/corporates/xml-data/corpfiling/AttachLive/" + pdf;
                        System.out.println(pdfLink);
                        System.out.println(formattedDate);
                        System.out.println("-------");
                    }
                }
//                System.out.println("----");

                if (currentMonth == 1 && year == 2020) {
                    currentMonth = 12;
                    year = 2019;
                }
                if (currentMonth == 1 && year == 2019) break;
            }
        } catch (IOException | ParseException ex) {
            System.out.println(ex.getMessage());

        }


    }
}

