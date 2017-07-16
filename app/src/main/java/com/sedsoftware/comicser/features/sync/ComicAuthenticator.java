package com.sedsoftware.comicser.features.sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;

public class ComicAuthenticator extends AbstractAccountAuthenticator {

  public ComicAuthenticator(Context context) {
    super(context);
  }

  @Override
  public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
    throw new UnsupportedOperationException("editProperties operation is not supported");
  }

  @Override
  public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
      String authTokenType, String[] requiredFeatures, Bundle options)
      throws NetworkErrorException {
    return null;
  }

  @Override
  public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account,
      Bundle options) throws NetworkErrorException {
    return null;
  }

  @Override
  public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
      String authTokenType, Bundle options) throws NetworkErrorException {
    throw new UnsupportedOperationException("getAuthToken operation is not supported");
  }

  @Override
  public String getAuthTokenLabel(String authTokenType) {
    throw new UnsupportedOperationException("getAuthTokenLabel operation is not supported");
  }

  @Override
  public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
      String authTokenType, Bundle options) throws NetworkErrorException {
    throw new UnsupportedOperationException("updateCredentials operation is not supported");
  }

  @Override
  public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account,
      String[] features) throws NetworkErrorException {
    throw new UnsupportedOperationException("hasFeatures operation is not supported");
  }
}
