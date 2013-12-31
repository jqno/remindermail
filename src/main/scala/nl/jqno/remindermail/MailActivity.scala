/*
 * ReminderMail is an Android app that will let you quickly send e-mail to
 * a pre-defined address.
 *
 * Copyright (C) 2011, 2013 by Jan Ouwens
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
import android.net.Uri
import android.text.TextUtils

class MailActivity extends Activity {
  private val AcceptedAttachmentTypes = List("image/", "application/pdf", "application/x-pdf")
  private lazy val state = new State(this)

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    if (state.mail.isDefined)
      mail()
    else
      configure()
    finish()
  }

  private def mail(): Unit = {
    val intent = new Intent(Intent.ACTION_SEND)
    intent.setType("message/rfc822")

    intent.putExtra(Intent.EXTRA_EMAIL, getRecipient)
    intent.putExtra(Intent.EXTRA_SUBJECT, getSubject)
    intent.putExtra(Intent.EXTRA_TEXT, getText)

    getAttachment foreach { intent.putExtra(Intent.EXTRA_STREAM, _) }

    startActivity(intent)
  }

  private def getRecipient: Array[String] = Array("%s <%s>".format(state.name.get, state.mail.get))

  private def getSubject: String = {
    val tag = state.tag
    val subject = getString(Intent.EXTRA_SUBJECT)
    val hasTag = !TextUtils.isEmpty(tag)
    val hasSubject = !TextUtils.isEmpty(subject)
    val sb = new StringBuilder

    if (hasTag) sb.append(s"[$tag]")
    if (hasTag && hasSubject) sb.append(" ")
    if (hasSubject) sb.append(subject)
    sb.toString
  }

  private def getText: String = getString(Intent.EXTRA_TEXT)

  private def getAttachment: Option[Uri] = {
    val intent = getIntent
    val intentType = intent.getType
    val isActionSend = intent.getAction == Intent.ACTION_SEND
    val isTypeOk = (intentType != null) && (AcceptedAttachmentTypes exists (intentType startsWith _))
    if (isActionSend && isTypeOk)
      Some(intent getParcelableExtra Intent.EXTRA_STREAM)
    else
      None
  }

  private def getString(id: String): String = {
    val intent = getIntent
    if (intent.getAction == Intent.ACTION_SEND)
      intent getStringExtra id
    else
      ""
  }

  private def configure(): Unit = {
    val intent = new Intent(this, classOf[ConfigActivity])
    startActivity(intent)
  }
}
