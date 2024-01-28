package com.interview.interviewservice.Util;

import org.json.JSONArray;

import java.util.Arrays;

public class HelperFunctions {

    public static String buildMultiIndexMatchBody(String query, String[] fields, String companyId) {
        return "{\n" +
                "  \"query\": {\n" +
                "    \"bool\" : {\n" +
                "       \"must\" : [\n" +
                "          {\n" +
                "             \"query_string\" : {\n" +
                "                \"query\": \"*" + query + "*\",\n" +
                "                \"fields\":" + new JSONArray(Arrays.asList(fields)) + ",\n" +
                "                \"default_operator\": \"OR\"\n" +
                "             }\n" +
                "           },\n" +
                "          {\n" +
                "             \"term\" : " +
                "             {\n" +
                "                \"companyId\": "+ companyId + "\n" +
                "             }\n" +
                "           }\n" +
                "       ]\n" +
                "   }\n" +
                " }\n" +
                "}";
    }

    public static String buildSearchUri(String elasticSearchUri,
                                        String elasticSearchIndex,
                                        String elasticSearchSearch) {
        return elasticSearchUri + elasticSearchIndex + elasticSearchSearch;
    }
}
