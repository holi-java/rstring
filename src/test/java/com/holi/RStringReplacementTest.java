package com.holi;

import com.holi.Replaceable.Context;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by selonj on 16-9-5.
 */
public class RStringReplacementTest {
  @Test public void replaceAllIfAllMatched() throws Exception {
    RString foo = new RString("foo");

    String result = foo.replace("[a-z]", toUpperCase());

    assertThat(result, equalTo("FOO"));
  }

  @Test public void returnDirectlyIfMismatched() throws Exception {
    RString foo = new RString("0");

    String result = foo.replace("^[a-z]", toUpperCase());

    assertThat(result, equalTo("0"));
  }

  @Test public void preserveTheHeadingMismatched() throws Exception {
    RString foo = new RString("1f");

    String result = foo.replace("[a-z]", toUpperCase());

    assertThat(result, equalTo("1F"));
  }

  @Test public void preserveTheSurroundingMismatched() throws Exception {
    RString foo = new RString("f1f");

    String result = foo.replace("[a-z]", toUpperCase());

    assertThat(result, equalTo("F1F"));
  }

  @Test public void preserveTheTailingMismatched() throws Exception {
    RString foo = new RString("f1");

    String result = foo.replace("[a-z]", toUpperCase());

    assertThat(result, equalTo("F1"));
  }

  private Context<Context<Integer, String>, String> toUpperCase() {
    return (groups) -> groups.get(0).toUpperCase();
  }
}
