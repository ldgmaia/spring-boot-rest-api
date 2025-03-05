package com.example.api.domain.settings.qbo;

import com.example.api.domain.adminsettings.AdminSettings;
import com.example.api.repositories.AdminSettingRepository;
import com.intuit.oauth2.client.OAuth2PlatformClient;
import com.intuit.oauth2.config.Environment;
import com.intuit.oauth2.config.OAuth2Config;
import com.intuit.oauth2.config.Scope;
import com.intuit.oauth2.exception.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QboService {

    @Autowired
    private AdminSettingRepository adminSettingRepository;

    @Value("${QBO_CLIENT_ID}")
    private String qboClientId;

    @Value("${QBO_SECRET}")
    private String qboSecret;

    public RedirectView getAuthUri() {

        OAuth2PlatformClient client;
        OAuth2Config oauth2Config;

        List<AdminSettings> qboSettings = adminSettingRepository.findByService("QuickBooks");

        // Convert list to a map (key: settingKey, value: settingValue)
        Map<String, String> qboSettingsMap = qboSettings.stream()
                .collect(Collectors.toMap(AdminSettings::getKey_param, AdminSettings::getValue_param));

        // Now you can access values like:
        String redirectUri = qboSettingsMap.get("redirect_uri");
        String webhookUri = qboSettingsMap.get("webhook_uri");


        //initialize the config
        oauth2Config = new OAuth2Config.OAuth2ConfigBuilder(qboClientId, qboSecret) //set client id, secret
                .callDiscoveryAPI(Environment.SANDBOX) // call discovery API to populate urls
                .buildConfig();
        String csrf = oauth2Config.generateCSRFToken();
        try {
            List<Scope> scopes = new ArrayList<>();
            scopes.add(Scope.Accounting);
            return new RedirectView(oauth2Config.prepareUrl(scopes, redirectUri, csrf), true, true, false);
        } catch (InvalidRequestException e) {

            System.out.println("Exception calling connectToQuickbooks " + e);
            throw new RuntimeException("Exception calling connectToQuickbooks " + e);
        }
    }

    public String handleCallback(QboCallbackRequestDTO data) {


        String htmlMessage = """
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
        return htmlMessage;
    }
}
