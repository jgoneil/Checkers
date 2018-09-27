<!DOCTYPE html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <title>${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
  <div class="page">

    <h1>${header}</h1>

    <div class="navigation">
      <a href="/">my home</a>
    </div>
    
    <#if attemptFailed>
      <div class="error">
        <p>Username already in use or was entered improperly. Please try again</p>
        <p>Note: Usernames can only include alpha numeric characters and spaces</p>
      </div>
    </#if>

    <div class="body">
      <form action="./postSignin" method="POST">
        <p>Please input your username below</p>
        <input name="username" type="text" />
        <button type="submit">Ok</button>
      </form>
    </div>

  </div>
</body>
</html>
