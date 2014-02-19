package controllers.actions.httpHeaders;

import org.myweb.utils.config.EnvConfigService;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;

public class CORSimplAction extends Action<CORSimpl> {
    public F.Promise<SimpleResult> call(Http.Context ctx) throws Throwable {

        String origin = System.getenv(EnvConfigService.PPRB_CORS_ACCESS_CONTROL_ALLOW_ORIGIN);
        ctx.response().setHeader("Access-Control-Allow-Origin", origin);
        ctx.response().setHeader("Access-Control-Allow-Credentials", "true");
        ctx.response().setHeader("Access-Control-Expose-Headers","X-Total-Count");
        // TODO update doc for CORS & related config
        return delegate.call(ctx) ;
    }
}
