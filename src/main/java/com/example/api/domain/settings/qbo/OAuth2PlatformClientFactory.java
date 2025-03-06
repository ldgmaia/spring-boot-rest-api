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

    @Value("${QBO_CLIENT_ID}")
    private String qboClientId;
    @Value("${QBO_SECRET}")
    private String qboSecret;
    @Value("${QBO_SANDBOX_CLIENT_ID}")
    private String qboSandboxClientId;
    @Value("${QBO_SANDBOX_SECRET}")
    private String qboSandboxSecret;

    @Value("${API_ENV}")
    private String env;


    @PostConstruct
    public void init() {
        String clientId = "prod".equalsIgnoreCase(env) ? qboClientId : qboSandboxClientId;
        String clientSecret = "prod".equalsIgnoreCase(env) ? qboSecret : qboSandboxSecret;

        oauth2Config = new OAuth2Config.OAuth2ConfigBuilder(clientId, clientSecret) //set client id, secret
                .callDiscoveryAPI(Environment.SANDBOX) // call discovery API to populate urls
                .buildConfig();

        client = new OAuth2PlatformClient(oauth2Config);
    }

    public OAuth2PlatformClient getOAuth2PlatformClient() {
        return client;
    }

    public OAuth2Config getOAuth2Config() {
        return oauth2Config;
    }

    public String getPropertyValue(String proppertyName) {
        String serviceSettingsName = "prod".equalsIgnoreCase(env) ? "QuickBooks" : "QuickBooksSandbox";
        List<AdminSettings> qboSettings = adminSettingRepository.findByService(serviceSettingsName);

        // Convert list to a map (key: settingKey, value: settingValue)
        Map<String, String> qboSettingsMap = qboSettings.stream()
                .collect(Collectors.toMap(AdminSettings::getKey_param, AdminSettings::getValue_param));
        return qboSettingsMap.get(proppertyName);
    }
}
