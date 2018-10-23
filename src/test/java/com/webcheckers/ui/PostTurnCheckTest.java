package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.Player;
import freemarker.template.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostTurnCheckTest {

  //friendly objects//
  private Gson gson = new Gson();
  private Player redPlayer;
  private Player whitePlayer;

  //non-friendly objects//
  private Request request;
  private Response response;
  private Session session;
  private TemplateEngine templateEngine;


  @BeforeEach
  void setUp() {


  }
}