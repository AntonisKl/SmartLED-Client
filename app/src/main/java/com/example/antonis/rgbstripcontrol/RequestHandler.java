package com.example.antonis.rgbstripcontrol;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.LinkedHashMap;
import java.util.Map;

public class RequestHandler {

    private interface Endpoint {
        String SET_COLOR = "setColor";
        String RAINBOW_EFFECT = "rainbowEffect";
    }

    private RequestQueue requestQueue;

    RequestHandler(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    StringRequest createHttpRequest(int method /*e.g. Request.Method.GET*/, String baseUrl, String endpoint, Map<String, String> params, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        String url = baseUrl + endpoint;
        if (params == null || params.isEmpty())
            return new StringRequest(method, url, responseListener, errorListener);

        StringBuilder paramsS = new StringBuilder();
        for (Map.Entry<String, String> e : params.entrySet()) {
            if (paramsS.length() > 0) {
                paramsS.append("&");
            }
            String key = e.getKey();
            String value = e.getValue();
            paramsS.append(key).append("=").append(value);
        }

        url += "?" + paramsS.toString();
        return new StringRequest(method, url, responseListener, errorListener);
    }

    void addSetColorHttpRequest(String baseUrl, int[] argb) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("r", Integer.toString(argb[1]));
        params.put("g", Integer.toString(argb[2]));
        params.put("b", Integer.toString(argb[3]));

        requestQueue.add(createHttpRequest(Request.Method.GET, baseUrl, Endpoint.SET_COLOR, params, null, null));
    }

    void addRainbowEffectHttpRequest(String baseUrl, int transitionIntervalMillis) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("transitionIntervalMillis", Integer.toString(transitionIntervalMillis));
        requestQueue.add(createHttpRequest(Request.Method.GET, baseUrl, Endpoint.RAINBOW_EFFECT, params, null, null));
    }
}
