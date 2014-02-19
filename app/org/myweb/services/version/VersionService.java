package org.myweb.services.version;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.myweb.services.RestServiceResult;
import org.myweb.services.ServiceException;
import play.Play;
import play.i18n.Messages;
import play.libs.Json;

import static play.mvc.Http.Status.INTERNAL_SERVER_ERROR;
import static play.mvc.Http.Status.OK;

public class VersionService {

    public static class ShortVersion {
        public String appName;
        public String appVersion;
    }

    public static class LongVersion extends ShortVersion {
        public String playVersion;
        public String javaVersion;
        public String jvmName;
        public String jvmVersion;
        public String osTimezone;
        public String osCountry;
        public String osArch;
        public String osName;
        public String osVersion;
    }

    private static VersionService instance = new VersionService();

    public static VersionService getInstance() {
        return instance;
    }

    private VersionService() {

    }

    public RestServiceResult getShortVersion() throws ServiceException {
        try {
            ShortVersion v = new ShortVersion();
            v.appName = appInfos.BuildInfo.name();
            v.appVersion = appInfos.BuildInfo.version();
            return RestServiceResult.buildServiceResult(OK, Json.toJson(v));

        } catch (Exception e) {
            throw new ServiceException(
                    VersionService.class.getName(), INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e),
                    Messages.get("java.service.result.generic.error.msg")
            );
        }
    }

    public RestServiceResult getLongVersion() throws ServiceException {
        try {
            LongVersion v = new LongVersion();
            v.appName = appInfos.BuildInfo.name();
            v.appVersion = appInfos.BuildInfo.version();
            v.playVersion = Play.application().configuration().getString("play.version");
            v.javaVersion = Play.application().configuration().getString("java.version");
            v.jvmName = Play.application().configuration().getString("java.vm.name");
            v.jvmVersion = Play.application().configuration().getString("java.vm.version");
            v.osTimezone = Play.application().configuration().getString("user.timezone");
            v.osCountry = Play.application().configuration().getString("user.country");
            v.osArch = Play.application().configuration().getString("os.arch");
            v.osName = Play.application().configuration().getString("os.name");
            v.osVersion = Play.application().configuration().getString("os.version");

            return RestServiceResult.buildServiceResult(OK, Json.toJson(v));

        } catch (Exception e) {
            throw new ServiceException(
                    VersionService.class.getName(), INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e),
                    Messages.get("java.service.result.generic.error.msg")
            );
        }
    }
}
