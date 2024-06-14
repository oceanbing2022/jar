package com.ohx.service;

//ip-api.com在其免费版本中有频率限制。如果您的应用程序需要处理大量请求，您可能需要考虑其他服务或付费选项。
import org.springframework.web.client.RestTemplate;
        import org.springframework.web.util.UriComponentsBuilder;
        import java.net.URI;

public class IpLocationService {

    private final RestTemplate restTemplate;

    public IpLocationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getIpLocation(String ip) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://ip-api.com/json")
                .path("/" + ip)
                .queryParam("fields", "status,message,country,city")
                .build()
                .toUri();

        IpApiResponse response = restTemplate.getForObject(uri, IpApiResponse.class);
        if (response != null && "success".equals(response.getStatus())) {
            return response.getCountry() + ", " + response.getCity();
        } else {
            // 处理错误情况
            return "Unknown";
        }
    }

    // 内部类，用于匹配ip-api的响应
    private static class IpApiResponse {
        private String status;
        private String message;
        private String country;
        private String city;

        // Getter and setter for status
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        // Getter and setter for country
        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        // Getter and setter for city
        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        // Getter and setter for message
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}