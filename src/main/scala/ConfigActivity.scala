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
import android.app.AlertDialog
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
  private lazy val contextMenuView = find(R.id.config_invisible_context_menu_view)

  private var nameCandidate = ""
  private var emailCandidates = List[String]()

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.config)
    findView[Button](R.id.config_step2_go).onClick { _ => select_mail }
    findView[Button](R.id.config_step3_go).onClick { _ => set_prefix  }
    findView[Button](R.id.config_done).onClick     { _ => finish      }
    findView[Button](R.id.config_about).onClick    { _ => about       }
    findView[ScrollView](R.id.config_scroller).fullScroll(View.FOCUS_UP)
    registerForContextMenu(contextMenuView)

    paintMail
    paintPrefix
  }

  private def select_mail {
    val intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI)
    startActivityForResult(intent, PICK_CONTACT)
  }

  private def set_prefix {
    val alert = new AlertDialog.Builder(this)
    alert.setTitle(R.string.config_step3_alert_title)
    alert.setMessage(R.string.config_step3_alert_message)

    val input = new EditText(this)
    input.setText(state.prefix getOrElse "")
    alert.setView(input)
    alert.positive(R.string.config_step3_alert_set,   { _ => state.prefix = input.getText.toString; paintPrefix })
    alert.negative(R.string.config_step3_alert_clear, { _ => state.prefix = "";                     paintPrefix })

    alert.show()
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
        paintMail
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
    menu.setHeaderTitle(R.string.config_step2_pick_mail)
    emailCandidates.zipWithIndex foreach {
      case (email, index) => menu.add(0, index, 0, email)
    }
  }

  override def onContextItemSelected(item: MenuItem): Boolean = {
    state.setNameAndMail(nameCandidate, emailCandidates(item.getItemId))
    paintMail
    return true
  }

  private def paintMail {
    state.mail match {
      case None    => hideMail
      case Some(_) => showMail
    }
  }

  private def showMail {
    findView[TextView](R.id.config_step2_name).setText(state.name.get)
    findView[TextView](R.id.config_step2_mail).setText(state.mail.get)
    findView[TextView](R.id.config_step2_empty).setVisibility(View.GONE)
    findView[TableLayout](R.id.config_step2_setting_box).setVisibility(View.VISIBLE)
  }

  private def hideMail {
    findView[TextView](R.id.config_step2_name).setText("")
    findView[TextView](R.id.config_step2_mail).setText("")
    findView[TextView](R.id.config_step2_empty).setVisibility(View.VISIBLE)
    findView[TableLayout](R.id.config_step2_setting_box).setVisibility(View.GONE)
  }

  private def paintPrefix = {
    state.prefix match {
      case None     => hidePrefix
      case Some("") => hidePrefix
      case Some(_)  => showPrefix
    }
  }

  private def showPrefix {
    findView[TextView](R.id.config_step3_prefix).setText(state.prefix.get)
    findView[TextView](R.id.config_step3_empty).setVisibility(View.GONE)
    findView[TableLayout](R.id.config_step3_setting_box).setVisibility(View.VISIBLE)
  }

  private def hidePrefix {
    findView[TextView](R.id.config_step3_prefix).setText("")
    findView[TextView](R.id.config_step3_empty).setVisibility(View.VISIBLE)
    findView[TableLayout](R.id.config_step3_setting_box).setVisibility(View.GONE)
  }
}
