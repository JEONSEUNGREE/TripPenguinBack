package com.trip.penguin.account.converter;

import com.trip.penguin.constant.OAuthVendor;
import com.trip.penguin.account.model.GoogleUser;
import com.trip.penguin.account.model.ProviderUser;
import com.trip.penguin.account.util.OAuth2Utils;

public class OAuth2GoogleProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

	@Override
	public ProviderUser converter(ProviderUserRequest providerUserRequest) {

		if (!OAuthVendor.GOOGLE.getOAuthVendor().equals(providerUserRequest.clientRegistration().getRegistrationId())) {
			return null;
		}
		return new GoogleUser(
			OAuth2Utils.getMainAttributes(providerUserRequest.oAuth2User()),
			providerUserRequest.oAuth2User(),
			providerUserRequest.clientRegistration());
	}

}