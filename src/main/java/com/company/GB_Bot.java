package com.company;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class GB_Bot {

    public static void scrapeData() throws IOException {
        String srcUrl = "https://api.londonstockexchange.com/api/v1/components/refresh";
        String USER_AGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Mobile Safari/537.36";
        int totalPages = 83;
        for (int i = 0; i <= totalPages; i++) {
            int j = i + 1;
            String jsonBody = "{\"path\":\"news\",\"parameters\":\"tab%3Dnews-explorer%26period%3Dlastmonth%26headlinetypes%3D%26excludeheadlines%3D%26page%3D5%26tabId%3D58734a12-d97c-40cb-8047-df76e660f23f\",\"components\":[{\"componentId\":\"block_content%3A431d02ac-09b8-40c9-aba6-04a72a4f2e49\",\"parameters\":\"period=lastmonth&headlinetypes=&excludeheadlines=&page=" + i + "&size=100\"}]}";
            Connection.Response response = Jsoup.connect(srcUrl)
                    .userAgent(USER_AGENT)
                    .header("Content-Type", "application/json")
                    .followRedirects(true)
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .header("Accept", "application/json")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("referer", "https://www.londonstockexchange.com/news?tab=news-explorer&period=lastmonth&headlinetypes=&excludeheadlines=&page=" + j)
                    .header("User-Agent", USER_AGENT)
                    .header("origin", "https://www.londonstockexchange.com")
                    .requestBody(jsonBody)
                    .method(Connection.Method.POST)
                    .execute();
            JSONArray announcements = new JSONArray(response.body());
            JSONObject announcementsJSONObject = announcements.getJSONObject(0);
            String name = "waqar";

        }

    }
}

