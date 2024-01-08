package by.andd3dfx.configs

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient

import java.nio.charset.StandardCharsets

class Configuration {
    public static final String host = "localhost"
    static final String serviceUrl = "http://$host:9080"

    public static final RESTClient restClient = new RESTClient(serviceUrl)
}
