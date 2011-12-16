package nl.jqno.quickmail.tests

import junit.framework.Assert._
import _root_.android.test.AndroidTestCase

class UnitTests extends AndroidTestCase {
  def testPackageIsCorrect {
    assertEquals("nl.jqno.quickmail", getContext.getPackageName)
  }
}