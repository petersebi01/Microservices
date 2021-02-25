package microservices.user.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration // beaneket fog legyártani
public class ComponentProvider {

    @Bean
    @Scope("prototype")
    public Logger logger(InjectionPoint injectionPoint){ // futási idöben lekérdezhetjük, hogy melyik osztályba fogja injektálni
        return LoggerFactory.getLogger(injectionPoint.getField().getDeclaringClass()); // a fieldnél ha beinjektáltuk, melyik osztályon belül történt az
    }
}
