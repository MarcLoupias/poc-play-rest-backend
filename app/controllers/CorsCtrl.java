package controllers;

import com.google.inject.Inject;
import controllers.actions.httpHeaders.CacheControlAction;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.myweb.utils.config.EnvConfigService;
import org.myweb.utils.config.EnvConfigServiceException;
import play.Logger;
import play.db.jpa.Transactional;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

public class CorsCtrl extends Controller {

    @Inject
    private EnvConfigService envConfigService;

    @Transactional(readOnly = false)
    @With(CacheControlAction.class)
    public Result checkPreFlight(@SuppressWarnings("UnusedParameters") String path) {

        String origin;

        try {
            origin = envConfigService.getEnvVarAsString(EnvConfigService.PPRB_CORS_ACCESS_CONTROL_ALLOW_ORIGIN);
            if(origin == null) {
                throw new EnvConfigServiceException("PPRB_CORS_ACCESS_CONTROL_ALLOW_ORIGIN env var is not set !");
            }
        } catch (EnvConfigServiceException e) {
            Logger.error(
                    "CORS impl : Impossible to load PPRB_CORS_ACCESS_CONTROL_ALLOW_ORIGIN env var !\n" +
                            ExceptionUtils.getStackTrace(e)
            );
            return internalServerError(Messages.get("cors.error.checkpreflight"));
        }

        response().setHeader("Access-Control-Allow-Origin", origin);
        response().setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        response().setHeader("Access-Control-Max-Age", "0");
        response().setHeader("Access-Control-Allow-Headers","Content-Type");
        response().setHeader("Access-Control-Allow-Credentials", "true");

        return noContent();
    }
}
