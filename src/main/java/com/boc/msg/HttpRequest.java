package com.boc.msg;




import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.boc.db.DBTool1;



public class HttpRequest {
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
	
	public static String msg_url="http://22.122.60.82:80/messages";
	public static String scs_url="http://22.122.20.219/counter/getHistCounterTranInfo";
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line+"\n";
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    
    
    public static BufferedReader sendGet1(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
         
            
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }

       
        
        return in;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
    
    
    
    
    public static String sendPost2(String url, Map m) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("charset", "UTF-8");

            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            System.out.println("request data:"+JSON.toJSONString(m));
             out.append( new String(JSON.toJSONString(m).getBytes("UTF-8")));//强制UTF-8
        //   out.append("{\"Title\": \"\\u7279\\u522b\\u5173\\u6ce8\", \"Content\": \"haha\", \"Tos\": \"1439171\"}");
           //System.out.println()
            //out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
                //System.out.println("line！");
                
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
    
    public static void main(String[] args) throws IOException{
    	/* 
    	String dsl="{\"query\":{\"match\":{\"content\":\"MUREX\"}}}";
    	 String s=HttpRequest.sendPost("http://192.168.254.132:9200/docs/doc/_search",dsl );
    	 Map o=(Map)JSON.parse(s);
    	 Map hits=(Map)o.get("hits");
    	 
    	 Object[] rec_array=(Object[])hits.get("hits");
    	
    //	 System.out.println("object:"+o.get("took"));
        System.out.println(s.substring(0, 500));
        for (int i=0;i< rec_array.length;i++)
        {
         Map rec=(Map)rec_array[i];
         System.out.println("object:"+rec.get("_source").toString().substring(0,500));
        }*/
    	
    /*	
    	String url="http://22.12.95.70/OptiDatabox/DATE/BOCNET/rmeResultSumCaculation.BII.layer1.out?now=1539046645173";
    	
    	url=url.replace("1539046645173", DateTool.getUnixTS()).replace("DATE", DateTool.getPeriod());
    	System.out.println(url);
    	String s=HttpRequest.sendGet(url, "");
    	s=s.replace("\n", "\r\n");
    	BufferedReader sb=new BufferedReader(new StringReader(s));
    	String line=null;
    	while((line=sb.readLine())!=null)
    	{
    		System.out.println(line);
    	}*/
    	//System.out.println(s);
    
    	
//    	Map m=new HashMap();
//    	m.put("Title","特别关注");
//    	m.put("Content","fromjava");
//    	m.put("Tos","1439171");
//    	System.out.println(JSON.toJSONString(m));
//    //	String param="Title=特别关注&&content=haha&&tos=1439171";
    	
    	
    
    	//HttpRequest.sendPost2(HttpRequest.msg_url, m);
    	HttpRequest.sendMsg("20190512");
    	
//    	
//    	Map m=new HashMap();
//    	m.put("tran_type","SCS");
//    	m.put("tran_province","全国");
//    	m.put("tran_grade","minute");
//    	m.put("tran_time", "0#7");
//    	
//    	String s=HttpRequest.sendPost2(scs_url, m);
//    	
//    	//System.out.println(s);
//    	List<Map> o=(List<Map>)JSON.parse(s);
//    	System.out.println(o.get(o.size()-1));
    /*
    	for(Map rec :o) {
    		String pollTime=(String)rec.get("record_time");
    		Integer txn=(Integer)rec.get("txn");
    		String sql="INSERT INTO trans_tbl_scs(appName,pollTime,txnVol,txnType) VALUES ('SCS',STR_TO_DATE('$TIME$','%Y-%m-%d %H:%i:%s'),$TXN$,'all')";
    		sql=sql.replace("$TXN$", txn.toString()).replace("$TIME$", pollTime);
    		System.out.println(sql);
    		//DBTool1.update(sql);
    				
    	
    		
    	
    		
    	}*/
    	System.out.println(new Date());

    	System.out.println("finished");
    	System.out.println(new Date());
    	
    	
    	
    	
    	
    }
    
    public static void sendMsg(String s) {
    	sendMsg("",s);
    }
    
    
    public static void sendMsg(String users,String s) {
    	Map m=new HashMap();
    	m.put("Title","特别关注");
    	m.put("Content",s);
     	//m.put("Tos","1439171");
    	///m.put("Tos","1439171|5223289|2655374|1938010");
    	if(users.equals(""))users="1439171|5223289|2655374|1938010";
    	m.put("Tos",users);
    	
    	//m.put("Tos",getUser("scc"));
    	System.out.println(m);
    	HttpRequest.sendPost2(HttpRequest.msg_url, m);
    }


	
}