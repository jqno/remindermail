package nl.jqno.quickmail

import android.app.Activity
import android.content.Context._
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget._

import FindView._

class ConfigActivity extends Activity with FindView {
  private val PICK_CONTACT = 1337

  private lazy val state = new State(this)

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.config)
    findView[Button](R.id.config_select).onClick { _ => select }
    findView[Button](R.id.config_done).onClick   { _ => finish }
    paint
  }

  private def select {
    val intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI)
    startActivityForResult(intent, PICK_CONTACT)
  }

  override def onActivityResult(reqCode: Int, resultCode: Int, data: Intent) {
    super.onActivityResult(reqCode, resultCode, data)

    reqCode match {
      case PICK_CONTACT =>
        if (resultCode == Activity.RESULT_OK) {
          updateSettings(data.getData)
          paint
        }
    }
  }

  private def updateSettings(contact: Uri) {
    val contactCursor = managedQuery(contact, null, null, null, null)
    if (contactCursor.moveToFirst()) {
      val id = getString(contactCursor, Contacts.LOOKUP_KEY)
      state.name = getString(contactCursor, Contacts.DISPLAY_NAME)

      val mailCursor = managedQuery(Contacts.EMAIL_CONTENT_URI, null, Contacts.EMAIL_CONTACT_ID + " = ?", Array(id), null)
      if (mailCursor.moveToFirst()) {
        val e = getString(mailCursor, Contacts.EMAIL_DATA)
        state.mail = e
      }
    }
  }

  private def getString(cursor: Cursor, id: String) =
    cursor.getString(cursor getColumnIndex id)

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
  }

  private def paintEmpty {
    findView[TextView](R.id.config_blurb).setText(R.string.config_empty)
    findView[TextView](R.id.config_name).setText("")
    findView[TextView](R.id.config_mail).setText("")
  }
}
