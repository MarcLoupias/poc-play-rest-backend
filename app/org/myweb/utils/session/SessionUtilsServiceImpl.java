package org.myweb.utils.session;

import org.jetbrains.annotations.Nullable;
import play.Logger;

import static play.mvc.Controller.session;

public class SessionUtilsServiceImpl implements SessionUtilsService {
    public static String SESSION_USER_ID_KEY = "userId";

    @Nullable
    @Override
    public Long getSessionUserId() {
        String val = session(SESSION_USER_ID_KEY);
        if(val == null) {
            return null;
        }

        Long res;
        try {
            res = Long.valueOf(val);
        } catch(NumberFormatException e) {
            Logger.warn("[SessionUtilsServiceImpl] String to Long cast failed in getSessionUserId() method for value " + val);
            return null;
        }

        return res;
    }

    @Override
    public void setSessionUserId(Long id) {
        session(SessionUtilsServiceImpl.SESSION_USER_ID_KEY, String.valueOf(id));
    }

    @Override
    public void deleteSessionUserId() {
        session().clear();
    }

}
