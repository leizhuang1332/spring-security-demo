package com.lz.security.util;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpUtils {
    private static final String CTYPE_FORM = "application/x-www-form-urlencoded;charset=utf-8";
    private static final String CTYPE_JSON = "application/json; charset=utf-8";
    private static final String charset = "utf-8";

    private static HttpUtils instance = null;

    public static HttpUtils getInstance() {
        if (instance == null) {
            return new HttpUtils();
        }
        return instance;
    }


    public static void main(String[] args) throws SocketTimeoutException, IOException {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOnsibG9naW5UeXBlIjoiZm9ybUxvZ2luIiwiY3JlZGVudGlhbHMiOiJ3YW5nd3UiLCJyb2xlcyI6WyJ2aXAyIiwidmlwMyJdfSwianRpIjoiWXpObE1qVmlOR1l0TVRJMU15MDBOelptTFRsa05XRXRZV0kyTVdSak1UTmxNakZoIiwiZXhwIjoxNTk2NjA4NTA5fQ.YeDBZVQmtmnknqtP9gRE0Npaa8TTQD964g41pyQgq2WxKXHVYbKLQLpgcSi3v1n1a4gvXOfAQPxBc3LxisS2bn21PUbV9gu7ffiYzEzjEK-PfaYDoUci7b36uYsP1635XiazoBNYVZwAquRYtvjNYjuAFy3i7HAeIvjHANLAKMHmEFm1-DnBaorRgRO194pq0clqJk2cFCiLEeNvQyloIq_FNI0X269xrXRujSVjCMue6mVhDZPNkO05owbQ29SPPC1HsdwZdeosZ5lfCK7z6Xu_-36zJViF4tgyNT-cXZu37BCdDSTC2MP1NHt9eOFpEwOmdgAyXl2jCx2MyRlkrA");
        String form = getInstance().getForm("http://localhost:8080/curriculum/list", null, headerMap);
        System.out.println(form);
    }

    private class DefaultTrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
    }

    /**
     * 以application/json; charset=utf-8方式传输
     *
     * @param url
     * @return
     * @throws SocketTimeoutException
     * @throws IOException
     */
    public String postJson(String url, String jsonContent)
            throws SocketTimeoutException, IOException {
        return doRequest("POST", url, jsonContent, 15000, 15000, CTYPE_JSON,
                null);
    }

    /**
     * POST 以application/x-www-form-urlencoded;charset=utf-8方式传输
     *
     * @param url
     * @return
     * @throws SocketTimeoutException
     * @throws IOException
     */
    public String postForm(String url) throws SocketTimeoutException,
            IOException {
        return doRequest("POST", url, "", 15000, 15000, CTYPE_FORM, null);
    }

    /**
     * POST 以application/x-www-form-urlencoded;charset=utf-8方式传输
     *
     * @param url
     * @return
     * @throws SocketTimeoutException
     * @throws IOException
     */
    public String postForm(String url, Map<String, String> params)
            throws SocketTimeoutException, IOException {
        return doRequest("POST", url, buildQuery(params), 15000, 15000,
                CTYPE_FORM, null);
    }

    /**
     * POST 以application/x-www-form-urlencoded;charset=utf-8方式传输
     *
     * @param url
     * @return
     * @throws SocketTimeoutException
     * @throws IOException
     */
    public String getForm(String url) throws SocketTimeoutException,
            IOException {
        return doRequest("GET", url, "", 15000, 15000, CTYPE_FORM, null);
    }

    /**
     * POST 以application/x-www-form-urlencoded;charset=utf-8方式传输
     *
     * @param url
     * @return
     * @throws SocketTimeoutException
     * @throws IOException
     */
    public String getForm(String url, Map<String, String> params)
            throws SocketTimeoutException, IOException {
        return doRequest("GET", url, buildQuery(params), 15000, 15000,
                CTYPE_FORM, null);
    }

    public String getForm(String url, Map<String, String> params, Map<String, String> headerMap)
            throws SocketTimeoutException, IOException {
        return doRequest("GET", url, buildQuery(params), 15000, 15000,
                CTYPE_FORM, headerMap);
    }

    /**
     * <p>@Description: </p>
     *
     * @param method         请求的method post/get
     * @param url            请求url
     * @param requestContent 请求参数
     * @param connectTimeout 请求超时
     * @param readTimeout    响应超时
     * @param ctype          请求格式  xml/json等等
     * @param headerMap      请求header中要封装的参数
     * @return
     * @throws SocketTimeoutException
     * @throws IOException
     * @Title doRequest
     * @author zhouyy
     * @date: 2019年10月14日 下午3:47:35
     */
    private String doRequest(String method, String url, String requestContent,
                             int connectTimeout, int readTimeout, String ctype,
                             Map<String, String> headerMap) throws SocketTimeoutException,
            IOException {
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp;
        try {
            conn = getConnection(new URL(url), method, ctype, headerMap);
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);

            if (requestContent != null && requestContent.trim().length() > 0) {
                out = conn.getOutputStream();
                out.write(requestContent.getBytes(charset));
            }

            rsp = getResponseAsString(conn);
        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }

    private HttpURLConnection getConnection(URL url, String method,
                                            String ctype, Map<String, String> headerMap) throws IOException {
        HttpURLConnection conn;
        if ("https".equals(url.getProtocol())) {
            SSLContext ctx;
            try {
                ctx = SSLContext.getInstance("TLS");
                ctx.init(new KeyManager[0],
                        new TrustManager[]{new DefaultTrustManager()},
                        new SecureRandom());
            } catch (Exception e) {
                throw new IOException(e);
            }
            HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection();
            connHttps.setSSLSocketFactory(ctx.getSocketFactory());
            connHttps.setHostnameVerifier((hostname, session) -> true);
            conn = connHttps;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept",
                "text/xml,text/javascript,text/html,application/json");
        conn.setRequestProperty("Content-Type", ctype);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        return conn;
    }

    private String getResponseAsString(HttpURLConnection conn)
            throws IOException {
        InputStream es = conn.getErrorStream();
        if (es == null) {
            return getStreamAsString(conn.getInputStream(), charset, conn);
        } else {
            String msg = getStreamAsString(es, charset, conn);
            if (msg.trim().length() > 0) {
                throw new IOException(conn.getResponseCode() + ":"
                        + conn.getResponseMessage());
            } else {
                return msg;
            }
        }
    }

    private String getStreamAsString(InputStream stream, String charset,
                                     HttpURLConnection conn) throws IOException {
        try {
            Reader reader = new InputStreamReader(stream, charset);

            StringBuilder response = new StringBuilder();
            final char[] buff = new char[1024];
            int read;
            while ((read = reader.read(buff)) > 0) {
                response.append(buff, 0, read);
            }

            return response.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private String buildQuery(Map<String, String> params) throws IOException {
        if (params == null || params.isEmpty()) {
            return "";
        }

        StringBuilder query = new StringBuilder();
        Set<Map.Entry<String, String>> entries = params.entrySet();
        boolean hasParam = false;

        for (Map.Entry<String, String> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue();
            if (hasParam) {
                query.append("&");
            } else {
                hasParam = true;
            }
            query.append(name).append("=")
                    .append(URLEncoder.encode(value, charset));
        }
        return query.toString();
    }
}
