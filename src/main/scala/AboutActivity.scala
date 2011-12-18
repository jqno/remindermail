package nl.jqno.quickmail

import android.app.Activity
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget._

import FindView._

class AboutActivity extends Activity with FindView {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.about)
    linkify(findView[TextView](R.id.about_explanation))
    linkify(findView[TextView](R.id.about_website))
    findView[Button](R.id.about_close).onClick { _ => finish }
  }

  private def linkify(textView: TextView) {
    textView.setMovementMethod(LinkMovementMethod.getInstance)
    val text = textView.getText.toString
    textView.setText(Html.fromHtml(text))
  }
}
