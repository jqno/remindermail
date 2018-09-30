package nl.jqno.remindermail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        linkify(about_explanation)
        linkify(about_privacy)
        linkify(about_website)

        about_close.setOnClickListener { finish() }
    }

    fun linkify(textView: TextView) {
        textView.movementMethod = LinkMovementMethod.getInstance()
        val text = textView.text.toString()
        textView.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    }
}
