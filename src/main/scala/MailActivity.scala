/*
 * QuickMail is an Android app that will let you quickly send e-mail to
 * a pre-defined address.
 *
 * Copyright (C) 2011 by Jan Ouwens
 *
 * This file is part of QuickMail.
 *
 * QuickMail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * QuickMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with QuickMail.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.jqno.quickmail

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
    val recipient = "%s <%s>".format(state.name.get, state.mail.get)
    val (subject, text) = determineContent

    val intent = new Intent(Intent.ACTION_SEND)
    intent.setType("plain/text")
    intent.putExtra(Intent.EXTRA_EMAIL, Array(recipient))
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, text)

    startActivity(intent)
  }

  private def determineContent: (String, String) = {
    val intent = getIntent
    if (intent.getAction == Intent.ACTION_SEND)
      (intent.getStringExtra(Intent.EXTRA_SUBJECT), intent.getStringExtra(Intent.EXTRA_TEXT))
    else
      ("", "")
  }

  private def configure {
    val intent = new Intent(this, classOf[ConfigActivity])
    startActivity(intent)
  }
}
