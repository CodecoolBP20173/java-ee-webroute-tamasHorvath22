package com.codecool.annotation;

import com.sun.net.httpserver.HttpExchange;

public class Routes {

    @WebRoute(path = "/route/<username>")
    public String route(String username) {
        return "This is the /route route, and the username is: " + username;
    }

    @WebRoute(method="POST", path = "/other")
    public String other() {
        return "This is another route. Not that one, but another";
    }

    @WebRoute()
    public String root() {
        return "Root page!";
    }
}
