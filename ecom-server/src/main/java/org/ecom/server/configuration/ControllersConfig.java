package org.ecom.server.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class ControllersConfig {

   /**
      Creating this bean to send appropriate message while exception handling
    */
   @Bean
   public MessageSource messageSource() {
      ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
      messageSource.setBasenames("classpath:messages/messages");
      messageSource.setDefaultEncoding("UTF-8");
      return messageSource;
   }
}
