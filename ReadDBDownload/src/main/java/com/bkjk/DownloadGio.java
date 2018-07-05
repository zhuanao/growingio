package com.bkjk;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadGio {

    // 填写自定义项
    private String se;//se 为项目私钥
    private String pub;//pub 为项目公钥
    private String project;//project 为projectid
    private String ai;//ai 为项目id
    private String export_type;//任务类型类型
    private String data_type;//数据类型
    private String date;//数据导出日期
    private Long tm = System.currentTimeMillis();
    private String base = "https://www.growingio.com";
    private String pathHeader;//数据导出路径头
    private String id;//项目配置中的id

    public DownloadGio(String id, String se, String pub, String project, String ai, String export_type, String data_type, String pathHeader, String date) {
        this.id = id;
        this.se = se;
        this.pub = pub;
        this.project = project;
        this.ai = ai;
        this.export_type = export_type;
        this.data_type = data_type;
        this.date = date;
        this.pathHeader = pathHeader;
    }

    //拼接token
    public String authToken(String secret, String project, String ai, Long tm) throws Exception {

        String message = "POST\n/auth/token\nproject=" + project + "&ai=" + ai + "&tm=" + tm;
        //MAC加密算法
        Mac hmac = Mac.getInstance("HmacSHA256");
        hmac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signature = hmac.doFinal(message.getBytes("UTF-8"));
        return Hex.encodeHexString(signature);
    }

    //获取token
    public String getAuth() {
        try {
            return authToken(se, project, ai, tm);
        } catch (Exception e) {
            e.printStackTrace();
            return "hehe";
        }
    }

    /**
     * @param url      文件地址
     * @param dir      存储目录
     * @param fileName 存储文件名
     */
    public void downloadHttpUrl(String url, String dir, String fileName) {
        try {
            URL httpurl = new URL(url);
            if (!dir.endsWith("\\")) {
                dir = dir + "\\";
            }
            File dirfile = new File(dir);
            if (!dirfile.exists()) {
                dirfile.mkdirs();
            }

            FileUtils.copyURLToFile(httpurl, new File(dir + fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取文件名
    public String getFileName(String url) {
//        return url.substring(url.indexOf("part"), url.indexOf("?"));
        return url.substring(url.indexOf("part"), url.indexOf("?")).substring(10,20);
    }

    //下载
    public void downLoad() throws JSONException, IOException {
        String auth = getAuth();
        System.out.println(id+"认证算法计算出来的签名:" + auth);
        System.out.println(id+"当前请求时间戳:" + tm);
        String token = "";

        //通过发送获取Token的POST请求，解析响应报文中的JSON得到Token
        HttpPost httpPost = new HttpPost("https://www.growingio.com/auth/token");
        httpPost.setHeader("X-Client-Id", pub);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("project", project));
        params.add(new BasicNameValuePair("ai", ai));
        params.add(new BasicNameValuePair("tm", "" + tm));
        params.add(new BasicNameValuePair("auth", auth));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Consts.UTF_8);
        httpPost.setEntity(entity);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse resp = null;

        try {
            resp = httpclient.execute(httpPost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpEntity httpEntity = resp.getEntity();

        JSONObject resultJsonObject = null;

        if (httpEntity != null) {
            try {
                InputStream is = httpEntity.getContent();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(isr, 8 * 1024);
                StringBuffer entityStringBuilder = new StringBuffer();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    entityStringBuilder.append(line);
                }
                System.out.println(entityStringBuilder);

                resultJsonObject = new JSONObject(entityStringBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            token = resultJsonObject.getString("code");

        }

        CloseableHttpClient httpclient2 = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(base + "/v2/insights/" + export_type + "/" + data_type + "/" + ai + "/" + date + ".json");  //原始数据导出
        httpGet.setHeader("X-Client-Id", pub);
        httpGet.setHeader("Authorization", token);
        HttpResponse httpResponse = httpclient2.execute(httpGet);
        HttpEntity respEntity = httpResponse.getEntity();
        String object = EntityUtils.toString(respEntity);

        resultJsonObject = new JSONObject(object);
        //输出获取到的包含下载链接的JSON对象
        System.out.println(resultJsonObject);

        JSONArray downloadLinks = resultJsonObject.getJSONArray("downloadLinks");

        String datetmp;

        if (date.length() > 8) {
            datetmp = date.substring(0,8);
        } else {
            datetmp = date;
        }

        if (downloadLinks.length() > 0) {
            //遍历获取到的下载链接
            for (int i = 0; i < downloadLinks.length(); i++) {
                String o = (String) downloadLinks.get(i);
                System.out.println(id+"-url-" + o.toString());
                //根据获取到的URL下载文件
                downloadHttpUrl(o, pathHeader+"\\"+export_type+"\\"+data_type+"\\"+datetmp, "part-"+id+getFileName(o)+date+".csv.gz");
            }
        } else {
            System.out.println(id+"-下载链接为空");
        }
    }

}