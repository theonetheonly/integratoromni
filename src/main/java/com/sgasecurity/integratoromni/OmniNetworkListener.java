package com.sgasecurity.integratoromni;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class OmniNetworkListener implements Serializable {
    private static final long serialVersionUID =1L;
    public OmniNetworkListener()
    {

    }
    public String executeURL(String urlString , HashMap<String,String> params, String method,
                             String contentType) throws IOException {
        try {

            if (method.length() <= 0)
            {
                method = "GET";
            }
            if (contentType.length() <=0)
            {
                contentType = "application/x-www-form-urlencoded";
            }
            StringBuilder postData = new StringBuilder();
            StringBuilder postDataNonUTF = new StringBuilder();
            for (HashMap.Entry<String, String> param : params.entrySet()) {
                System.out.println("&&&&&&&&&&&\n"+param.toString());
                if (postData.length() != 0) postData.append('&');
                if (postData.length() != 0) postDataNonUTF.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postDataNonUTF.append(param.getKey());
                postData.append('=');
                postDataNonUTF.append('=');
                postData.append(URLEncoder.encode(String.valueOf(correctSearchSpec(param.getValue())), "UTF-8"));
                postDataNonUTF.append(correctSearchSpec(param.getValue()));
            }
            if (method.equals("GET"))
            {
                urlString = correctSearchSpec(urlString+"?"+postData);
            }
            URL url = new URL(correctSearchSpec(urlString));
            System.out.println("*****************************CorrectedFinalSearch Url:==================\n"+correctSearchSpec(urlString)+"\n");
            System.out.println(urlString);
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", contentType);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            if(method.equals("POST")) {
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                conn.getOutputStream().write(postDataBytes);
            }
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0; )
                sb.append((char) c);
            String response = sb.toString();
            return response;
        }
        catch (Exception ex2)
        {
            return ex2.getMessage();
        }
    }

    public String performURLExecution(String urlString, HashMap<String, String> params, String method,
                             String contentType, String requestBody) throws IOException {
        try {
            if (method.length() <= 0) {
                method = "GET";
            }
            if (contentType.length() <= 0) {
                contentType = "application/x-www-form-urlencoded";
            }
            StringBuilder postData = new StringBuilder();
            StringBuilder postDataNonUTF = new StringBuilder();
            for (HashMap.Entry<String, String> param : params.entrySet()) {
                System.out.println("&&&&&&&&&&&\n" + param.toString());
                if (postData.length() != 0) postData.append('&');
                if (postData.length() != 0) postDataNonUTF.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postDataNonUTF.append(param.getKey());
                postData.append('=');
                postDataNonUTF.append('=');
                postData.append(URLEncoder.encode(String.valueOf(correctSearchSpec(param.getValue())), "UTF-8"));
                postDataNonUTF.append(correctSearchSpec(param.getValue()));
            }
            if (method.equals("GET")) {
                urlString = correctSearchSpec(urlString + "?" + postData);
            }
            URL url = new URL(correctSearchSpec(urlString));
            System.out.println("*****************************CorrectedFinalSearch Url:==================\n" + correctSearchSpec(urlString) + "\n");
            System.out.println(urlString);
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", contentType);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            if (method.equals("POST")) {
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                if (requestBody != null) {
                    byte[] requestBodyBytes = requestBody.getBytes("UTF-8");
                    conn.getOutputStream().write(requestBodyBytes);
                } else {
                    conn.getOutputStream().write(postDataBytes);
                }
            }
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0; )
                sb.append((char) c);
            String response = sb.toString();
            return response;
        } catch (Exception ex2) {
            return ex2.getMessage();
        }
    }


    public String correctSearchSpec(String inputString) {
        String outputString = inputString.replace("\"{\\", "{");
        outputString = outputString.replace("\\", "");
        outputString = outputString.replace("}\"", "}");
        System.out.println("-------------------CLEANED STRING: ------------------           "+outputString);
        return outputString;
    }
}
