ReminderMail
------------

Do you email reminders to yourself? Do you use a ToDo app that you can send emails to? Or do you have this one special person that you just have to mail ALL THE TIME? Then ReminderMail is the app for you.

With ReminderMail, you can quickly send e-mail to a pre-defined address. It could be:

* Your own
* Your online ToDo-list application (such as [Toodledo](http://www.toodledo.com) or [NirvanaHQ](http://www.nirvanahq.com))
* Your blog (such as [Tumblr](http://www.tumblr.com/docs/nl/email_publishing) or [Wordpress](http://codex.wordpress.org/Post_to_your_blog_using_email))
* Your significant other

Download the app from [Google Play](https://play.google.com/store/apps/details?id=nl.jqno.remindermail)!

Instructions for use:

* Make sure you have a contact for the e-mail address you want to use.
* Open the ReminderMail Configuration app and select the contact.
* Move the ReminderMail app icon to your home screen.

Now you can send yourself or your ToDo app quick reminders, either by opening the app from the home screen, or by sharing a page to "@ ReminderMail".

Note that ReminderMail only supports one e-mail address.

If you want to report an issue, or contact the author, send an e-mail to jqno.android@gmail.com

# Build & release
In order to build this app, you need the following things:

* JDK 1.7. It doesn't work with 8 or up.
* Maven version 3.0.5. You need _precisely_ this version.

Make sure you have build-tools version 23.0.x. If you have anything higher, delete them. You can find them in the `build-tools` directory of you Android SDK.

In order to release the app, you need to do the following things:

* Replace the `???`s in `pom.xml` with the appropriate values.
* `<path-to-mvn-3.0.5>/mvn clean package -P release`
* `<path-to-buildtools-23.0.x>/zipalign 4 target/remindermail-<version>.apk remindermail-<version>.apk`

Copyright (C) 2011, 2013 by Jan Ouwens
This program is free software, released under the GNU General Public License.
For more information, see the [License document](https://raw.github.com/jqno/remindermail/master/COPYING).
