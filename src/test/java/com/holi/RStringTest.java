package com.holi;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * Created by selonj on 16-9-5.
 */
public class RStringTest {

  @Test public void toJavaString() throws Exception {
    assertThat(RString.valueOf("foo").toString(), equalTo("foo"));
  }

  @Test public void equality() throws Exception {
    assertThat(new RString("foo"), equalTo(new RString("foo")));
    assertThat(new RString("foo"), not(equalTo(new RString(new StringBuilder("foo")))));
    assertThat(new RString("foo"), not(equalTo(new RString("bar"))));
    assertThat(new RString("foo"), not(equalTo("foo")));
    assertThat(new RString("foo"), not(equalTo(null)));
  }

  @Test public void hash() throws Exception {
    assertThat(RString.valueOf("foo").hashCode(), equalTo("foo".hashCode()));
  }

  @Test(expected = NullPointerException.class) public void throwsNullPointerExceptionIfCreateWithANullValue() throws Exception {
    new RString(null);
  }

  @Test public void createRStringWithFactoryMethodCanAcceptANullValue() throws Exception {
    assertThat(RString.valueOf(null), equalTo(new RString("null")));
  }
}
