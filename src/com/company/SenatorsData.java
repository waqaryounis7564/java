package com.company;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SenatorsData {
    public static void scrapeData() throws IOException {
        try (WebClient webClient = new WebClient(BrowserVersion.FIREFOX_68);
        ) {
            webClient.getOptions().setRedirectEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getCookieManager().setCookiesEnabled(true);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            final HtmlPage page = webClient.getPage("https://efdsearch.senate.gov/search/home/");
            final DomElement ipbtn = page.getElementById("agree_statement");
            final HtmlPage page2 = ipbtn.click();
            DomElement chkbox = page2.querySelector("[value='11']");
            HtmlInput start_date = page2.querySelector("[name='submitted_start_date']");
            HtmlInput end_date = page2.querySelector("[name='submitted_end_date']");
            DomElement searchBtn = page2.querySelector("#searchForm > div > button");
            chkbox.click();
            start_date.click();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            Date month_ago = cal.getTime();
            Format dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String dateFrom = dateFormat.format(month_ago);
            String dateTo = dateFormat.format(new Date());
            start_date.setValueAttribute(dateFrom);
            start_date.click();
            end_date.setValueAttribute(dateTo);
            HtmlPage xhr = searchBtn.click();
            HtmlSelect records = xhr.querySelector("#filedReports_length>label>select");
            records.getOptionByValue("100").setSelected(true);
            webClient.waitForBackgroundJavaScript(10000);
            String result = xhr.asXml();
            Document res = Jsoup.parse(result);
            Elements Report_rows = res.select("tbody>tr");
            for (int row_num = 0; row_num < Report_rows.size(); row_num++) {
                String name = Report_rows.get(row_num).select("td:nth-child(1)").text();
                String[] nameParts = name.split(" ");
                String firstName = "";
                String middleName = "";
                if (nameParts.length > 1) {
                    firstName = nameParts[0];
                    middleName = nameParts[1];
                } else{
                firstName=name;
                }
                String lastName = Report_rows.get(row_num).select("td:nth-child(2)").text();
                //  System.out.println("Report type  " + Report_rows.get(row_num).select("td:nth-child(4)").text());
                String report_Link = Report_rows.get(row_num).select("td:nth-child(4)>a").attr("href");
                String fillingDate =Report_rows.get(row_num).select("td:nth-child(5)").text();
                System.out.println("---------------------------------");

                String reportUrl = "https://efdsearch.senate.gov" + report_Link;
                final HtmlPage page3 = webClient.getPage(reportUrl);
                String to = page3.getWebResponse().getContentAsString();
                Document cv = Jsoup.parse(to);
                if (report_Link.contains("search/view/paper/")) {
                    List<String> links = new ArrayList<>();
                    Elements jpgData = cv.getElementsByClass("img-wrap");
                    for (Element value : jpgData) {
                        String jpg = value.getElementsByTag("img").attr("src");
                        links.add(jpg);
                    }
                    String jpgLinks = links.toString().replace("[", " ").replace("]", " ");
                    System.out.println(jpgLinks);
                    //call db
                  //  writeToDB(firstName, middleName, lastName, fillingDate, null, "periodic", "", "", "", "", "", "", "", "", report_Link, jpgLinks,report_Link);
                } else {
                    Elements rows = cv.body().select("tbody>tr");
                    for (int i = 0; i < rows.size(); i++) {
                        // System.out.println("index " + rows.get(i).select("td:nth-child(1)"));
                        String reportIndex = rows.get(i).select("td:nth-child(1)").text();
                        String transactionDate=rows.get(i).select("td:nth-child(2)").text();
                        String owner= rows.get(i).select("td:nth-child(3)").text();
                        String ticker=rows.get(i).select("td:nth-child(4)").text().replace("--", "");
                        //System.out.println("Asset Name" + rows.get(i).select("td:nth-child(5)").textNodes());
                        ArrayList<TextNode> assetName = (ArrayList<TextNode>) rows.get(i).select("td:nth-child(5)").textNodes();
                      //  System.out.println("Issuer  " + assetName.get(0));
                        String assetType= rows.get(i).select("td:nth-child(6)").text();
                        String type =rows.get(i).select("td:nth-child(7)").text();
                        String amount=rows.get(i).select("td:nth-child(8)").text();
                        String comment=rows.get(i).select("td:nth-child(9)").text().replace("--", "");
                        String rnsIndex= report_Link.substring(1) + reportIndex;
                        String [] fid=report_Link.split("/");
                        System.out.println("fid "+fid[4]);
                        System.out.println(rnsIndex);
                      //  writeToDB(firstName, middleName, lastName, fillingDate, transactionDate, "periodic", owner, ticker, assetName, assetType, type, amount, comment, "USD", report_Link + rnsIndex, "", report_Link);
                        System.out.println("********************************************");
                    }
                }
            }

        }


    }
//    private void writeToDB(String firstName, String middleName, String lastName, String fillingDate, String transactionDate, String reportType, String owner, String ticker, String assetName, String assetType, String type, String amount, String comment, String currency, String rnsID, String jpgLink, String fileIndex) {
//        senatorsRepo.insertSenatorsData(firstName, middleName, lastName, fillingDate, transactionDate, reportType, owner, ticker, assetName, assetType, type, amount, comment, currency, rnsID, jpgLink,fileIndex);
//    }
}

//        HashMap<String, String> data = new HashMap<String, String>();
//        data.put("draw", "1");
//        data.put("columns[0][data]", "0");
//        data.put("columns[0][name]", "");
//        data.put("columns[0][searchable]", "true");
//        data.put("columns[0][orderable]", "true");
//        data.put("columns[0][search][value]", "");
//        data.put("columns[0][search][regex]", "false");
//        data.put("columns[1][data]", "1");
//        data.put("columns[1][name]", "");
//        data.put("columns[1][searchable]", "true");
//        data.put("columns[1][orderable]", "true");
//        data.put("columns[1][search][value]", "");
//        data.put("columns[1][search][regex]", "false");
//        data.put("columns[2][data]", "2");
//        data.put("columns[2][name]", "");
//        data.put("columns[2][searchable]", "true");
//        data.put("columns[2][orderable]", "true");
//        data.put("columns[2][search][value]", "");
//        data.put("columns[2][search][regex]", "false");
//        data.put("columns[3][data]", "3");
//        data.put("columns[3][name]", "");
//        data.put("columns[3][searchable]", "true");
//        data.put("columns[3][orderable]", "true");
//        data.put("columns[3][search][value]", "");
//        data.put("columns[3][search][regex]", "false");
//        data.put("columns[4][data]", "4");
//        data.put("columns[4][name]", "");
//        data.put("columns[4][searchable]", "true");
//        data.put("columns[4][orderable]", "true");
//        data.put("columns[4][search][value]", "");
//        data.put("columns[4][search][regex]", "false");
//        data.put("order[0][column]", "1");
//        data.put("order[0][dir]", "asc");
//        data.put("order[1][dir]", "asc");
//        data.put("start", "0");
//        data.put("length", "25");
//        data.put("search[value]", "");
//        data.put("search[regex]", "false");
//        data.put("report_types", "[11]");
//        data.put("filer_types", "[]");
//        data.put("submitted_start_date", "06/06/2020 00:00:00");
//        data.put("submitted_end_date", "06/08/2020 23:59:59");
//        data.put("candidate_state", "");
//        data.put("senator_state", "");
//        data.put("office_id", "");
//        data.put("first_name", "");
//        data.put("last_name", "");
//        try {
//            Document doc = Jsoup
//                    .connect("https://efdsearch.senate.gov/search/home/")
//                    .userAgent(userAgent)
//                    .get();
//
//            String token = doc.select("input[name=csrfmiddlewaretoken]").val();
//            Connection.Response res = Jsoup
//                    .connect("https://efdsearch.senate.gov/search/home/")
//                    .userAgent(userAgent)
//                    .followRedirects(true)
//                    .ignoreHttpErrors(true)
//                    .ignoreContentType(true)
//                    .method(Connection.Method.GET).execute();
//            Map<String, String> cookies = res.cookies();
//
//
//
//            Connection.Response response = Jsoup.connect("https://efdsearch.senate.gov/search/report/data/")
//                    .userAgent(userAgent)
//                    .followRedirects(true)
//                    .ignoreHttpErrors(true)
//                    .ignoreContentType(true)
//                    .header("Accept", "application/json")
//                    .header("Accept-Language", "en-US,en;q=0.9")
//                    .header("Referer", "https://efdsearch.senate.gov/search/home/")
//                    .header("Host", "efdsearch.senate.gov")
//                    .header("X-CSRFToken", token)
//                    .header("X-Requested-With", "XMLHttpRequest")
//                    .header("Origin", "https://efdsearch.senate.gov")
//                    .followRedirects(false)
//                    .sslSocketFactory(socketFactory())
//                    .data(data)
//                 .cookies(cookies)
//                    .method(Connection.Method.POST).execute();
//
//
//            System.out.println(response.body());
//
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
//        }


//   DomElement nexBtn = xhr.querySelector("a#filedReports_next.paginate_button.next");
//                    HtmlPage np = nexBtn.click();
//                    webClient.waitForBackgroundJavaScript(10000);
//                    String re = np.asXml();
//                    if (nexBtn.hasAttribute("a#filedReports_next.paginate_button.disabled")) {
//                        System.out.println("next button is disable");
//                    } else {
//                        Document response = Jsoup.parse(re);
//                        System.out.println(response.select("tbody>tr"));
//                        System.out.println("next is enable");
//  }