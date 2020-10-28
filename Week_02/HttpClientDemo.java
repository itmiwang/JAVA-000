package java0.nio01;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author guozq
 * @date 2020-10-28-08:55 下午
 */
public class HttpClientDemo {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://localhost:8801");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            System.out.println("statusCode:" + response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
