<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.net.URL"%>
<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String key = "1h8V5AYzAOz5qhaAEF+34Q==";
//String url = "http://www.dlibrary.go.kr/openapi/call.do";
//String url = "http://www.dlibrary.go.kr/openapi/call.do?dist_key=1h8V5AYzAOz5qhaAEF+34Q==&func_id=3&sw=";
String sw = "아몬드";
request.setAttribute("dist_key", key);
request.setAttribute("func_id", 3);
request.setAttribute("sw", URLEncoder.encode(sw, "UTF-8"));


try {
            
            URL url = new URL("http://www.dlibrary.go.kr/openapi/call.do");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("dist_key", key);
            con.setRequestProperty("func_id", "3");
            con.setRequestProperty("sw", URLEncoder.encode(sw, "UTF-8"));
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer resp = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                resp.append(inputLine);
            }
            br.close();
            System.out.println(resp.toString());
            
   } catch(Exception e) {
   		System.out.println("연결 오류");
   		e.printStackTrace();
   }
 %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>책 검색 테스트</title>

</head>
<body>	

</body>
</html>