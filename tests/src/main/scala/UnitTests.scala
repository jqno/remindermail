package nl.jqno.remindermail.tests

import junit.framework.Assert._
import _root_.android.test.AndroidTestCase

class UnitTests extends AndroidTestCase {
  def testPackageIsCorrect {
    assertEquals("nl.jqno.remindermail", getContext.getPackageName)
  }
}
