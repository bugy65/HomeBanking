package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity  //habilita la seguridad en la web y te va a joder para que te loguees
@Configuration  //indica que esto es una configuracion
public class WebAuthorization extends WebSecurityConfigurerAdapter {


 @Override
 protected void configure(HttpSecurity http) throws Exception{
  http.authorizeRequests()



          .antMatchers("/web/signUp.html","/web/assets/**", "/web/assets/js/index1.js").permitAll()
          .antMatchers(HttpMethod.POST,"/api/clients","/api/transactions/**").permitAll()
          .antMatchers(HttpMethod.POST,"/api/cards/delete").hasAuthority("CLIENT")


          .antMatchers(HttpMethod.POST,"/api/transactions/generate").hasAuthority("CLIENT")

          .antMatchers("/web/index.html","/web/signUp.html","/web/assets/**", "/web/assets/js/index1.js").permitAll()

          .antMatchers("/**","/api/clients/current/**","/api/clients/current/cards","/api/loans").hasAuthority("CLIENT")

          .antMatchers("/rest/**","/h2-console/**").hasAuthority("ADMIN");






  http.formLogin()

          .usernameParameter("email")

          .passwordParameter("password")

          .loginPage("/api/login");

  http.logout().logoutUrl("/api/logout");


  http.csrf().disable();
  http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
  http.headers().frameOptions().disable();
  http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
  http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
  http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
  http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
 }

 private void clearAuthenticationAttributes(HttpServletRequest request) {

  HttpSession session = request.getSession(false);

  if (session != null) {

   session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

  }

 }
}
