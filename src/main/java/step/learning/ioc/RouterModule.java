package step.learning.ioc;

import com.google.inject.servlet.ServletModule;
import step.learning.filters.CharsetFilter;
import step.learning.servlets.*;


public class RouterModule extends ServletModule{
    @Override
    protected void configureServlets() {
        filter( "/*" ).through( CharsetFilter.class ) ;

        serve( "/" ).with( HomeServlet.class   ) ;
        serve( "/addProduct" ).with( AddProductServlet.class ) ;
        serve( "/auth"   ).with( AuthServlet.class   ) ;
        serve( "/cart"   ).with( CartServlet.class   ) ;
        serve( "/promotion" ).with( PromotionServlet.class ) ;
        serve( "/shop"   ).with( ShopServlet.class   ) ;
        serve( "/signup" ).with( SignupServlet.class ) ;

        serve( "/shop-api" ).with( ShopApiServlet.class ) ;
    }
}
