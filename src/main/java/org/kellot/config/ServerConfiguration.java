package org.kellot.config;

public record ServerConfiguration(int port,String rootLocation,String pageLocation,String errorTemplateLocation,int queryStringLength) {}
