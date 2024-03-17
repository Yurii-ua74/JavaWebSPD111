package step.learning.ioc;

import com.google.inject.servlet.ServletModule;
import step.learning.servlets.*;
public class RouterModule extends ServletModule {
    // ctr+O
    @Override
    protected void configureServlets() {
        serve( "/" ).with( HomeServlet.class ) ;
        serve( "/cart" ).with( CartServlet.class ) ;
        serve( "/promotion" ).with( PromotionsServlet.class ) ;
        serve( "/signup" ).with( SignupServlet.class ) ;
        serve( "/addProduct" ).with( AddProductServlet.class ) ;
    }
}
