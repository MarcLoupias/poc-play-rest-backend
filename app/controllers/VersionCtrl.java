package controllers;

import org.myweb.services.version.VersionService;
import play.mvc.Controller;
import play.mvc.Result;

public class VersionCtrl extends Controller {

    public static Result shortVersion() {

        return VersionService.getInstance().getShortVersion()
                .buildPlayCtrlResult();

    }

    public static Result longVersion() {

        return VersionService.getInstance().getLongVersion()
                .buildPlayCtrlResult();

    }
}
