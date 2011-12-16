package nl.jqno.quickmail

import android.app.Activity
import android.content.Context

class State(val activity: Activity) {
  private val NAME_ID = "name"
  private val MAIL_ID = "mail"

  private val prefs = activity.getPreferences(Context.MODE_PRIVATE)

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
