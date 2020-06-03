package com.atmire.dspace.hal;

import com.atmire.dspace.hal.service.HALStatusService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dspace.core.LogManager;
import org.dspace.services.ConfigurationService;
import org.dspace.services.factory.DSpaceServicesFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jonas - jonas@atmire.com on 30/01/2018.
 */
public class HALStatusServiceImpl implements HALStatusService {

    /* Log4j logger*/
    private static final Logger log =  Logger.getLogger(HALStatusServiceImpl.class);
    private static final String HAL_URL = DSpaceServicesFactory.getInstance().getConfigurationService().getProperty("hal.url");

    @Autowired
    ConfigurationService configurationService;

    public String retrieveHALStatus(String identifier, String version ) {
        String url =identifier;
        if(StringUtils.isNotBlank(version)){
            url+="v"+version;
        }
        HttpGet httpGet = new HttpGet(HAL_URL+url);
        try {
            String user = configurationService.getProperty("hal.login.user");
            String pass = configurationService.getProperty("hal.login.pass");
            String basicAuth = Base64.encodeBase64String((user + ":" + pass).getBytes(StandardCharsets.UTF_8));

            httpGet.setHeader("Authorization", "Basic " + basicAuth);
            HttpResponse getResponse = getHttpClient().execute(httpGet);
            if (getResponse != null && getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String response = IOUtils.toString(getResponse.getEntity().getContent());
                Document document = new SAXReader().read(new StringReader(response));
                if (document != null) {
                    Node node = document.selectSingleNode("//status");
                    if (node != null) {
                        String nodeValue = node.getStringValue();
                        if (StringUtils.isNotBlank(nodeValue)) {
                            return nodeValue;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(LogManager.getHeader(null, "hal_status_retrieve_error", "Identifier: " + identifier + ", version:" + version), e);
            return "error";
        }
        return null;
    }

    private HttpClient getHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
        RequestConfig.Builder requestBuilder = RequestConfig.custom();
        requestBuilder = requestBuilder.setConnectTimeout(10000);
        requestBuilder = requestBuilder.setConnectionRequestTimeout(10000);
        final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setDefaultRequestConfig(requestBuilder.build());
        return httpClientBuilder.build();
    }
}
