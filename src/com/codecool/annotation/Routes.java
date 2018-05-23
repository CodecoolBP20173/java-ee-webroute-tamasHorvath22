package com.codecool.annotation;

import com.sun.net.httpserver.HttpExchange;

public class Routes {

    @WebRoute(method = "GET", path = "/route/<username>")
    public String route(HttpExchange exchange, String username) {
        return "This is the /route route, and the username is: " + username;
    }

    @WebRoute(method="POST", path = "/other")
    public String other(HttpExchange exchange) {
        return "This is another route. Not that one, but another";
    }

    @WebRoute(method = "GET", path = "/")
    public String root(HttpExchange exchange) {
        return  "Root page!";
    }
}
