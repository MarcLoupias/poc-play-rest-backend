package controllers.actions.httpHeaders;

import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;

public class CacheControlAction extends Action<CacheControl> {
    public F.Promise<SimpleResult> call(Http.Context ctx) throws Throwable {

        ctx.response().setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

        return delegate.call(ctx) ;
    }
}
