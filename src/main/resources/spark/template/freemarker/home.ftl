<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <meta http-equiv="refresh" content="10">
    <title>${title} | Web Checkers</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
  <div class="page">
  
    <h1>Web Checkers</h1>
    
    <div class="navigation">
      <a href="/">my home</a>
      <#if !signedin>
        <a href="/signin" class="tab">Sign in</a>
      <#else>
        <a href="./signout">sign out[${user}]</a>
      </#if>
    </div>
    
    <div class="body">
      <#if message??>
        <p class="error">Player currently in game. Please select a different player.</p>
      </#if>
      <p>Welcome to the world of online Checkers.</p>

      <#if signedin>
        <p>Other users currently signed in: </p>
        <#if onlyOne>
          <p>You're the only one currently logged in</p>
        <#else>
          <#list users as player>
            <p><a href="/game?${player}" name=otherPlayer>
              ${player}
            </a></p>
          </#list>
        </#if>
      <#else>
        <p>Number of players in the game: ${users}</p>
      </#if>
    </div>
    
  </div>
</body>
</html>
