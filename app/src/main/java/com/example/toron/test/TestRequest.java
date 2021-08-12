package com.example.toron.test;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TestRequest extends StringRequest {

    // 서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://49.247.195.99/test/test_volley.php"; //호스팅 주소 + php
    private Map<String, String> map;



    public TestRequest(String data, Response.Listener<String> listener) { //문자형태로 보낸다는 뜻
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("User_data", data);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}

