package nl.jqno.remindermail

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_config.*

const val pickContact = 1337

class ConfigActivity : AppCompatActivity() {

    private val state by lazy { State(this) }
    private var nameCandidate = ""
    private var emailCandidates = emptyList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        config_scroller.fullScroll(View.FOCUS_UP)
        registerForContextMenu(config_invisible_context_menu)

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

        paintMail()
    }

    private fun selectMail() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), pickContact)
        }
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, pickContact)
    }

    private fun clearTag() {
        step3_tag.setText("")
        state.setTag("")
    }

    private fun openAbout() {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    private fun tagChanged() {
        val tag = step3_tag.text.toString()
        state.setTag(tag)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickContact && resultCode == Activity.RESULT_OK)
            data?.data?.let { updateSettings(it) }
    }

    private fun updateSettings(contact: Uri) {
        val cr = contentResolver
        val cursor = cr.query(contact, null, null, null, null)
        cursor?.use { cur ->
            if (cur.moveToFirst()) {
                nameCandidate = getString(cur, ContactsContract.Contacts.DISPLAY_NAME)
                emailCandidates = emptyList()

                val id = getString(cur, ContactsContract.Contacts._ID)
                val mailCursor = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?", arrayOf(id), null)
                mailCursor?.use { mc ->
                    while (mc.moveToNext()) {
                        val e = getString(mc, ContactsContract.CommonDataKinds.Email.DATA)
                        emailCandidates += e
                    }
                }

                when (emailCandidates.size) {
                    0 -> {
                        Toast.makeText(this, "No e-mail address found for this contact", Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        state.setNameAndMail(nameCandidate, emailCandidates[0])
                        paintMail()
                    }
                    else -> {
                        openContextMenu(config_invisible_context_menu)
                    }
                }
            }
        }
    }

    private fun getString(cursor: Cursor, id: String): String =
            cursor.getString(cursor.getColumnIndex(id))

    private fun paintMail() {
        if (state.mail() == null) {
            step2_name.text = ""
            step2_email.text = ""
            step2_empty.visibility = View.VISIBLE
            step2_settingbox.visibility = View.GONE
        }
        else {
            step2_name.text = state.name()!!
            step2_email.text = state.mail()!!
            step2_empty.visibility = View.GONE
            step2_settingbox.visibility = View.VISIBLE
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.setHeaderTitle(R.string.config_step2_pick_mail)
        emailCandidates.withIndex().forEach {
            menu?.add(0, it.index, 0, it.value)
        }
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        state.setNameAndMail(nameCandidate, emailCandidates[item!!.itemId])
        paintMail()
        return true
    }
}
