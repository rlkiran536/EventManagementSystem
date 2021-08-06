package jaassecurity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import dao.*;
import dataModels.*;
import utilities.*;

public class JaasLoginModule implements LoginModule {
	  private CallbackHandler handler;
	  private Subject subject;
	  private UserPrincipal userPrincipal;
	  private RolePrincipal rolePrincipal;
	  private String login;
	  private List<String> userGroups;

	  public void initialize(Subject subject,
	      CallbackHandler callbackHandler,
	      Map<String, ?> sharedState,
	      Map<String, ?> options) {

	    handler = callbackHandler;
	    this.subject = subject;
	  }

	  @Override
	  public boolean login() throws LoginException {

	    Callback[] callbacks = new Callback[2];
	    callbacks[0] = new NameCallback("login");
	    callbacks[1] = new PasswordCallback("password", true);

	    try {
	      handler.handle(callbacks);
	      String username = ((NameCallback) callbacks[0]).getName();
	      String password = String.valueOf(((PasswordCallback) callbacks[1])
	          .getPassword());
	      ExtraUtilities eu = new ExtraUtilities();
	      if ( eu.checkString(username) && eu.checkString(password)) {
	    	  UserModel user = new UserDao().userLogin(username,password);
	    	  if(user!=null) {
	    	  	login = username;
	    	  	userGroups = new ArrayList<String>();
	    	  	if(user.getRole().equals("ADMINISTRATOR"))
	    	  		userGroups.add("ADMINISTRATOR");
	    	  	else if(user.getRole().equals("FACULTY"))
	    	  		userGroups.add("FACULTY");
	    	  	else if(user.getRole().equals("STUDENT"))
	    	  		userGroups.add("STUDENT");
	    	  	else throw new LoginException("Failed to authenticate user");
	    	  	return true;	
	    	  }	    	  
	      } 
	      throw new LoginException("Failed to authenticate user");

	    } catch (IOException e) {
	      throw new LoginException(e.getMessage());
	    } catch (UnsupportedCallbackException e) {
	      throw new LoginException(e.getMessage());
	    }

	  }

	  @Override
	  public boolean commit() throws LoginException {

	    userPrincipal = new UserPrincipal(login);
	    subject.getPrincipals().add(userPrincipal);

	    if (userGroups != null && userGroups.size() > 0) {
	      for (String groupName : userGroups) {
	        rolePrincipal = new RolePrincipal(groupName);
	        subject.getPrincipals().add(rolePrincipal);
	      }
	    }

	    return true;
	  }

	  @Override
	  public boolean abort() throws LoginException {
	    return false;
	  }

	  @Override
	  public boolean logout() throws LoginException {
	    subject.getPrincipals().remove(userPrincipal);
	    subject.getPrincipals().remove(rolePrincipal);
	    return true;
	  }

	}