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

import android.content.Context
import android.os.Build.VERSION

class State(val ctx: Context) {
  private val SettingsId = "state"
  private val NameId     = "name"
  private val MailId     = "mail"
  private val TagId      = "tag"
  private val IceCreamSandwich = 14

  private val prefs = ctx.getSharedPreferences(SettingsId, Context.MODE_PRIVATE)

  def name: Option[String] = Option(prefs.getString(NameId, null))
  def mail: Option[String] = Option(prefs.getString(MailId, null))
  def tag: String = prefs.getString(TagId, "ReminderMail")

  def setNameAndMail(name: String, mail: String): Unit = {
    set(NameId, name)
    set(MailId, mail)
  }

  def setTag(tag: String): Unit = {
    set(TagId, tag)
  }

  private def set(id: String, value: String): Unit = {
    val editor = prefs.edit
    editor.putString(id, value)
    editor.commit()
  }

  def color: Int =
    if (VERSION.SDK_INT < IceCreamSandwich)
      R.color.android2
    else
      R.color.android4
}
