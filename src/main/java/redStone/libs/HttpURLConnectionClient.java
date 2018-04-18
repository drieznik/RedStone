package redStone.libs;

import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.json.JSONArray;
import org.json.JSONObject;
import redStone.BaseTest;
import redStone.libs.reports.BaseReport;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


public class HttpURLConnectionClient {
    enum RequestMethods {GET, POST, PUT, PATCH, DELETE}

    private ArrayList<String> requestParametersList;

    Properties properties = new Properties();
    private String reportsList = "";
    private String assetsList = "";
    private String requestsList = "";
    private String baseUrl;
    private Integer[] assetsArray;
    private String cookie;
    HashMap<String, String> requestMap = new HashMap<>();

    private ArrayList<String> requestsArrayList = new ArrayList<>();

    public ArrayList<BaseReport> getReportsArray() {
        return reportsArray;
    }

    private ArrayList<BaseReport> reportsArray = new ArrayList<>();


    public void initialize() {
        try {
            properties.load(BaseTest.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException i) {
        }

        baseUrl = properties.getProperty("base.url");
    }

    //Login to the system and setting cookie parameter
    public void loginToRedStone() throws IOException {
        HttpURLConnection con = null;
        try {
            initialize();
            URL myPostUrl = new URL(baseUrl + "/Account/Index");
            con = (HttpURLConnection) myPostUrl.openConnection();
            con.setRequestMethod(RequestMethods.POST.name());
            con.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            con.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            con.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");

            String body = "UserName=admin@wolfgang.com&Password=k9Dq7WGtLK&RememberMe=false";

            byte[] outputInBytes = body.getBytes("UTF-8");
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(outputInBytes);
            wr.close();
            con.setInstanceFollowRedirects(false);
            cookie = con.getHeaderField("set-cookie");
        } finally {
            if (con != null)
                con.disconnect();
        }
    }

    //Rest that retrieves report list
    public void getReportsList() throws IOException {
        HttpURLConnection con = null;
        try {
            URL myPostUrl = new URL(baseUrl + "/api/v1/ReportFilter/Reports");
            con = (HttpURLConnection) myPostUrl.openConnection();
            con.setRequestMethod(RequestMethods.GET.name());
            con.setRequestProperty("Cookie", cookie);
            System.out.println("Response is: " + con.getResponseCode() + ", " + con.getResponseMessage());
            reportsList = readInputStreamToString(con);
            System.out.println("Object is: " + reportsList);
            parseReports(reportsList);
        } finally {
            if (con != null)
                con.disconnect();
        }

    }

    /* Rest that retrieves assets list */
    public void getAssetsList() throws IOException {
        HttpURLConnection con = null;
        try {
            URL myPostUrl = new URL(baseUrl + "/api/v1/Asset/Assets");

            con = (HttpURLConnection) myPostUrl.openConnection();

            con.setRequestMethod(RequestMethods.GET.name());
            con.setRequestProperty("Cookie", cookie);
            System.out.println("Response is: " + con.getResponseCode() + ", " + con.getResponseMessage());
            assetsList = readInputStreamToString(con);
            System.out.println("Assets are: " + assetsList);
            assetsArray = parseListToArray(assetsList, "id");
        } finally {
            if (con != null)
                con.disconnect();
        }
    }

    /* Rest that gets list of requests by report id */
    private void populateReportRequests(BaseReport report) throws IOException {
        HttpURLConnection con = null;
        try {
            URL myPostUrl = new URL(baseUrl + "/api/v1/ReportFilter/ReportFilters?reportId=" + String.valueOf(report.getReportId()));
            con = (HttpURLConnection) myPostUrl.openConnection();
            con.setRequestMethod(RequestMethods.GET.name());
            con.setRequestProperty("Cookie", cookie);
            System.out.println("Response is: " + con.getResponseCode() + ", " + con.getResponseMessage());
            requestsList = null;
            requestsList = readInputStreamToString(con);
            System.out.println("Requests are: " + requestsList);
            parseRequestsToArray(requestsList, report);
        } finally {
            if (con != null) {
                con.disconnect();

            }
        }
    }

    /* Rest that generates report */
    public int generateReport(BaseReport report) throws IOException {
        populateReportRequests(report);
        String s = makeQueryParams(report);
        HttpURLConnection con = null;
        try {
            URL myPostUrl = new URL(baseUrl + "/api/v1/Report/" + report.getRouteName()+ "?" + s);
            System.out.println(myPostUrl);
            con = (HttpURLConnection) myPostUrl.openConnection();
            con.setRequestMethod(RequestMethods.GET.name());
            con.setRequestProperty("Cookie", cookie);
            System.out.println("Response is: " + con.getResponseCode() + ", " + con.getResponseMessage());
            System.out.println("Report is: " + report.getRouteName());
        } finally {
            if (con != null)
                con.disconnect();
            report.setRequests(null);
        }
        report.setRequests(null);
        return con.getResponseCode();
    }

    public int generatePArticularReport() throws IOException {
        BaseReport reportToGenerate = new BaseReport();
        reportToGenerate.setReportId(42);
        reportToGenerate.setRouteName("ActualProjectedCCDate");
        populateReportRequests(reportToGenerate);
        String s = makeQueryParams(reportToGenerate);
        HttpURLConnection con = null;
        try {
            URL myPostUrl = new URL(baseUrl + "/api/v1/Report/ActualProjectedCCDate?" + s);
            System.out.println(myPostUrl);
            con = (HttpURLConnection) myPostUrl.openConnection();
            con.setRequestMethod(RequestMethods.GET.name());
            con.setRequestProperty("Cookie", cookie);
            System.out.println("Response is: " + con.getResponseCode() + ", " + con.getResponseMessage());
            System.out.println("Report is: " + reportToGenerate.getRouteName());
        } finally {
            if (con != null)
                con.disconnect();
            reportToGenerate.setRequests(null);
        }
        //report.setRequests(null);
        return con.getResponseCode();
    }

    private String makeQueryParams(BaseReport report){

        StringBuilder str = new StringBuilder();
        Set set = report.getRequests().entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()){
            Map.Entry mentry = (Map.Entry)iterator.next();
            str.append(mentry.getKey() + "=" + String.valueOf(mentry.getValue())
                    .replace("[", "")
                    .replace("]", "")
                    .replace(" ", "") + "&");
        }
        return str.toString();
    }

    /* Method that reads input string */
    private String readInputStreamToString(HttpURLConnection connection) {
        String result = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;

        try {
            is = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();

        } catch (Exception e) {
            result = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {

                }
            }
        }
        return result;
    }

    /* Method that generates  Integer array from string*/
    private Integer[] parseListToArray(String stringToParse, String keyToParseBy) {
        JSONArray ar = new JSONArray(stringToParse);
        JSONObject obj;
        Integer[] itemsArray = new Integer[ar.length()];
        for (int i = 0; i < ar.length(); i++) {
            obj = ar.getJSONObject(i);
            itemsArray[i] = obj.getInt(keyToParseBy);
        }
        return itemsArray;
    }


    /* Method that populates ArrayList from requests list */
    private void parseRequestsToArray(String stringToParse, BaseReport report) {
        requestsArrayList.clear();
        JSONArray ar = new JSONArray(stringToParse);
        HashMap<String, String> map = new HashMap<>();

        for (int i = 0; i < ar.length(); i++) {
            requestsArrayList.add(ar.getJSONObject(i).getString("name"));
        }
        mapRequests(requestsArrayList, report);
    }

    private void mapRequests(ArrayList<String> parameters, BaseReport report) {
        requestMap.clear();
       for(String s: parameters)
            switch (s) {
                case "assets":

                    StringBuilder str = new StringBuilder();

                    str.append(assetsArray[0].toString() + "&");
                    for (int i = 1; i <assetsArray.length -2; i++) {
                        str.append("assets=" + assetsArray[i].toString() + "&");
                    }
                    str.append("assets=" + assetsArray[assetsArray.length -1]);
                    requestMap.put(s, str.toString());
                    break;
                case "reportingYear":
                    requestMap.put(s, "2017");
                    break;
                case "source":
                    requestMap.put(s, "1");
                    break;
                case "quarter":
                    requestMap.put(s, "1");
                    break;
                case "fundId":
                    requestMap.put(s, "24");
                    break;
                case "endDate":
                    requestMap.put(s, "12/31/2017");
                    break;
                case "singleAsset":
                    requestMap.put("assets", "67");
                    break;
                case "funds":
                    requestMap.put("funds", "1,2");
                    break;
                case "startDate":
                    requestMap.put(s, "01/01/2010");
                    break;
                case "fundInvestorLE":
                    requestMap.put(s, "129");
                    break;
                case "globalPartyTypes":
                    requestMap.put(s, "6");
                    break;
                case "archivedParties":
                    requestMap.put(s, "true");
                    break;
                case "singleFund":
                    requestMap.put("funds", "24");
                    break;

            }
            report.setRequests(requestMap);
    }


    private void parseReports(String toParse) {
        JSONArray ar = new JSONArray(toParse);
        for (int i = 0; i < ar.length(); i++) {
            BaseReport reportTemp = new BaseReport();
            reportTemp.setReportId(ar.getJSONObject(i).getInt("reportId"));

            reportTemp.setRouteName(ar.getJSONObject(i).getString("routePrefix"));
            reportsArray.add(reportTemp);
        }
    }


}
