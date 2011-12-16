package nl.jqno.quickmail

import android.app.Activity
import android.view.View
import android.view.View.OnClickListener

trait FindView extends Activity {
  def findView[WidgetType <: View](id: Int): WidgetType = findViewById(id).asInstanceOf[WidgetType]
  def find(id: Int): View = findView[View](id)
}

class ViewWithOnClick(view: View) {
  def onClick(action: View => Any) {
    view.setOnClickListener(new View.OnClickListener() {
      def onClick(v: View) { action(v) }
    })
  }
}

object FindView extends Activity {
  implicit def addOnClickToViews(view: View) = new ViewWithOnClick(view)
}
