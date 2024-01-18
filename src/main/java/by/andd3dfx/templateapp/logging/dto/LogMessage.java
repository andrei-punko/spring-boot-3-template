package by.andd3dfx.templateapp.logging.dto;

import lombok.Data;

@Data
public class LogMessage {

    private int httpStatus;
    private String httpMethod;
    private String path;
    private String clientIp;
    private String javaMethod;
    private String response;

    @Override
    public String toString() {
        return httpStatus + " " +
                httpMethod + " " +
                "'" + path + '\'' +
                ", clientIp='" + clientIp + '\'' +
                //", javaMethod='" + javaMethod + '\'' +
                ", response='" + response + '\'';
    }
}
