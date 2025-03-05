package com.example.api.domain.settings.qbo;

import com.example.api.domain.adminsettings.AdminSettings;
import com.example.api.repositories.AdminSettingRepository;
import com.intuit.oauth2.client.OAuth2PlatformClient;
import com.intuit.oauth2.config.Environment;
import com.intuit.oauth2.config.OAuth2Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@PropertySource(value = "classpath:/application.properties", ignoreResourceNotFound = true)
public class OAuth2PlatformClientFactory {

    OAuth2PlatformClient client;
    OAuth2Config oauth2Config;

    @Autowired
    private AdminSettingRepository adminSettingRepository;


    //    @Autowired
//    org.springframework.core.env.Environment env;
    @Value("${QBO_CLIENT_ID}")
    private String qboClientId;
    @Value("${QBO_SECRET}")
    private String qboSecret;

    @PostConstruct
    public void init() {
        //initialize the config
        oauth2Config = new OAuth2Config.OAuth2ConfigBuilder(qboClientId, qboSecret) //set client id, secret
                .callDiscoveryAPI(Environment.SANDBOX) // call discovery API to populate urls
                .buildConfig();
        //build the client
        client = new OAuth2PlatformClient(oauth2Config);
    }

    public OAuth2PlatformClient getOAuth2PlatformClient() {
        return client;
    }

    public OAuth2Config getOAuth2Config() {
        return oauth2Config;
    }

    public String getPropertyValue(String proppertyName) {
        List<AdminSettings> qboSettings = adminSettingRepository.findByService("QuickBooks");

        // Convert list to a map (key: settingKey, value: settingValue)
        Map<String, String> qboSettingsMap = qboSettings.stream()
                .collect(Collectors.toMap(AdminSettings::getKey_param, AdminSettings::getValue_param));
        return qboSettingsMap.get("redirect_uri");
    }
}
