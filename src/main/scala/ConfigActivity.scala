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
import android.content.Context._
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.MenuItem
import android.view.View
import android.widget._

import FindView._

class ConfigActivity extends Activity with FindView {
  private val PICK_CONTACT = 1337

  private lazy val state = new State(this)
  private lazy val contextMenuView = find(R.id.invisible_context_menu_view)

  private var nameCandidate = ""
  private var emailCandidates = List[String]()

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.config)
    findView[Button](R.id.config_select).onClick { _ => select }
    findView[Button](R.id.config_done).onClick   { _ => finish }
    findView[Button](R.id.config_about).onClick  { _ => about  }
    registerForContextMenu(contextMenuView)
    paint
  }

  private def select {
    val intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI)
    startActivityForResult(intent, PICK_CONTACT)
  }

  private def about {
    val intent = new Intent(this, classOf[AboutActivity])
    startActivity(intent)
  }

  override def onActivityResult(reqCode: Int, resultCode: Int, data: Intent) {
    super.onActivityResult(reqCode, resultCode, data)

    reqCode match {
      case PICK_CONTACT =>
        if (resultCode == Activity.RESULT_OK) {
          updateSettings(data.getData)
        }
    }
  }

  private def updateSettings(contact: Uri) {
    val contactCursor = managedQuery(contact, null, null, null, null)
    if (contactCursor.moveToFirst()) {
      val id = getString(contactCursor, Contacts.LOOKUP_KEY)

      nameCandidate = getString(contactCursor, Contacts.DISPLAY_NAME)
      emailCandidates = List[String]()

      val mailCursor = managedQuery(Contacts.EMAIL_CONTENT_URI, null, Contacts.EMAIL_CONTACT_ID + " = ?", Array(id), null)
      while (mailCursor.moveToNext()) {
        val e = getString(mailCursor, Contacts.EMAIL_DATA)
        emailCandidates = e :: emailCandidates
      }

      if (emailCandidates.size == 1) {
        state.setNameAndMail(nameCandidate, emailCandidates(0))
        paint
      }
      else {
        openContextMenu(contextMenuView)
      }
    }
  }

  private def getString(cursor: Cursor, id: String) =
    cursor.getString(cursor getColumnIndex id)

  override def onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo)
    menu.setHeaderTitle(R.string.config_pick_mail)
    emailCandidates.zipWithIndex foreach {
      case (email, index) => menu.add(0, index, 0, email)
    }
  }

  override def onContextItemSelected(item: MenuItem): Boolean = {
    state.setNameAndMail(nameCandidate, emailCandidates(item.getItemId))
    paint
    return true
  }

  private def paint {
    state.mail match {
      case Some(_) => paintFull
      case None    => paintEmpty
    }
  }

  private def paintFull {
    findView[TextView](R.id.config_blurb).setText(R.string.config_full)
    findView[TextView](R.id.config_name).setText(state.name.get)
    findView[TextView](R.id.config_mail).setText(state.mail.get)
    findView[TableLayout](R.id.config_setting_box).setVisibility(View.VISIBLE)
  }

  private def paintEmpty {
    findView[TextView](R.id.config_blurb).setText(R.string.config_empty)
    findView[TextView](R.id.config_name).setText("")
    findView[TextView](R.id.config_mail).setText("")
    findView[TableLayout](R.id.config_setting_box).setVisibility(View.GONE)
  }
}
