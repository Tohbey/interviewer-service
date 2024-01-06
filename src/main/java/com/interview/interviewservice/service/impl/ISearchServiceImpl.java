package com.interview.interviewservice.service.impl;

import com.interview.interviewservice.Util.Constant;
import com.interview.interviewservice.Util.HelperFunctions;
import com.interview.interviewservice.Util.ResultQuery;
import com.interview.interviewservice.service.ISearchService;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ISearchServiceImpl implements ISearchService {
    @Value("${api.elasticsearch.uri}")
    private String elasticSearchUri;

    @Value("${api.elasticsearch.search}")
    private String elasticSearchSearchPrefix;

    private static final Logger LOGGER = LoggerFactory.getLogger(ISearchServiceImpl.class);

    @Override
    public ResultQuery searchFromQuery(String query, String[] fields) throws IOException {
        String body = HelperFunctions.buildMultiIndexMatchBody(query, fields);
        return executeHttpRequest(body);
    }

    /**
     * Fetch resultQuery from elastic engine for the given body
     *
     * @param body String
     * @return ResultQuery
     * @throws IOException IOException
     */
    private ResultQuery executeHttpRequest(String body) throws IOException{
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            ResultQuery resultQuery = new ResultQuery();
            HttpPost httpPost = new HttpPost(HelperFunctions.buildSearchUri(elasticSearchUri
                    , "", elasticSearchSearchPrefix));
            httpPost.setHeader(Constant.CONTENT_ACCEPT, Constant.APP_TYPE);
            httpPost.setHeader(Constant.CONTENT_TYPE, Constant.APP_TYPE);
            try {
                httpPost.setEntity(new StringEntity(body, Constant.ENCODING_UTF8));
                HttpResponse response = httpClient.execute(httpPost);
                String message = EntityUtils.toString(response.getEntity());
                JSONObject myObject = new JSONObject(message);
                if(myObject.getJSONObject(Constant.HITS)
                        .getInt(Constant.TOTAL_HITS) != 0){
                    resultQuery
                            .setElements(myObject
                                    .getJSONObject(Constant.HITS)
                                    .getJSONArray(Constant.HITS)
                                    .toString());
                    resultQuery
                            .setNumberOfResults(myObject.getJSONObject(Constant.HITS)
                                    .getInt(Constant.TOTAL_HITS));
                    resultQuery.setTimeTook((float) ((double) myObject.getInt(Constant.TOOK) / Constant.TO_MS));
                } else {
                    resultQuery.setElements(null);
                    resultQuery.setNumberOfResults(0);
                    resultQuery.setTimeTook((float) ((double) myObject.getInt(Constant.TOOK) / Constant.TO_MS));
                }
            } catch (IOException | JSONException e) {
                LOGGER.error("Error while connecting to elastic engine --> {}", e.getMessage());
                resultQuery.setNumberOfResults(0);
            }

            return resultQuery;
        }
    }
}
