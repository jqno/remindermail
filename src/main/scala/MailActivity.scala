package nl.jqno.quickmail

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class MailActivity extends Activity {
  private lazy val state = new State(this)

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    state.mail match {
      case Some(_) => mail
      case None    => configure
    }
    finish()
  }

  private def mail {
    val recipient = "%s <%s>".format(state.name.get, state.mail.get)
    val intent = new Intent(Intent.ACTION_SEND)
    intent.setType("plain/text")
    intent.putExtra(Intent.EXTRA_EMAIL, Array(recipient))
    startActivity(intent)
  }

  private def configure {
    val intent = new Intent(this, classOf[ConfigActivity])
    startActivity(intent)
  }
}
