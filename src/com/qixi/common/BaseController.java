package com.qixi.common;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-1
 * Time: 上午12:06
 * To change this template use File | Settings | File Templates.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qixi.common.constant.UserConst;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    public Logger log = Logger.getLogger(BaseController.class);

    public <T> T getModel(String data, Class<T> clzz) {
        return new Gson().fromJson(data, clzz);
    }

    protected UserBase defaultUserBase(UserBase u) {
        if (u == null) {
            return new UserBase();
        }
        return u;
    }

    public String setModel(Object testmodel) {

        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd");
        return builder.create().toJson(testmodel);
    }

    public void debug(Object obj) {
        if (log.isDebugEnabled())
            log.debug(obj);
    }

    public void pointError(Object obj) {
        log.error(obj);
    }

    public <X> void debug(String str, Class<X> clazz) {
        Logger log = Logger.getLogger(clazz);
        if (log.isDebugEnabled())
            log.debug(str);
    }

    protected int getInt(Map<String, Object> map, String str) {
        if (null == map) {
            return -1;
        }
        if (null == map.get(str) || "".equals(map.get(str))) {
            return -1;
        } else {
            return Double.valueOf(map.get(str).toString().trim()).intValue();
        }
    }

    protected long getLong(Map<String, Object> map, String str) {
        if (null == map) {
            return -1;
        }
        if (null == map.get(str) || "".equals(map.get(str))) {
            return -1;
        } else {
            return Long.parseLong(String.valueOf(map.get(str)).trim());
        }
    }

    protected Integer getInteger(Map<String, Object> map, String str) {

        if (null == map) {
            return null;
        }
        if (null == map.get(str) || "".equals(map.get(str))) {
            return null;
        } else {
            return Double.valueOf(map.get(str).toString().trim()).intValue();
        }
    }

    protected String getString(Map<String, Object> map, String str) {

        if (null == map) {
            return null;
        }
        if (null == map.get(str) || "".equals(map.get(str))) {
            return null;
        } else {
            return map.get(str).toString();
        }
    }

    public UserBase getUserBase(HttpServletRequest request) {
        UserBase userBase = UserBase.class.cast(request.getSession()
                .getAttribute(UserConst.USER_BASE));
        return userBase;
    }

    public String getData(HttpServletRequest request) {
        return request.getParameter("data");
    }

    public static Map<String, Object> getRequestParameterForMap(
            HttpServletRequest request) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            paramMap.put(name, request.getParameter(name));
        }
        return paramMap;
    }

    protected void successResponse(HttpServletResponse response,
                                   Map<String, Object> resultMap) {
        writeResponse(response, resultMap, ResultEnum.SUCCESS, null);
    }

    protected void failResponse(HttpServletResponse response,
                                String errorMessage) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        writeResponse(response, resultMap, ResultEnum.FAILURE, errorMessage);
    }

    protected void noLoginResponse(HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        writeResponse(response, resultMap, ResultEnum.BAD_IDENTIFIER, "");

    }

    protected String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    protected void writeResponse(HttpServletResponse response,
                                 Map<String, Object> resultMap, ResultEnum result, String message) {
        OutputStream os = null;
        try {
            if (resultMap == null) {
                resultMap = new HashMap<String, Object>();
            }
            resultMap.put("resultCode", result.getCode());
            resultMap.put("resultMessage",
                    message != null ? message : result.getMessage());
            resultMap.put("contextCode", 0);
            resultMap.put("responseMessage", result.getMessage());
            resultMap.put("time", new Date());
            os = response.getOutputStream();
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            os.write(new Gson().toJson(resultMap).getBytes("UTF-8"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    protected void writeJsonpResponse(HttpServletResponse response,
                                      Map<String, Object> resultMap, ResultEnum result,
                                      String jsonpCallback) {
        OutputStream os = null;
        try {
            if (resultMap == null) {
                resultMap = new HashMap<String, Object>();
            }
            resultMap.put("resultCode", result.getCode());
            resultMap.put("resultMessage", result.getMessage());
            resultMap.put("contextCode", 0);
            resultMap.put("responseMessage", result.getMessage());
            resultMap.put("time", new Date());
            os = response.getOutputStream();
            response.setContentType("text/html;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            StringBuilder scriptBuilder = new StringBuilder();
            scriptBuilder
                    .append("<script type='text/javascript' charset='utf-8'>")
                    .append("(window.").append(jsonpCallback).append("||")
                    .append("parent.").append(jsonpCallback).append(")(")
                    .append(new Gson().toJson(resultMap)).append(");")
                    .append("</script>");
            os.write(scriptBuilder.toString().getBytes("UTF-8"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

    }

    public boolean hasTargetCookie(HttpServletRequest request,
                                   String cookieName, String cookieValue) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return false;
        }
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            String value = cookie.getValue();
            if (name.equals(cookieName) && value.equals(cookieValue)) {
                return true;
            }
        }
        return false;

    }
}
