package it.prova.pokeronline.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
    private CustomAuthenticationSuccessHandlerImpl successHandler;
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
         .userDetailsService(customUserDetailsService)
         .passwordEncoder(passwordEncoder());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	 http.authorizeRequests()
         .antMatchers("/assets/**").permitAll()
         .antMatchers("/login/**").permitAll()
         .antMatchers("/registrazione/**").permitAll()
         .antMatchers("/utente/resetuserpassword").hasAnyRole("ADMIN", "PLAYER", "SPECIAL_PLAYER")
         .antMatchers("/utente/saveresetuserpw").hasAnyRole("ADMIN", "PLAYER", "SPECIAL_PLAYER")
         .antMatchers("/utente/credito").hasAnyRole("ADMIN", "PLAYER", "SPECIAL_PLAYER")
         .antMatchers("/utente/addcredito").hasAnyRole("ADMIN", "PLAYER", "SPECIAL_PLAYER")
         .antMatchers("/utente/searchUtenteAjax").hasAnyRole("ADMIN", "PLAYER", "SPECIAL_PLAYER")
         .antMatchers("/tavolo/**").hasAnyRole("ADMIN", "SPECIAL_PLAYER")
         .antMatchers("/tavolo/search").hasAnyRole("ADMIN", "PLAYER", "SPECIAL_PLAYER")
         .antMatchers("/tavolo/list").hasAnyRole("ADMIN", "PLAYER", "SPECIAL_PLAYER")
         .antMatchers("/tavolo/show").hasAnyRole("ADMIN", "PLAYER", "SPECIAL_PLAYER")
         .antMatchers("/utente/**").hasRole("ADMIN")
         .antMatchers("/gioca/**").hasAnyRole("ADMIN", "PLAYER", "SPECIAL_PLAYER")
         .antMatchers("/**").hasAnyRole("ADMIN", "PLAYER", "SPECIAL_PLAYER")
         //.antMatchers("/anonymous*").anonymous()
         .anyRequest().authenticated()
         .and().exceptionHandling().accessDeniedPage("/accessDenied")
         .and()
         	.formLogin()
         	.loginPage("/login")
         	//.defaultSuccessUrl("/home",true)
         	//uso un custom handler perché voglio mettere delle user info in session
         	.successHandler(successHandler)
         	.failureUrl("/login?error=true")
         	.permitAll()
         .and()
         	.logout()
         	.logoutSuccessUrl("/executeLogout")
            .invalidateHttpSession(true)
            .permitAll()
         .and()
            .csrf()
            .disable();
//         
    }
}
