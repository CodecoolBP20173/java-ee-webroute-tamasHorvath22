package com.codecool.annotation;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


class MyHandler implements HttpHandler {

    private int basicRouteLength;

    public void handle(HttpExchange exchange) throws IOException {
        Class myClass = Routes.class;
        String route = exchange.getRequestURI().getPath();
        String username = getUsername(route);

        Method method = getMethod(myClass.getMethods(), route);

        try {
            if (method == null) {
                exchange.setAttribute("response", "ERROR: Unknown WebRoute: " + route);
            }
            else if (method.getParameterCount() == 1) {
                String response = (String) method.invoke(myClass.newInstance(), username);
                exchange.setAttribute("response", response);
            } else {
                method.invoke(myClass.newInstance());
                String response = (String) method.invoke(myClass.newInstance());
                exchange.setAttribute("response", response);
                }
        } catch (IllegalAccessException |
                InvocationTargetException |
                IllegalArgumentException |
                InstantiationException |
                NullPointerException |
                ExceptionInInitializerError |
                SecurityException e) {
            e.printStackTrace();
        }

        String response = exchange.getAttribute("response").toString();

        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private Method getMethod(Method[] methodList, String route) {
        for (Method method : methodList) {
            if(method.isAnnotationPresent(WebRoute.class)) {
                if(getBasicRoute(method.getAnnotation(WebRoute.class).path()).equals(getBasicRoute(route))) {
                    return method;
                }
            }
        }
        return null;
    }

    private String getBasicRoute(String route) {
        StringBuilder basicRoute = new StringBuilder();
        basicRoute.append("/");
        for (int i = 1; i < route.length(); i++) {
            if(route.charAt(i) == '/') {
                break;
            }
            basicRoute.append(route.charAt(i));
        }
        this.basicRouteLength = basicRoute.length();
        return basicRoute.toString();
    }

    private String getUsername(String route) {
        getBasicRoute(route);
        StringBuilder username = new StringBuilder();
        for (int i = 1 + basicRouteLength; i < route.length(); i++) {
            username.append(route.charAt(i));
        }
        return username.toString();
    }
}
