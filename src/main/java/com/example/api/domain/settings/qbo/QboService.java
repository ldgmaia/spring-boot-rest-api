package com.example.api.domain.settings.qbo;

import com.example.api.domain.adminsettings.AdminSettings;
import com.example.api.domain.purchaseorders.PurchaseOrderResponseDTO;
import com.example.api.repositories.AdminSettingRepository;
import com.intuit.oauth2.client.OAuth2PlatformClient;
import com.intuit.oauth2.config.OAuth2Config;
import com.intuit.oauth2.config.Scope;
import com.intuit.oauth2.data.BearerTokenResponse;
import com.intuit.oauth2.exception.InvalidRequestException;
import com.intuit.oauth2.exception.OAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QboService {

    private static final String failureMsg = "Failed";
    @Autowired
    OAuth2PlatformClientFactory factory;
    String urlModule = "/purchaseorder/" + 322;
    @Autowired
    private AdminSettingRepository adminSettingRepository;
    @Value("${API_ENV}")
    private String env;
    String serviceSettingsName = "prod".equalsIgnoreCase(env) ? "QuickBooks" : "QuickBooksSandbox";

    public RedirectView getAuthUri() {

        OAuth2Config oauth2Config = factory.getOAuth2Config();
        String redirectUri = factory.getPropertyValue("redirectUri");
        String csrf = oauth2Config.generateCSRFToken();

        try {
            List<Scope> scopes = new ArrayList<>();
            scopes.add(Scope.Accounting);
            return new RedirectView(oauth2Config.prepareUrl(scopes, redirectUri, csrf), true, true, false);
        } catch (InvalidRequestException e) {
            throw new RuntimeException("Exception calling connectToQuickbooks " + e);
        }
    }

    public String handleCallback(QboCallbackRequestDTO data) {
        try {
            OAuth2PlatformClient client = factory.getOAuth2PlatformClient();
            String redirectUri = factory.getPropertyValue("redirectUri");

            BearerTokenResponse bearerTokenResponse = client.retrieveBearerTokens(data.code(), redirectUri);

            adminSettingRepository.updateValueparamByServiceAndKeyparam(bearerTokenResponse.getRefreshToken(), serviceSettingsName, "refreshToken");
            adminSettingRepository.updateValueparamByServiceAndKeyparam(bearerTokenResponse.getAccessToken(), serviceSettingsName, "accessToken");
            adminSettingRepository.updateValueparamByServiceAndKeyparam(data.realmId(), serviceSettingsName, "realmId");

            return """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>QuickBooks Connection Successful</title>
                        <style>
                            body {
                                font-family: sans-serif;
                                display: flex;
                                justify-content: center;
                                align-items: center;
                                height: 100vh;
                                margin: 0;
                                background-color: #f4f4f4;
                            }
                            .container {
                                text-align: center;
                                padding: 20px;
                                background-color: white;
                                border-radius: 8px;
                                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <p>You are connected to QuickBooks and you can close this window.</p>
                        </div>
                    </body>
                    </html>""";

        } catch (OAuthException e) {
            throw new RuntimeException("Exception calling connectToQuickbooks " + e);
        }
    }

    public <T> T fetchFromQbo(String urlModule, Class<T> responseType) {
        List<AdminSettings> qboSettings = adminSettingRepository.findByService(serviceSettingsName);

        // Convert list to a map (key: settingKey, value: settingValue)
        Map<String, String> qboSettingsMap = qboSettings.stream()
                .collect(Collectors.toMap(AdminSettings::getKey_param, AdminSettings::getValue_param));

        if (qboSettingsMap.get("realmId") == null) {
            throw new RuntimeException("No realm ID.  QBO calls only work if the accounting scope was passed!");
        }

        String accessToken = qboSettingsMap.get("accessToken");
        String refreshToken = qboSettingsMap.get("refreshToken");
        String minorVersion = qboSettingsMap.get("minorVersion");
        String realmId = qboSettingsMap.get("realmId");

        String url = factory.getPropertyValue("qboUrl") + "/v3/company/" + realmId;

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            if (urlModule.contains("/pdf")) {
                headers.set("Accept", "application/pdf");
            } else {
                headers.set("Accept", "application/json");
            }

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<T> purchaseOrder = restTemplate.exchange(url + urlModule + "?minorversion=" + minorVersion, HttpMethod.GET, entity, responseType);
            return purchaseOrder.getBody();
        }
        /*
         * Handle 401 status code -
         * If a 401 response is received, refresh tokens should be used to get a new access token,
         * and the API call should be tried again.
         */ catch (HttpClientErrorException e) {
            OAuth2PlatformClient client = factory.getOAuth2PlatformClient();
            try {
                BearerTokenResponse bearerToken = client.refreshToken(refreshToken);

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + bearerToken.getAccessToken());
                if (urlModule.contains("/pdf")) {
                    headers.set("Accept", "application/pdf");
                } else {
                    headers.set("Accept", "application/json");
                }

                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<T> purchaseOrder = restTemplate.exchange(url + urlModule + "?minorversion=" + minorVersion, HttpMethod.GET, entity, responseType);

                return purchaseOrder.getBody();

            } catch (OAuthException e1) {
                throw new RuntimeException("Error while calling refreshToken :: " + failureMsg + " - " + e1.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while calling executeQuery :: " + e.getMessage());
        }
    }

    public PurchaseOrderResponseDTO getPurchaseOrderInfo(Long purchaseOrderId) {
        List<AdminSettings> qboSettings = adminSettingRepository.findByService(serviceSettingsName);

        // Convert list to a map (key: settingKey, value: settingValue)
        Map<String, String> qboSettingsMap = qboSettings.stream()
                .collect(Collectors.toMap(AdminSettings::getKey_param, AdminSettings::getValue_param));

        if (qboSettingsMap.get("realmId") == null) {
            throw new RuntimeException("No realm ID.  QBO calls only work if the accounting scope was passed!");
//            return new JSONObject().put("response", "No realm ID.  QBO calls only work if the accounting scope was passed!").toString();
        }

        String accessToken = qboSettingsMap.get("accessToken");
        String refreshToken = qboSettingsMap.get("refreshToken");
        String minorVersion = qboSettingsMap.get("minorVersion");
        String realmId = qboSettingsMap.get("realmId");

        String url = factory.getPropertyValue("qboUrl") + "/v3/company/" + realmId;

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<PurchaseOrderResponseDTO> purchaseOrder = restTemplate.exchange(url + "/purchaseorder/" + purchaseOrderId + "?minorversion=" + minorVersion, HttpMethod.GET, entity, PurchaseOrderResponseDTO.class);
            purchaseOrder.getBody().PurchaseOrder().DocNumber();
//            ResponseEntity<String> response = restTemplate.exchange(url + "/query?query=select * from PurchaseOrder where Id = '322'&minorversion=" + minorVersion, HttpMethod.GET, entity, String.class);
            return purchaseOrder.getBody();
        }
        /*
         * Handle 401 status code -
         * If a 401 response is received, refresh tokens should be used to get a new access token,
         * and the API call should be tried again.
         */ catch (HttpClientErrorException e) {
            OAuth2PlatformClient client = factory.getOAuth2PlatformClient();
            try {
                BearerTokenResponse bearerToken = client.refreshToken(refreshToken);

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + bearerToken.getAccessToken());
                headers.set("Accept", "application/json");

                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<PurchaseOrderResponseDTO> purchaseOrder = restTemplate.exchange(url + "/purchaseorder/" + purchaseOrderId + "?minorversion=" + minorVersion, HttpMethod.GET, entity, PurchaseOrderResponseDTO.class);

                return purchaseOrder.getBody();

            } catch (OAuthException e1) {
                throw new RuntimeException("Error while calling refreshToken :: " + failureMsg + " - " + e1.getMessage());
//                return new JSONObject().put("response", failureMsg).toString();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while calling executeQuery :: " + e.getMessage());
        }
    }
}
