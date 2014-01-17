package controllers;

import credentials.CredentialsCheckerAction;
import org.myweb.services.version.VersionService;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

public class VersionCtrl extends Controller {

    public static Result shortVersion() {
        return VersionService.getInstance().getShortVersion().buildPlayCtrlResult();
    }

    @With(CredentialsCheckerAction.class)
    public static Result longVersion() {
        return VersionService.getInstance().getLongVersion().buildPlayCtrlResult();
    }
}
