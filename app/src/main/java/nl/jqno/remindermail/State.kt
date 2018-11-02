package nl.jqno.remindermail

import android.content.Context

const val settingsId = "state"
const val nameId = "name"
const val mailId = "mail"
const val tagId = "tag"

class State(ctx: Context) {
    private val prefs = ctx.getSharedPreferences(settingsId, Context.MODE_PRIVATE)

    val name: String? get() = prefs.getString(nameId, null)
    val mail: String? get() = prefs.getString(mailId, null)

    fun setNameAndMail(name: String, mail: String) {
        set(nameId, name)
        set(mailId, mail)
    }

    var tag: String
        get() {
            val tag = prefs.getString(tagId, null)
            return if (tag == null && name == null)
                ""
            else if (tag == null)
                "ReminderMail"
            else
                tag
        }
        set(value) {
            set(tagId, value)
        }

    private fun set(id: String, value: String) {
        val editor = prefs.edit()
        editor.putString(id, value)
        editor.apply()
    }
}
