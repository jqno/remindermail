/*
 * ReminderMail is an Android app that will let you quickly send e-mail to
 * a pre-defined address.
 *
 * Copyright (C) 2011 by Jan Ouwens
 *
 * This file is part of ReminderMail.
 *
 * ReminderMail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReminderMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ReminderMail.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.jqno.remindermail;

import android.net.Uri;
import android.provider.ContactsContract;

/*
 * The class ContactsContract.Contacts is both protected and an interface.
 * Due to a limitation in the Scala compiler[1], static fields in such a class
 * are inaccessible from Scala. Therefore, the fields have to be made available
 * through Java, which is what this class is for.
 *
 * [1] https://issues.scala-lang.org/browse/SI-1806
 */
class Contacts {
  public static Uri    CONTENT_URI  = ContactsContract.Contacts.CONTENT_URI;
  public static String LOOKUP_KEY   = ContactsContract.Contacts._ID;
  public static String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;

  public static Uri    EMAIL_CONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
  public static String EMAIL_CONTACT_ID  = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
  public static String EMAIL_DATA        = ContactsContract.CommonDataKinds.Email.DATA;
}
