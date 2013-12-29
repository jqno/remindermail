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
  private val PickContact = 1337
  private val ColorLines = List(
    R.id.config_step1_line,
    R.id.config_step2_line,
    R.id.config_step3_line,
    R.id.config_step4_line,
    R.id.config_done_line
  )

  private lazy val state = new State(this)
  private lazy val contextMenuView = find(R.id.config_invisible_context_menu_view)

  private var nameCandidate = ""
  private var emailCandidates = List.empty[String]

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.config)
    findView[Button](R.id.config_step2_go) onClick selectMail()
    findView[Button](R.id.config_step3_clear) onClick clearTag()
    findView[Button](R.id.config_done) onClick finish()
    findView[Button](R.id.config_about) onClick about()

    val edit = findView[EditText](R.id.config_step3_tag)
    edit.setSelection(edit.getText.length)
    edit.setText(state.tag)
    edit onTextChanged tagChanged()

    findView[ScrollView](R.id.config_scroller).fullScroll(View.FOCUS_UP)
    registerForContextMenu(contextMenuView)

    paintColorLines()
    paintMail()
  }

  private def selectMail(): Unit = {
    val intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI)
    startActivityForResult(intent, PickContact)
  }

  private def about(): Unit = {
    val intent = new Intent(this, classOf[AboutActivity])
    startActivity(intent)
  }

  private def clearTag(): Unit = {
    findView[EditText](R.id.config_step3_tag).setText("")
    state.setTag("")
  }

  private def tagChanged(): Unit = {
    val edit = findView[EditText](R.id.config_step3_tag)
    val tag = edit.getText.toString
    state.setTag(tag)
  }

  override def onActivityResult(reqCode: Int, resultCode: Int, data: Intent): Unit = {
    super.onActivityResult(reqCode, resultCode, data)

    reqCode match {
      case PickContact =>
        if (resultCode == Activity.RESULT_OK) {
          updateSettings(data.getData)
        }
    }
  }

  private def updateSettings(contact: Uri): Unit = {
    val contactCursor = managedQuery(contact, null, null, null, null)
    if (contactCursor.moveToFirst()) {
      val id = getString(contactCursor, Contacts.LOOKUP_KEY)

      nameCandidate = getString(contactCursor, Contacts.DISPLAY_NAME)
      emailCandidates = List.empty[String]

      val mailCursor = managedQuery(Contacts.EMAIL_CONTENT_URI, null, s"${Contacts.EMAIL_CONTACT_ID} = ?", Array(id), null)
      while (mailCursor.moveToNext()) {
        val e = getString(mailCursor, Contacts.EMAIL_DATA)
        emailCandidates = e :: emailCandidates
      }

      if (emailCandidates.size == 1) {
        state.setNameAndMail(nameCandidate, emailCandidates(0))
        paintMail()
      }
      else {
        openContextMenu(contextMenuView)
      }
    }
  }

  private def getString(cursor: Cursor, id: String): String =
    cursor.getString(cursor getColumnIndex id)

  override def onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo): Unit = {
    super.onCreateContextMenu(menu, v, menuInfo)
    menu.setHeaderTitle(R.string.config_step2_pick_mail)
    for ((email, index) <- emailCandidates.zipWithIndex) {
      menu.add(0, index, 0, email)
    }
  }

  override def onContextItemSelected(item: MenuItem): Boolean = {
    state.setNameAndMail(nameCandidate, emailCandidates(item.getItemId))
    paintMail()
    return true
  }

  private def paintColorLines(): Unit =
    for (line <- ColorLines) { find(line).setBackgroundResource(state.color) }

  private def paintMail(): Unit =
    if (state.mail.isDefined)
      showMail()
    else
      hideMail()

  private def showMail(): Unit = {
    findView[TextView](R.id.config_step2_name).setText(state.name.get)
    findView[TextView](R.id.config_step2_mail).setText(state.mail.get)
    findView[TextView](R.id.config_step2_empty).setVisibility(View.GONE)
    findView[TableLayout](R.id.config_step2_setting_box).setVisibility(View.VISIBLE)
  }

  private def hideMail(): Unit = {
    findView[TextView](R.id.config_step2_name).setText("")
    findView[TextView](R.id.config_step2_mail).setText("")
    findView[TextView](R.id.config_step2_empty).setVisibility(View.VISIBLE)
    findView[TableLayout](R.id.config_step2_setting_box).setVisibility(View.GONE)
  }
}
