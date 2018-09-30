package nl.jqno.remindermail

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_config.*

const val pickContact = 1337

class ConfigActivity : AppCompatActivity() {

    private val state by lazy { State(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        step2_go.setOnClickListener { selectMail() }
        step3_clear.setOnClickListener { clearTag() }
        done.setOnClickListener { finish() }
        about.setOnClickListener { openAbout() }

        step3_tag.setText(state.tag())
        step3_tag.setSelection(step3_tag.text.length)
        step3_tag.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                tagChanged()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
    }

    private fun selectMail() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, pickContact)
    }

    private fun clearTag() {
        step3_tag.setText("")
        state!!.setTag("")
    }

    private fun openAbout() {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    private fun tagChanged() {
        val tag = step3_tag.text.toString()
        state.setTag(tag)
    }
}
