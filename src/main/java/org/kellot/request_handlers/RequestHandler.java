package org.kellot.request_handlers;


import org.kellot.response.HttpResponse;

public  interface RequestHandler {
     HttpResponse handleRequest();
}
