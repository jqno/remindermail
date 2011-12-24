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
package nl.jqno.remindermail

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class MailActivity extends Activity {
  private lazy val state = new State(this)

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    state.mail match {
      case Some(_) => mail
      case None    => configure
    }
    finish()
  }

  private def mail {
    val intent = new Intent(Intent.ACTION_SEND)
    intent.setType("plain/text")

    intent.putExtra(Intent.EXTRA_EMAIL, getRecipient)
    intent.putExtra(Intent.EXTRA_TEXT, getText)
    intent.putExtra(Intent.EXTRA_SUBJECT, getSubject)

    startActivity(intent)
  }

  private def getRecipient: Array[String] = Array("%s <%s>".format(state.name.get, state.mail.get))
  private def getText: String = getString(Intent.EXTRA_TEXT)
  private def getSubject: String = {
    val subject = getString(Intent.EXTRA_SUBJECT)
    state.prefix match {
      case None         => subject
      case Some("")     => subject
      case Some(prefix) => "[%s] %s".format(prefix, subject)
    }
  }

  private def getString(id: String): String = {
    val intent = getIntent
    if (intent.getAction == Intent.ACTION_SEND)
      intent getStringExtra id
    else
      ""
  }

  private def configure {
    val intent = new Intent(this, classOf[ConfigActivity])
    startActivity(intent)
  }
}
