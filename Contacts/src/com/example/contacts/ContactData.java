package com.example.contacts;

import android.media.Image;

import java.io.Serializable;

import com.example.contacts.tools.Logging;

public class ContactData implements Serializable {
	private static final long serialVersionUID = -5158009157779590079L;

	// private Image photo;
	private String name;
	private String email;

	public ContactData(Image photo, String name, String email) {
		// this.photo = photo;
		this.name = name;
		this.email = email;

		Logging.logEntrance(this.toString());
	}

	// public Image getPhoto() {
	// return photo;
	// }
	//
	// public void setPhoto(Image photo) {
	// this.photo = photo;
	// }

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
		return String.format("[name=%s, email=%s]", name, email);
	}
}
