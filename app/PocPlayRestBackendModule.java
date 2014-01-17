import com.google.inject.AbstractModule;
import models.ModelFactoryHelper;
import models.ModelFactoryHelperProdImpl;
import models.ModelFactoryHelperTestImpl;
import org.myweb.db.Dao;
import org.myweb.db.DaoJpa;
import org.myweb.services.crud.create.*;
import org.myweb.services.crud.delete.*;
import org.myweb.services.crud.get.*;
import org.myweb.services.crud.query.*;
import org.myweb.services.crud.update.*;

import org.myweb.services.user.check.*;
import org.myweb.services.user.create.*;
import org.myweb.services.user.login.*;
import org.myweb.services.user.update.*;
import org.myweb.utils.mail.*;
import org.myweb.utils.security.*;
import org.myweb.utils.session.*;
import play.Play;

public class PocPlayRestBackendModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(MailUtilsService.class).to(MailUtilsServiceJavamailImpl.class);
        bind(SecurityUtilsService.class).to(SecurityUtilsServiceImpl.class);
        bind(PasswordGenerationService.class).to(PasswordGenerationServiceImpl.class);
        bind(SessionUtilsService.class).to(SessionUtilsServiceImpl.class);

        bind(Dao.class).to(DaoJpa.class);

        bind(CreateServiceJava.class).to(CreateServiceJavaImpl.class);
        bind(CreateServiceRest.class).to(CreateServiceRestImpl.class);
        bind(DeleteServiceJava.class).to(DeleteServiceJavaImpl.class);
        bind(DeleteServiceRest.class).to(DeleteServiceRestImpl.class);
        bind(GetServiceJava.class).to(GetServiceJavaImpl.class);
        bind(GetServiceRest.class).to(GetServiceRestImpl.class);
        bind(QueryServiceJava.class).to(QueryServiceJavaImpl.class);
        bind(QueryServiceRest.class).to(QueryServiceRestImpl.class);
        bind(UpdateServiceJava.class).to(UpdateServiceJavaImpl.class);
        bind(UpdateServiceRest.class).to(UpdateServiceRestImpl.class);

        bind(UserCheckEmailServiceJava.class).to(UserCheckEmailServiceJavaImpl.class);
        bind(UserCheckLoginServiceJava.class).to(UserCheckLoginServiceJavaImpl.class);
        bind(UserCreateServiceJava.class).to(UserCreateServiceJavaImpl.class);
        bind(UserCreateServiceRest.class).to(UserCreateServiceRestImpl.class);
        bind(UserLoginServiceRest.class).to(UserLoginServiceRestImpl.class);
        bind(UserUpdateServiceRest.class).to(UserUpdateServiceRestImpl.class);

        if( Play.isDev() || Play.isTest() ) {
            bind(ModelFactoryHelper.class).to(ModelFactoryHelperTestImpl.class);
        } else if( Play.isProd() ) {
            bind(ModelFactoryHelper.class).to(ModelFactoryHelperProdImpl.class);
        }
    }
}
