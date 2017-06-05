/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.db.integration.streaming;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.core.api.util.StreamingUtils.streamingContent;
import org.mule.extension.db.integration.AbstractDbIntegrationTestCase;
import org.mule.runtime.api.streaming.bytes.CursorStreamProvider;
import org.mule.runtime.api.streaming.object.CursorIterator;
import org.mule.runtime.api.streaming.object.CursorIteratorProvider;
import org.mule.runtime.core.api.Event;
import org.mule.runtime.core.streaming.StreamingManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;

public class DbStreamingTestCase extends AbstractDbIntegrationTestCase {

  private StreamingManager streamingManager;

  @Override
  protected String[] getFlowConfigurationResources() {
    return new String[] {"integration/streaming/streaming-config.xml"};
  }

  @Override
  protected void doSetUp() throws Exception {
    streamingManager = muleContext.getRegistry().lookupObject(StreamingManager.class);
  }

  @Test
  public void insertAndSelectBlobWhileStreamingItsContent() throws Exception {
    String data = randomAlphabetic(1024);
    InputStream stream = new ByteArrayInputStream(data.getBytes());
    CursorStreamProvider payload =
        (CursorStreamProvider) streamingContent(stream, streamingManager.forBytes().getDefaultCursorProviderFactory(),
                                                testEvent());
    flowRunner("insert").withPayload(payload).run();

    Event responseEvent = flowRunner("select").keepStreamsOpen().run();
    Object response = responseEvent.getMessage().getPayload().getValue();

    assertThat(response, is(instanceOf(CursorIteratorProvider.class)));
    CursorIterator<Map<String, Object>> iterator = ((CursorIteratorProvider) response).openCursor();

    try {
      Map<String, Object> row = iterator.next();
      assertThat(row.get("ID"), is(88));
      Object blob = row.get("PICTURE");
      assertThat(blob, is(instanceOf(CursorStreamProvider.class)));
    } finally {
      iterator.close();
    }

  }
}