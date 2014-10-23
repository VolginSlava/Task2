package com.example.contacts;

import android.net.Uri;

import java.io.Serializable;

import com.example.contacts.tools.Logging;

public class ContactData implements Serializable {
	private static final long serialVersionUID = -5158009157779590079L;

	// private Uri photoUri; // to support serialization
	private String photoUri;
	private String name;
	private String email;


	public ContactData() {
		Logging.logEntrance();
	}


	public Uri getPhotoUri() {
		return photoUri != null ? Uri.parse(photoUri) : null;
	}

	public void setPhotoUri(Uri photo) {
		this.photoUri = photo != null ? photo.toString() : null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return String.format("[name=%s, email=%s, uri=%s]", name, email, photoUri);
	}
}
