package nl.jqno.quickmail

import android.app.Activity
import android.os.Bundle
import android.text.util.Linkify
import android.widget._

import FindView._

class AboutActivity extends Activity with FindView {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.about)
    Linkify.addLinks(findView[TextView](R.id.about_website), Linkify.WEB_URLS)
    findView[Button](R.id.about_close).onClick { _ => finish }
  }
}
