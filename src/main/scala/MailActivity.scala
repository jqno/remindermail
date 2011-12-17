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
    val (subject, text) = determineContent

    val intent = new Intent(Intent.ACTION_SEND)
    intent.setType("plain/text")
    intent.putExtra(Intent.EXTRA_EMAIL, Array(recipient))
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, text)

    startActivity(intent)
  }

  private def determineContent: (String, String) = {
    val intent = getIntent
    if (intent.getAction == Intent.ACTION_SEND)
      (intent.getStringExtra(Intent.EXTRA_SUBJECT), intent.getStringExtra(Intent.EXTRA_TEXT))
    else
      ("", "")
  }

  private def configure {
    val intent = new Intent(this, classOf[ConfigActivity])
    startActivity(intent)
  }
}
