package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.model.Row;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Application-tier")
class RowTest {

  private Row row1;
  private Row row2;

  @BeforeEach
  void setUp() throws Exception {
    row1 = new Row(1, 4, "red");
    row2 = new Row(2, 2, "white");
  }

  @AfterEach
  void tearDown() throws Exception {
    row1 = null;
  }

  @Test
  void getRowTest() {
    assertNotNull(row1.getRow());
  }

  @Test
  void getIndexTest() {

    assertEquals(1, row1.getIndex());
    assertEquals(2, row2.getIndex());
  }

  @Test
  void getSpaceTest() {
    assertNotNull(row1.getSpace(0));

  }

  @Test
  void iteratorTest() {
    assertNotNull(row1.iterator());
  }
}