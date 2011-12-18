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

import android.content.Context

class State(val ctx: Context) {
  private val SETTINGS_ID = "state"
  private val NAME_ID = "name"
  private val MAIL_ID = "mail"

  private val prefs = ctx.getSharedPreferences(SETTINGS_ID, Context.MODE_PRIVATE)

  def name = Option(prefs.getString(NAME_ID, null))
  def name_=(name: String) = set(NAME_ID, name)

  def mail = Option(prefs.getString(MAIL_ID, null))
  def mail_=(mail: String) = set(MAIL_ID, mail)

  private def set(id: String, value: String) {
    val editor = prefs.edit
    editor.putString(id, value)
    editor.commit()
  }
}
