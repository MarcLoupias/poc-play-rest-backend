package controllers;

import controllers.actions.httpHeaders.CORSimplAction;
import controllers.actions.credentials.CredentialsCheckerAction;
import controllers.actions.httpHeaders.CacheControlAction;
import org.myweb.services.version.VersionService;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

public class VersionCtrl extends Controller {

    @With({CacheControlAction.class, CORSimplAction.class})
    public static Result shortVersion() {
        return VersionService.getInstance().getShortVersion().buildPlayCtrlResult();
    }

    @With({CacheControlAction.class, CORSimplAction.class, CredentialsCheckerAction.class})
    public static Result longVersion() {
        return VersionService.getInstance().getLongVersion().buildPlayCtrlResult();
    }
}
