package com.webcheckers.appl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RowTest {
  Row row;

  @BeforeEach
  public void setUp() throws Exception {
    row = new Row(1, 4, "red");
  }

  @AfterEach
  void tearDown() throws Exception {
    row = null;
  }

  @Test
  void getRow() {
    assertNotNull(row.getRow());
  }

  @Test
  void index() {
    assertEquals(1, row.index());
  }

  @Test
  void iterator() {
    assertNotNull(row.iterator());
  }
}