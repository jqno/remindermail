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
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget._

import FindView._

class AboutActivity extends Activity with FindView {
  private lazy val state = new State(this)

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.about)
    linkify(findView[TextView](R.id.about_explanation))
    linkify(findView[TextView](R.id.about_website))
    findView[Button](R.id.about_close) onClick finish()
    find(R.id.about_line).setBackgroundResource(state.color)
  }

  private def linkify(textView: TextView) {
    textView.setMovementMethod(LinkMovementMethod.getInstance)
    val text = textView.getText.toString
    textView.setText(Html.fromHtml(text))
  }
}
