package com.sgasecurity.integratoromni;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class EntryPoint {


    private static final String HOST = "192.168.0.9";
    private static final int PORT = 8080;
    private static final String ENDPOINT = "/Invoice/";

    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();
//    String OMNIURL = "http://127.0.0.1:8080/";

    String OMNIURL = "http://192.168.1.38:8080/";
    String U_NAME = "gmbatia";
    String P_WORD = "machine";
    String COMPANY_NAME = "SGA CIT Kenya LTD - TRY";

    ObjectMapper mapper = new ObjectMapper();

    @RequestMapping("/")
    @ResponseBody
    public String doEntryPoint()
    {
        try{



                return "<h1>Hello World</h1>";

        } catch (Exception exx)
        {
            return "<h1>Error: {"+exx.toString()+"}</h1>";
        }

    }

    @RequestMapping("/pushinvoicetoomni/{citinvoiceid}")
    @ResponseBody
    public String doPushInvoice(
            @PathVariable("citinvoiceid") String citinvoiceid)
    {
        try{

            System.out.println("CIT Invoice Number: "+ citinvoiceid);

            String url = OMNIURL.concat("Invoice/");
/*            if(invoiceNumber != null && !invoiceNumber.isEmpty()) {
                url += invoiceNumber;
            }*/
            url += "?UserName=" + U_NAME + "&Password=" + P_WORD + "&CompanyName=" +  URLEncoder.encode(COMPANY_NAME, "UTF-8");

            Random random = new Random();
            String customerAccountCode = "KIMELI01";
            String stockCode = "";
            int qty = 1+ random.nextInt(9);
            String quantity = Integer.toString(qty);

            int slp = 100+ random.nextInt(9999);
            String sellingPrice = Integer.toString(slp);

            String description = "Description: CIT Invoice D.C.M.S Innvoice: ("+citinvoiceid+") - " +quantity + " "+ sellingPrice ;
            System.out.println("\n=============================\n\n");
            System.out.println(description);
            System.out.println("\n=============================\n\n");


            String revenueAccountNumber ="18A101";

            JSONObject invoice = new JSONObject();

            invoice.put("customer_account_code", customerAccountCode);

            JSONArray invoiceLines = new JSONArray();

            JSONObject invoiceLine = new JSONObject();

            invoiceLine.put("stock_code", stockCode);
            invoiceLine.put("quantity", qty);
            invoiceLine.put("selling_price", slp);
            invoiceLine.put("selling_price_per", 1);
            invoiceLine.put("line_type", "Non Stock");
            invoiceLine.put("description", description);
            invoiceLine.put("revenue_acc_code", revenueAccountNumber);

            invoiceLines.put(invoiceLine);

            invoice.put("invoice_lines", invoiceLines);
            JSONObject requestBody = new JSONObject();
            requestBody.put("invoice", invoice);

            System.out.println("REQUEST BODY: "+requestBody.toString());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();
            System.out.println("===========KIMELI CHECK URL===============: "+request.toString());
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            System.out.println("======================OMNI ACCOUNTS RESPONSE===================\n" + responseBody);
            return "<h3>"+responseBody+"</h3>";


        } catch (Exception exx)
        {
            return "<h1>Error: {"+exx.toString()+"}</h1>";
        }

    }


    @PostMapping("/postOmniInvoiceDetails")
    public Object postOmniInvoiceDetails(@RequestBody Map<String, Object> requestBody) {
        try {
            OMNIURL = OMNIURL.concat("Invoice/");
            String requestBodyJson = mapper.writeValueAsString(requestBody);
            HashMap<String, String> requestParamObject = new HashMap<>();
            requestParamObject.put("UserName", U_NAME);
            requestParamObject.put("Password", P_WORD);
            requestParamObject.put("CompanyName", COMPANY_NAME);
            String fullURL = OMNIURL + "?" + mapper.writeValueAsString(requestParamObject);
            System.out.println("OMNI REQUEST FORMAT:\n" + fullURL);
            OmniNetworkListener omniNetworkListener = new OmniNetworkListener();
            String netResponse = omniNetworkListener.performURLExecution(OMNIURL, requestParamObject, "POST", "application/json", requestBodyJson);
            System.out.println("======================OMNI ACCOUNTS RESPONSE===================\n" + netResponse);
            return netResponse;
        } catch (Exception e) {
            System.out.println("\nErrors\n" + e.getMessage().toString() + "\n\n" + Arrays.asList(e).toString());
            return null;
        }
    }

    public String postInvoiceRaw(Map<String, Object> requestBody) {
        try {



            OMNIURL = OMNIURL.concat("Invoice/");
            String requestBodyJson = mapper.writeValueAsString(requestBody);
            HashMap<String, String> requestParamObject = new HashMap<>();
            requestParamObject.put("UserName", U_NAME);
            requestParamObject.put("Password", P_WORD);
            requestParamObject.put("CompanyName", COMPANY_NAME);
            String fullURL = OMNIURL + "?" + mapper.writeValueAsString(requestParamObject);
            System.out.println("OMNI REQUEST FORMAT:\n" + fullURL);
            OmniNetworkListener omniNetworkListener = new OmniNetworkListener();
            String netResponse = omniNetworkListener.performURLExecution(OMNIURL, requestParamObject, "POST", "application/json", requestBodyJson);
            System.out.println("======================OMNI ACCOUNTS RESPONSE===================\n" + netResponse);
            return netResponse;
        } catch (Exception e) {
            System.out.println("\nErrors\n" + e.getMessage().toString() + "\n\n" + Arrays.asList(e).toString());
            return null;
        }
    }

}
