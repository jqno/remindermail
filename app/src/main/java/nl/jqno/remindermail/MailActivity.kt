package nl.jqno.remindermail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils

val AcceptedAttachmentTypes = listOf("image/", "application/pdf", "application/x-pdf")

class MailActivity : AppCompatActivity() {

    private val state by lazy { State(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        if (state.mail == null)
            configure()
        else
            mail()
        finish()
    }

    private fun configure() {
        val intent = Intent(this, ConfigActivity::class.java)
        startActivity(intent)
    }

    private fun mail() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"

        intent.putExtra(Intent.EXTRA_EMAIL, getRecipient())
        intent.putExtra(Intent.EXTRA_SUBJECT, getSubject())
        intent.putExtra(Intent.EXTRA_TEXT, getText())

        getAttachment()?.let { intent.putExtra(Intent.EXTRA_STREAM, it) }

        startActivity(intent)
    }

    private fun getRecipient() = arrayOf("%s <%s>".format(state.name!!, state.mail!!))

    private fun getSubject(): String {
        val tag = state.tag
        val subject = getString(Intent.EXTRA_SUBJECT)
        val hasTag = !TextUtils.isEmpty(tag)
        val hasSubject = !TextUtils.isEmpty(subject)
        val sb = StringBuilder()

        if (hasTag) sb.append("[$tag]")
        if (hasTag && hasSubject) sb.append(" ")
        if (hasSubject) sb.append(subject)
        return sb.toString()
    }

    private fun getAttachment(): Uri? {
        val type = intent.type
        val isActionSend = intent.action == Intent.ACTION_SEND
        val isTypeOk = (type != null) && AcceptedAttachmentTypes.any { type.startsWith(it) }
        return if (isActionSend && isTypeOk)
            intent.getParcelableExtra(Intent.EXTRA_STREAM)
        else
            null
    }

    private fun getText() = getString(Intent.EXTRA_TEXT)

    private fun getString(id: String): String? {
        return if (intent.action == Intent.ACTION_SEND)
            intent.getStringExtra(id)
        else
            ""
    }
}