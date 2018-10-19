package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.model.Message.Type;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
class MessageTest {

  private Message message1;

  @BeforeEach
  void setUp() {
    message1 = new Message(Type.info, "hello");
  }

  @AfterEach
  void tearDown() {
    message1 = null;
  }

  @Test
  void getType() {
    assertEquals(message1.getType(), Type.info);
  }

  @Test
  void getText() {
    assertEquals(message1.getText(), "hello");
  }
}