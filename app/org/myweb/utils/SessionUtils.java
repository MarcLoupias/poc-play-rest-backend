package org.myweb.utils;

import org.jetbrains.annotations.Nullable;
import play.Logger;

import static play.mvc.Controller.session;

public class SessionUtils {
    public static String SESSION_USER_ID_KEY = "userId";

    @Nullable
    public static Long getSessionUserId() {
        String val = session(SESSION_USER_ID_KEY);
        if(val == null) {
            return null;
        }

        Long res;
        try {
            res = Long.valueOf(val);
        } catch(NumberFormatException e) {
            Logger.warn("[UserLoginService] String to Long cast failed in getSessionUserId() method for value " + val);
            return null;
        }

        return res;
    }
}
