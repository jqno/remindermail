package nl.jqno.remindermail

import android.content.Context

const val settingsId = "state"
const val nameId = "name"
const val mailId = "mail"
const val tagId = "tag"

class State(ctx: Context) {
    private val prefs = ctx.getSharedPreferences(settingsId, Context.MODE_PRIVATE)

    fun name(): String? = prefs.getString(nameId, null)
    fun mail(): String? = prefs.getString(mailId, null)
    fun tag(): String {
        val tag = prefs.getString(tagId, null)
        if (tag == null && name() == null)
            return ""
        else if (tag == null)
            return "ReminderMail"
        else
            return tag
    }

    fun setNameAndMail(name: String, mail: String) {
        set(nameId, name)
        set(mailId, mail)
    }

    fun setTag(tag: String) {
        set(tagId, tag)
    }

    private fun set(id: String, value: String) {
        val editor = prefs.edit()
        editor.putString(id, value)
        editor.apply()
    }
}
