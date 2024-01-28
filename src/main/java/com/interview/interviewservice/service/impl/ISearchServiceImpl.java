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
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ISearchServiceImpl implements ISearchService {
    @Value("${api.elasticsearch.uri}")
    private String elasticSearchUri;

    @Value("${api.elasticsearch.search}")
    private String elasticSearchSearchPrefix;

    private static final Logger LOGGER = LoggerFactory.getLogger(ISearchServiceImpl.class);

    @Override
    public ResultQuery searchFromQuery(String query, String[] fields, String className, String companyId) throws IOException {
        String body = HelperFunctions.buildMultiIndexMatchBody(query, fields, companyId);
        return executeHttpRequest(body, className);
    }

    /**
     * Fetch resultQuery from elastic engine for the given body
     *
     * @param body String
     * @return ResultQuery
     * @throws IOException IOException
     */
    private ResultQuery executeHttpRequest(String body, String className) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            ResultQuery resultQuery = new ResultQuery();
            HttpPost httpPost = new HttpPost(HelperFunctions.buildSearchUri(elasticSearchUri,
                    className, elasticSearchSearchPrefix));
            httpPost.setHeader(Constant.CONTENT_ACCEPT, Constant.APP_TYPE);
            httpPost.setHeader(Constant.CONTENT_TYPE, Constant.APP_TYPE);

            try {
                httpPost.setEntity(new StringEntity(body, Constant.ENCODING_UTF8));

                HttpResponse response = httpClient.execute(httpPost);

                // Extract response handling to a separate method
                handleHttpResponse(response, resultQuery);
            } catch (IOException | JSONException e) {
                // Log the exception with additional information
                LOGGER.error("Error while connecting to elastic engine for class {}: {}", className, e.getMessage(), e);

                // Set appropriate values in case of an error
                resultQuery.setNumberOfResults(0);
            }

            return resultQuery;
        }
    }

    private void handleHttpResponse(HttpResponse response, ResultQuery resultQuery) throws IOException, JSONException {
        String message = EntityUtils.toString(response.getEntity());
        JSONObject myObject = new JSONObject(message);

        JSONObject hits = myObject.getJSONObject(Constant.HITS);
        int totalHitsValue = hits.getJSONObject(Constant.TOTAL_HITS).getInt("value");

        resultQuery.setElements(totalHitsValue != 0
                ? hits.getJSONArray(Constant.HITS).toString()
                : null);

        resultQuery.setNumberOfResults(totalHitsValue);
        resultQuery.setTimeTook((float) myObject.getInt(Constant.TOOK) / Constant.TO_MS);
    }

}
