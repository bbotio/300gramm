package org.springframework.gramm.util;

import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.oauth.InstagramService;
import org.springframework.gramm.Constants;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class InstagramUtils {

    private static final String CONFIG_PROPERTIES = "/config.properties";

    public static Properties getConfigProperties() {
        InputStream input = null;
        final Properties prop = new Properties();
        try {
            input = InstagramUtils.class.getResourceAsStream(CONFIG_PROPERTIES);
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;

    }

    public static boolean isLogged(HttpServletRequest request){
        HttpSession session = request.getSession();
        Object objInstagram = session.getAttribute(Constants.INSTAGRAM_OBJECT);

        return objInstagram != null;
    }


}