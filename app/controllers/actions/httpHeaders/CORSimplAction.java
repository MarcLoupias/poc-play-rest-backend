package controllers.actions.httpHeaders;

import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;

public class CORSimplAction extends Action<CORSimpl> {
    public F.Promise<SimpleResult> call(Http.Context ctx) throws Throwable {

        ctx.response().setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:9000"); // TODO load values from env
        ctx.response().setHeader("Access-Control-Allow-Credentials", "true");
        ctx.response().setHeader("Access-Control-Expose-Headers","X-Total-Count"); // TODO load values from env
        // TODO update doc for CORS & related config
        return delegate.call(ctx) ;
    }
}
