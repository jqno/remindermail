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
import android.view.View
import scala.language.implicitConversions
import android.widget.EditText
import android.text.{Editable, TextWatcher}

trait FindView extends Activity {
  def findView[WidgetType <: View](id: Int): WidgetType = findViewById(id).asInstanceOf[WidgetType]
  def find(id: Int): View = findView[View](id)
}

class ViewWithOnClick(view: View) {
  def onClick(action: => Unit): Unit = {
    view.setOnClickListener(new View.OnClickListener() {
      def onClick(v: View): Unit = action
    })
  }
}

class EditTextWithOnTextChanged(editText: EditText) {
  def onTextChanged(action: => Unit): Unit = {
    editText.addTextChangedListener(new TextWatcher {
      def beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int): Unit = ()
      def onTextChanged(s: CharSequence, start: Int, before: Int, count: Int): Unit = ()
      def afterTextChanged(s: Editable): Unit = action
    })
  }
}

object FindView extends Activity {
  implicit def addOnClickToViews(view: View): ViewWithOnClick =
    new ViewWithOnClick(view)

  implicit def addOnTextChangedToEditTexts(editText: EditText): EditTextWithOnTextChanged =
    new EditTextWithOnTextChanged(editText)
}

