package org.eu.yaesakura.simpleauth.framework.util;

import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Subdivision;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

/**
 * IP工具类
 *
 * @author YaeSakura
 */

@Component
public class IpUtil {

    @Value("${geoip2.account-id}")
    private Integer accountId;
    @Value("${geoip2.license-key}")
    private String licenseKey;

    /**
     * 获取真实IP地址
     * @param request
     * @return IP地址
     */
    public String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * IP地址转地理位置
     * @param ip IP地址
     * @return 地理位置
     */
    public String ip2geo(String ip) {

        WebServiceClient client = new WebServiceClient.Builder(accountId, licenseKey).host("geolite.info").build();

        StringBuilder stringBuilder = new StringBuilder();

        try {
            InetAddress ipAddress = InetAddress.getByName(ip);

            CityResponse cityResponse = client.city(ipAddress);

            // 提取国家信息
            Country country = cityResponse.getCountry();
            String countryName = country.getNames().get("zh-CN");

            stringBuilder.append(countryName).append(" ");

            List<Subdivision> subdivisions = cityResponse.getSubdivisions();
            subdivisions.forEach(subdivision -> {
                String subdivisionName = subdivision.getNames().get("zh-CN");
                stringBuilder.append(subdivisionName).append(" ");
            });

            // 提取城市信息
            City city = cityResponse.getCity();
            String cityName = city.getNames().get("zh-CN");

            stringBuilder.append(cityName).append(" ");

            return stringBuilder.toString();

        } catch (GeoIp2Exception | IOException e) {
            return "未知地点";
        }
    }
}
