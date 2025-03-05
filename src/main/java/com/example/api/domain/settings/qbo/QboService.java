package com.example.api.domain.settings.qbo;

import com.intuit.oauth2.client.OAuth2PlatformClient;
import com.intuit.oauth2.config.Environment;
import com.intuit.oauth2.config.OAuth2Config;
import com.intuit.oauth2.config.Scope;
import com.intuit.oauth2.exception.InvalidRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Service
public class QboService {
    @Value("${QBO_CLIENT_ID}")
    private String qboClientId;

    @Value("${QBO_SECRET}")
    private String qboSecret;

    public String getAuthUri() {

        OAuth2PlatformClient client;
        OAuth2Config oauth2Config;
        System.out.println("qboClientId " + qboClientId);

        //initialize the config
        oauth2Config = new OAuth2Config.OAuth2ConfigBuilder(qboClientId, qboSecret) //set client id, secret
                .callDiscoveryAPI(Environment.SANDBOX) // call discovery API to populate urls
                .buildConfig();
        String csrf = oauth2Config.generateCSRFToken();
        try {
            List<Scope> scopes = new ArrayList<>();
            scopes.add(Scope.Accounting);
            System.out.println(new RedirectView(oauth2Config.prepareUrl(scopes, "https://4fab-142-127-94-250.ngrok-free.app/QBOCallback", csrf), true, true, false));
        } catch (InvalidRequestException e) {
            System.out.println("Exception calling connectToQuickbooks " + e);
        }

        return "new URI";
    }
}
