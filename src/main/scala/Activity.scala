package nl.jqno.quickmail

import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.widget.TextView

class QuickMailActivity extends Activity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new TextView(this) {
      setText("hello, world")
    })
  }
}
