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

    <div class="body">
      <form action="./postSignin" method="POST">
        <p>Please input your username below</p>
        <input name="username" />
        <button type="submit">Ok</button>
      </form>
    </div>

  </div>
</body>
</html>
