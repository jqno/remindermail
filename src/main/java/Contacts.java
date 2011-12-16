package nl.jqno.quickmail;

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
