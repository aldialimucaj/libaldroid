package al.aldi.andorid.net;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

import static android.util.Log.e;
import static android.util.Log.i;

/**
 * Helper class for sending and receiving http requests.
 *
 * @author Aldi Alimucaj
 */
public class HttpUtils {
    public static final int    SOCKET_TIMEOUT      = 15000;
    public static final int    CONNECTION_TIMEOUT  = 15000;
    public static final String TAG                 = "al.aldi";
    public static final String STATUS_CODE_SUCCESS = "200";

    public static final String LINE_END    = "\r\n";
    public static final String TWO_HYPHENS = "--";
    public static final String BOUNDARY    = "*****";

    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };


    private int socketTimeout     = 0;
    private int connectionTimeout = 0;


    public HttpUtils() {
        this.socketTimeout = SOCKET_TIMEOUT;
        this.connectionTimeout = CONNECTION_TIMEOUT;
    }

    public HttpUtils(int socketTimeout, int connectionTimeout) {
        super();
        this.socketTimeout = socketTimeout;
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * Sends a get request to the following url and returns true if Server
     * responds with successful request. CODE 200
     *
     * @param url    url to be called
     * @param params hashmap with params
     * @return ture if code 200
     */
    public HttpResponse sendPostRequestWithParams(final String url, final HashMap<String, String> params) {

        HttpContext localContext = new BasicHttpContext();
        HttpResponse res = null;

        /* Standard parameters to limit the timeout */
        HttpClient client = getDefaultClient();
        client = ignoreSslClient(client);// TODO: Remove this or make it optional

        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Accept", "application/json"); /* in order to let the server know we accept json */

        /* reading the parameter list and adding it to the entity */
        List<NameValuePair> httpParams = new ArrayList<NameValuePair>();

        for (String key : params.keySet()) {
            String value = params.get(key);
            httpParams.add(new BasicNameValuePair(key, value));
        }

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(httpParams, "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        /* finished adding params */

        /* Executing the call */
        try {
            res = client.execute(httpPost, localContext);
        } catch (ClientProtocolException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            if (null != e.getMessage())
                System.err.println(e.getMessage());
             else
                e.printStackTrace();
        }
        return res;
    }

    /**
     * Returns a client which ignores ssl validation and verification.
     *
     * @param client
     * @return
     */
    private HttpClient ignoreSslClient(HttpClient client) {
        try {
            SSLSocketFactory ssf = new MySSLSocketFactory(null, null);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = client.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 80)); // the default port doesnt seem to have a big effect
            return new DefaultHttpClient(ccm, client.getParams());
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * After sending asynchronously the get request this method waits for
     * the response which it then sends back to the caller.
     *
     * @param url URI to call
     * @return the response from the other peer
     */
    public HttpResponse sendGetRequest(final String url) {
        Callable<HttpResponse> request = new Callable<HttpResponse>() {

            @Override
            public HttpResponse call() {
                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = connectionTimeout;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = socketTimeout;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                HttpClient client = new DefaultHttpClient(httpParameters);

                HttpGet get = new HttpGet(url);
                HttpContext localContext = new BasicHttpContext();
                HttpResponse res = null;
                try {
                    res = client.execute(get, localContext);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(res.getStatusLine());

                return res;
            }
        };

        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future<HttpResponse> future = pool.submit(request);

        HttpResponse success = null;

        try {
            success = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return success;
    }

    /**
     * Sends post request to url. Ignores the response.
     *
     * @param url
     * @return true if response code is 200
     */
    public boolean sendPostRequest(String url) {
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        // The default value is zero, that means the timeout is not used.
        int timeoutConnection = connectionTimeout;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = socketTimeout;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        HttpClient client = new DefaultHttpClient(httpParameters);

        HttpPost get = new HttpPost(url);
        HttpContext localContext = new BasicHttpContext();
        HttpResponse res = null;
        try {
            res = client.execute(get, localContext);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.getStatusLine().getStatusCode() == 200;
    }

    /**
     * returns a client with predefined parameters for timeout and socket timeout.
     *
     * @return
     */
    private DefaultHttpClient getDefaultClient() {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = connectionTimeout;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = socketTimeout;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        return new DefaultHttpClient(httpParameters);
    }

    /**
     * Reads the content out of the entity and returns it as a string.
     *
     * @param entity
     * @return http content
     */
    public String httpEntitiyToString(HttpEntity entity) {
        StringBuilder builder = new StringBuilder();
        InputStream content = null;
        try {
            content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                content.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }

    /**
     * Returns the entity's content as a JSONObject.
     *
     * @param entity
     * @return
     * @throws JSONException
     */
    public JSONObject httpEntitiyToJson(HttpEntity entity) throws JSONException {
        JSONObject jObject = null;
        String jsonStr = httpEntitiyToString(entity);
        jsonStr = StringEscapeUtils.unescapeJava(jsonStr); /* need to excape from \n form */
        int strSize = jsonStr.length();
        jsonStr = jsonStr.substring(1, strSize - 1); /* removing leading quotes */
        jObject = new JSONObject(jsonStr);
        return jObject;
    }

    /**
     * Returns the entity's content as an escaped String.
     *
     * @param entity
     * @return
     */
    public String httpEntitiyToSafeString(HttpEntity entity) {
        String jsonStr = httpEntitiyToString(entity);
        jsonStr = StringEscapeUtils.unescapeJava(jsonStr); /* need to excape from \n form */
        if (jsonStr.startsWith("\"") && jsonStr.endsWith("\"")) {
            int strSize = jsonStr.length();
            jsonStr = jsonStr.substring(1, strSize - 1); /* removing leading quotes */
        }
        return jsonStr;
    }

    /**
     * Upload file from device to destination url
     *
     * @param destinationUrl
     * @param sourceFile
     * @return text response
     */
    public String uploadFile(String destinationUrl, File sourceFile, final HashMap<String, String> params) {
        String fileName = sourceFile.getAbsolutePath();
        String uploadFilePath = sourceFile.getParent();
        String uploadFileName = sourceFile.getName();
        String serverResponseMessage = "{}";

        HttpsURLConnection conn = null;
        DataOutputStream dos = null;


        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        if (!sourceFile.isFile()) {
            e("uploadFile", "Source File not exist :" + uploadFilePath + "" + uploadFileName);
        } else {
            try {
                URL url = new URL(destinationUrl);
                if (url.getProtocol().toLowerCase().equals("https")) {
                    trustAllHosts();
                    HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                    https.setHostnameVerifier(DO_NOT_VERIFY);
                    conn = https;
                }

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);

                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
                conn.setRequestProperty("uploaded_file", fileName);
                conn.setChunkedStreamingMode(bufferSize);


                dos = new DataOutputStream(conn.getOutputStream());

                // writing paramters
                for (String key : params.keySet()) {
                    String value = params.get(key);
                    writeParam(dos, key, value);
                }
                dos.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_END);
                dos.writeBytes("Content-Disposition: form-data; name=" + uploadFileName + ";filename=\"" + uploadFileName + "\"" + LINE_END);
                dos.writeBytes(LINE_END);


                buffer = new byte[bufferSize];
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multi part form data necessary after file data...
                dos.writeBytes(LINE_END);
                dos.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_END);

                // Responses from the server (code and message)
                Scanner s;
                if (conn.getResponseCode() != 200) {
                    s = new Scanner(conn.getErrorStream());
                } else {
                    s = new Scanner(conn.getInputStream());
                }
                s.useDelimiter("\\Z");
                serverResponseMessage = s.next();

                serverResponseMessage = serverResponseMessage.replaceAll("\\\\","");
                serverResponseMessage = serverResponseMessage.substring(1,serverResponseMessage.length()-1);
                i(TAG, "al.aldi.andorid.net.HttpUtils.uploadFile(): " + serverResponseMessage);

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
                conn.disconnect();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } // End else block
        return serverResponseMessage;
    }

    private void writeParam(DataOutputStream dos, String name, String value) throws IOException {
        dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + LINE_END);
        dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + LINE_END);
        dos.writeBytes("Content-Length: " + value.length() + LINE_END);
        dos.writeBytes(LINE_END);
        dos.writeBytes(value + LINE_END);
        dos.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_END);
    }

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
